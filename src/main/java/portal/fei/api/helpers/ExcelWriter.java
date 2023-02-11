package portal.fei.api.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import jxl.write.WriteException;
import org.apache.poi.ss.usermodel.*;

public class ExcelWriter {

    private Workbook workbook;
    private Sheet sheet;
    private FileInputStream fis;
    public ExcelWriter(String fileName) throws IOException {
        fis = new FileInputStream(fileName);
        workbook = WorkbookFactory.create(fis);

        sheet = workbook.getSheetAt(0);
    }

    public void writeToCell(HashMap<String, String> variables) throws WriteException {
        for (Row row : sheet){
            for (Cell cell : row){
                if (cell.getCellType() == CellType.STRING){
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.contains("$")){
                        String val = variables.get(cellValue);
                        cell.setCellValue(val);
                    }
                }
            }
        }
    }

    public void close(String fileName) throws IOException, WriteException {
        fis.close();
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
    }
}
