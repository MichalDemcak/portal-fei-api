package portal.fei.api.controllers.isp;

import jxl.write.WriteException;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.fei.api.domain.EmailQueue;
import portal.fei.api.domain.isp.IspSubjects;
import portal.fei.api.domain.isp.StudentRequest;
import portal.fei.api.domain.isp.StudentRequestStates;
import portal.fei.api.domain.up.SubjectsRequest;
import portal.fei.api.helpers.ExcelWriter;
import portal.fei.api.helpers.RandomString;
import portal.fei.api.helpers.WordReplacer;
import portal.fei.api.repositories.interfaces.EmailQueueRepository;
import portal.fei.api.domain.isp.requests.ReturnToStudentRequest;
import portal.fei.api.repositories.interfaces.isp.IspSubjectsRepository;
import portal.fei.api.repositories.interfaces.isp.StudentRequestRepository;
import portal.fei.api.repositories.interfaces.isp.StudentRequestStatesRepository;
import portal.fei.api.resources.FileResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import portal.fei.api.helpers.SemesterFromYear;

@Controller
@RequestMapping("/api/request")
@CrossOrigin("http://localhost:4200")
public class StudentRequestController {

    @Autowired
    private StudentRequestRepository studentRequestRepository;

    @Autowired
    private IspSubjectsRepository ispSubjectsRepository;

    @Autowired
    private StudentRequestStatesRepository studentRequestStatesRepository;

    @Autowired
    private EmailQueueRepository emailQueueRepository;

    @GetMapping("")
    public ResponseEntity<Collection<StudentRequest>> findAll(@RequestParam("name") Optional<String> name, @RequestParam("stateId") Optional<Long> stateId){
        Collection<StudentRequest> studentRequestCollection = null;

        if (name.isPresent() && stateId.isPresent()){
            studentRequestCollection = this.studentRequestRepository.findByNameState(stateId.get(), name.get());
        } else if(name.isPresent()){
            studentRequestCollection = this.studentRequestRepository.findByName(name.get());
        } else if (stateId.isPresent()){
            studentRequestCollection = this.studentRequestRepository.findByState(stateId.get());
        } else {
            studentRequestCollection = this.studentRequestRepository.findAll();
        }

        return new ResponseEntity<>(studentRequestCollection, HttpStatus.OK);
    }

