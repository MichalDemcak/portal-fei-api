package portal.fei.api.helpers;

import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class WordReplacer {

    private WordReplacer() {
        throw new IllegalStateException("Utility class");
    }

    public static byte[] getDocument(byte[] document, Map<String, String> replaceValues) {
        try (var byteArrayInputStream = new ByteArrayInputStream(document);
             var doc = new XWPFDocument(byteArrayInputStream)) {
            for (XWPFHeader header : doc.getHeaderList()) {
                replaceAllBodyElements(header.getBodyElements(), replaceValues);
            }
            replaceAllBodyElements(doc.getBodyElements(), replaceValues);

            var byteArrayOutputStream = new ByteArrayOutputStream();
            doc.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException("Could not generate file: " + e.getMessage());
        }
    }

    private static void replaceAllBodyElements(List<IBodyElement> bodyElements, Map<String, String> replaceValues){
        for (IBodyElement bodyElement : bodyElements) {
            if (bodyElement.getElementType() == BodyElementType.PARAGRAPH)
                replaceParagraph((XWPFParagraph) bodyElement, replaceValues);
            if (bodyElement.getElementType() == BodyElementType.TABLE)
                replaceTable((XWPFTable) bodyElement, replaceValues);
        }
    }

    private static void replaceTable(XWPFTable table, Map<String, String> replaceValues) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                for (IBodyElement bodyElement : cell.getBodyElements()) {
                    if (bodyElement.getElementType() == BodyElementType.PARAGRAPH) {
                        replaceParagraph((XWPFParagraph) bodyElement, replaceValues);
                    }
                    if (bodyElement.getElementType() == BodyElementType.TABLE) {
                        replaceTable((XWPFTable) bodyElement, replaceValues);
                    }
                }
            }
        }
    }

    public static void replaceParagraph(XWPFParagraph paragraph, Map<String, String> replaceValues) {
        List<XWPFRun> runs = paragraph.getRuns();
        for (Map.Entry<String, String> replPair : replaceValues.entrySet()) {
            String find = replPair.getKey();
            String repl = replPair.getValue();
            TextSegment found = paragraph.searchText(find, new PositionInParagraph());
            if (found != null) {
                if (found.getBeginRun() == found.getEndRun()) {
                    XWPFRun run = runs.get(found.getBeginRun());
                    CTR ctrRun = run.getCTR();
                    int sizeOfCtr = ctrRun.sizeOfTArray();
                    for (int textPosition = 0; textPosition < sizeOfCtr; textPosition++) {
                        String runText = run.getText(textPosition);
                        String replaced = runText.replace(find, repl);
                        run.setText(replaced, 0);
                    }
                } else {
                    StringBuilder b = new StringBuilder();
                    for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++) {
                        XWPFRun run = runs.get(runPos);
                        CTR ctrRun = run.getCTR();
                        int sizeOfCtr = ctrRun.sizeOfTArray();
                        for (int textPosition = 0; textPosition < sizeOfCtr; textPosition++) {
                            b.append(run.getText(textPosition));
                        }
                    }

                    String connectedRuns = b.toString();
                    String replaced = connectedRuns.replace(find, repl);

                    XWPFRun partOne = runs.get(found.getBeginRun());
                    partOne.setText(replaced, 0);

                    for (int runPos = found.getBeginRun() + 1; runPos <= found.getEndRun(); runPos++) {
                        XWPFRun partNext = runs.get(runPos);
                        partNext.setText("", 0);
                    }
                }
            }
        }
    }

    public static List<String> getKeywords(byte[] document) {
        List<String> keywordList = new ArrayList<>();

        try (var byteArrayInputStream = new ByteArrayInputStream(document);
             POITextExtractor textExtractor = ExtractorFactory.createExtractor(byteArrayInputStream);
             var documentScanner = new Scanner(textExtractor.getText())) {

            var regex = Pattern.compile("\\$\\{([^{}]*)\\}");

            List<MatchResult> keyWordsFound = documentScanner.findAll(regex).toList();

            for (MatchResult m : keyWordsFound) {
                keywordList.add(m.group(1));
            }

            return keywordList;

        } catch (IOException e) {
            throw new RuntimeException("Could not get keywords: " + e.getMessage());
        }
    }
}