    //TODO: Tu by sme nemali posielat studentId jak parameter, ale ziskat ho z autentifikacie ktoru este nemame tuna rozchodenu:D
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Collection<StudentRequest>> studentRequests(@PathVariable String studentId){
        Collection<StudentRequest> studentRequests = this.studentRequestRepository.studentRequests(studentId);
        return new ResponseEntity<>(studentRequests, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentRequest> findById(@PathVariable Long id){
        Optional<StudentRequest> studentRequest = this.studentRequestRepository.findById(id);

        if (studentRequest.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(studentRequest.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<FileResource> getRequestFile(@PathVariable Long id){
        Optional<StudentRequest> studentRequest = this.studentRequestRepository.findById(id);

        if (studentRequest.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = studentRequest.get();

        String path = Paths.get("").toAbsolutePath().toString() + "\\src\\main\\resources";
        String source = path + "\\templates\\student_request.docx";
        String output =  path + "\\saved\\student_request.docx";

        HashMap<String, String> values = new HashMap<>();
        values.put("${nameSurname}", request.getStudentName());
        values.put("${address}", request.getAddress());
        values.put("${studyProgramme}", request.getStudyProgramme());
        values.put("${degree}", request.getDegree());
        values.put("${year}", String.valueOf(request.getYear()));
        values.put("${purpose}", request.getPurpose() != null ? request.getPurpose() : " ");
        values.put("${date}", new SimpleDateFormat("dd.MM.yyyy").format(request.getCreatedAt()));
        values.put("${subdeanStatement}", request.getSubdeanStatement() != null ? request.getSubdeanStatement() : " ");
        values.put("${deanStatement}", request.getDeanStatement() != null ? request.getDeanStatement() : " ");

        byte[] out = null;

        try {
            out = WordReplacer.getDocument(Files.readAllBytes(Paths.get(source)), values);
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }

        return new ResponseEntity<>(new FileResource( "IŠP -" + request.getStudentName(), ".docx", out), HttpStatus.OK);
    }
    @GetMapping("{id}/isp-attachment")
    public ResponseEntity<FileResource> getIspRequestAttachment(@PathVariable Long id){
        Optional<StudentRequest> studentRequest = this.studentRequestRepository.findById(id);

        if (studentRequest.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = studentRequest.get();

        String path = Paths.get("").toAbsolutePath().toString() + "\\src\\main\\resources";
        String source = path + "\\templates\\isp.xls";

        String temp =  path + "\\temp\\" + RandomString.getAlphaNumericString(25) + ".xls";
        Path original = Paths.get(source);

        new File(temp);
        Path toCopy = Paths.get(temp);

        try {
            Files.copy(original, toCopy, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ExcelWriter excelWriter = null;

        try {
            excelWriter = new ExcelWriter(temp);
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }

        HashMap<String, String> variables = new HashMap<>();
        variables.put("${name}", request.getStudentName());
        variables.put("${studyProgramme}",request.getStudyProgramme());
        variables.put("${degree}", request.getDegree());
        variables.put("${semester}", SemesterFromYear.getSemesterFromYear(request));
        variables.put("${date}", new SimpleDateFormat("dd.MM.yyyy").format(request.getCreatedAt()));

        for(int i = 0; i < request.getSubjectsList().size(); i++){
            IspSubjects subject = request.getSubjectsList().get(i);
            variables.put("${subject["+ String.valueOf(i) +"][1]}", subject.getName());
            variables.put("${subject["+ String.valueOf(i) +"][2]}", subject.getRozsah());
            variables.put("${subject["+ String.valueOf(i) +"][3]}", subject.getSposobUkoncenia());
            variables.put("${subject["+ String.valueOf(i) +"][4]}", subject.getPodmienkyUznania());
            variables.put("${subject["+ String.valueOf(i) +"][5]}", subject.getEvaluationTerms());
            variables.put("${subject["+ String.valueOf(i) +"][6]}", subject.getTeacher().getName());
        }

        try {
            excelWriter.writeToCell(variables);
        } catch (WriteException e){
            throw new RuntimeException(e.getMessage());
        }

        try {
            excelWriter.close(temp);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        FileResource fileResource = null;

        try {
            fileResource = new FileResource("Príloha k IŠP - " + request.getStudentName() + " - predmety", ".xls", Files.readAllBytes(Paths.get(temp)));
            Files.delete(Paths.get(temp));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(fileResource,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> create(@RequestBody StudentRequest studentRequest){
        Optional<StudentRequestStates> opState = this.studentRequestStatesRepository.findById(Long.valueOf("1"));

        if (opState.isEmpty()){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        studentRequest.setStudentRequestState(opState.get());
        studentRequest.setCreatedAt(Timestamp.from(Instant.now()));

        this.studentRequestRepository.save(studentRequest);

        return new ResponseEntity<>(studentRequest.getId(), HttpStatus.CREATED);
    }

    @PostMapping("{id}/add-attachment")
    public ResponseEntity addAttachment(@RequestParam("file") MultipartFile file, @PathVariable Long id){
        String path = Paths.get("").toAbsolutePath().toString() + "\\src\\main\\resources\\isp-attachments\\";
        String fileHash = RandomString.getAlphaNumericString(200) + "." + FileNameUtils.getExtension(file.getOriginalFilename());

        Optional<StudentRequest> opReq = this.studentRequestRepository.findById(id);

        if (opReq.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = opReq.get();

        if (request.getAttachmentHash() != null){
            File f = new File(path + request.getAttachmentHash());
            f.delete();
        }

        try {
            file.transferTo(new File(path + fileHash));
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        request.setAttachmentHash(fileHash);
        request.setUpdatedAt(Timestamp.from(Instant.now()));
        this.studentRequestRepository.save(request);


        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("{id}/student-attachment")
    public ResponseEntity<FileResource> getStudentAttachment(@PathVariable Long id){
        Optional<StudentRequest> opReq = this.studentRequestRepository.findById(id);

        if(opReq.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String path = Paths.get("").toAbsolutePath().toString() + "\\src\\main\\resources\\isp-attachments\\";
        StudentRequest request = opReq.get();

        byte[] bytes = null;

        try {
            bytes = Files.readAllBytes(Paths.get(path + request.getAttachmentHash()));
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(new FileResource(request.getStudentName() + " IŠP príloha", "."+FileNameUtils.getExtension(request.getAttachmentHash()), bytes), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<StudentRequest> update(@RequestBody StudentRequest studentRequest){
        studentRequest.setUpdatedAt(Timestamp.from(Instant.now()));
        this.studentRequestRepository.save(studentRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<StudentRequest> returnToStudent(@PathVariable Long id, @RequestBody ReturnToStudentRequest body){
        Optional<StudentRequest> opReq = this.studentRequestRepository.findById(id);
        Optional<StudentRequestStates> opState = this.studentRequestStatesRepository.findById(Long.valueOf("3"));

        if (opReq.isEmpty() || opState.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = opReq.get();
        StudentRequestStates state = opState.get();

        request.setStudentRequestState(state);
        request.setReturnedToStudentAt(Timestamp.from(Instant.now()));

        this.studentRequestRepository.save(request);

        EmailQueue email = new EmailQueue();
        email.setFrom("from@email.com");
        email.setTo("to@email.com");
        email.setSubject("Individuálny štúdijný plán - vrátena žiadosť");
        email.setText(body.getMessage());
        email.setCreatedAt(Timestamp.from(Instant.now()));
        this.emailQueueRepository.save(email);

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @GetMapping("{id}/approve")
    public ResponseEntity<StudentRequest> approve(@PathVariable Long id){
        Optional<StudentRequest> opReq = this.studentRequestRepository.findById(id);
        Optional<StudentRequestStates> opState = this.studentRequestStatesRepository.findById(Long.valueOf("4"));

        if (opReq.isEmpty() || opState.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = opReq.get();

        request.setStudentRequestState(opState.get());

        this.studentRequestRepository.save(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("{id}/decision-file")
    public ResponseEntity<FileResource> decisionFile(@PathVariable Long id){
        Optional<StudentRequest> opReq = this.studentRequestRepository.findById(id);

        if (opReq.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = opReq.get();

        String path = Paths.get("").toAbsolutePath().toString() + "\\src\\main\\resources";
        String source = path + "\\templates\\isp_rozhodnutie.docx";
        String output = path + "\\saved\\isp_rozhodnutie.docx";

        HashMap<String, String> values = new HashMap<>();
        values.put("${name}", request.getStudentName());
        values.put("${address}", request.getAddress());
        values.put("${studyProgramme}", request.getStudyProgramme());
        values.put("${degree}", request.getDegree() == "Bc" ? "bakalárskeho" : "inžinierskeho");
        values.put("${year}", String.valueOf(request.getYear()));
        values.put("${requestDate}", new SimpleDateFormat("dd.MM.yyyy").format(request.getCreatedAt()));
        values.put("${date}", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        values.put("${studyYear}", SemesterFromYear.getAkYear(request));
        values.put("${semester}", SemesterFromYear.getSemester(request) == "ZS" ? "zimnom" : "letnom");
        values.put("${referentName}", request.getReferent().getName());
        values.put("${psc}", request.getPsc() != null ? request.getPsc() : " ");

        byte[] out = null;

        try {
            out = WordReplacer.getDocument(Files.readAllBytes(Paths.get(source)), values);
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }

        return new ResponseEntity<>(new FileResource("IŠP Rozhodnutie - " + request.getStudentName(), ".docx", out), HttpStatus.OK);
    }

    @PostMapping("{id}/attach-subjects")
    public ResponseEntity attachSubjects(@PathVariable Long id, @RequestBody List<IspSubjects> subjects){
        Optional<StudentRequest> opReq = this.studentRequestRepository.findById(id);

        if (opReq.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = opReq.get();

        if (!request.getSubjectsList().isEmpty()){
            this.ispSubjectsRepository.deleteAll(request.getSubjectsList());
        }

        for (IspSubjects sub : subjects) {
            sub.setStudentRequestId(opReq.get().getId());
            sub.setCreatedAt(Timestamp.from(Instant.now()));
            this.ispSubjectsRepository.save(sub);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("{id}/vk-approve")
    public ResponseEntity vkApprove(@PathVariable Long id){
        Optional<StudentRequest> opReq = this.studentRequestRepository.findById(id);

        if (opReq.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = opReq.get();
        request.setApprovedByVk(true);
        request.setUpdatedAt(Timestamp.from(Instant.now()));
        this.studentRequestRepository.save(request);

        EmailQueue emailQueue = new EmailQueue();
        emailQueue.setTo("prodekan@tuke-portal.sk");
        emailQueue.setFrom("no-reply@tuke-portal.sk");
        emailQueue.setSubject("IŠP - povolenie");
        emailQueue.setText("V systéme pribudlo nové povolenie IŠP na schválenie. Žiadateľom je: " + request.getStudentName());
        this.emailQueueRepository.save(emailQueue);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("{id}/referent-approve")
    public ResponseEntity referentApprove(@PathVariable Long id){
        Optional<StudentRequest> opReq = this.studentRequestRepository.findById(id);
        Optional<StudentRequestStates> opState = this.studentRequestStatesRepository.findById(Long.valueOf("2"));

        if (opReq.isEmpty() || opState.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = opReq.get();
        request.setStudentRequestState(opState.get());
        request.setUpdatedAt(Timestamp.from(Instant.now()));

        this.studentRequestRepository.save(request);


        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{id}/subdean-approve")
    public ResponseEntity subdeanApprove(@PathVariable Long id, @RequestBody() String text){
        Optional<StudentRequest> opReq = this.studentRequestRepository.findById(id);

        if (opReq.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = opReq.get();
        request.setApprovedBySubdean(true);
        request.setSubdeanStatement(text);
        request.setUpdatedAt(Timestamp.from(Instant.now()));
        this.studentRequestRepository.save(request);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("{id}/approve-permission")
    public ResponseEntity approvePermission(@PathVariable Long id) {
        Optional<StudentRequest> opReq = this.studentRequestRepository.findById(id);
        Optional<StudentRequestStates> opStat = this.studentRequestStatesRepository.findById(Long.valueOf("4"));
        if (opReq.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        StudentRequest request = opReq.get();
        request.setPermissionApproved(true);
        request.setStudentRequestState(opStat.get());

        EmailQueue emailQueue = new EmailQueue();
        emailQueue.setFrom("no-reply@tuke-portal.sk");
        emailQueue.setTo("student@email.sk");
        emailQueue.setSubject("IŠP žiadosť");
        emailQueue.setText("Vaša žiadosť o IŠP bola schválená. V prílohách emailu nájdete všetky dokumenty.");

        return new ResponseEntity(HttpStatus.OK);
    }
}
