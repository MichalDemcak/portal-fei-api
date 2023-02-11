package portal.fei.api.controllers.up;

import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portal.fei.api.domain.isp.StudentRequest;
import portal.fei.api.domain.up.SubjectsRequest;
import portal.fei.api.domain.up.UpRequest;
import portal.fei.api.helpers.WordReplacer;
import portal.fei.api.repositories.interfaces.up.SubjectsRequestRepository;
import portal.fei.api.repositories.interfaces.up.UpRequestRepository;
import portal.fei.api.resources.FileResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/upRequest")
@CrossOrigin("http://localhost:4200")
public class UpRequestController {

    @Autowired
    private UpRequestRepository upRequestRepository;

    @Autowired
    private SubjectsRequestRepository subjectsRequestRepository;

    @PostMapping("")
    public ResponseEntity<Integer> create(@RequestBody UpRequest upRequest) {
        upRequest.setCreatedAt(Timestamp.from(Instant.now()));
        @Nullable
        UpRequest stored = this.upRequestRepository.save(upRequest);
        List<SubjectsRequest> subjectList = upRequest.getSubjectList();
        for (SubjectsRequest subject : subjectList){
            subject.setUpRequest(upRequest.getId());
        }



        List<SubjectsRequest> stored2 = this.subjectsRequestRepository.saveAll(subjectList);
        return new ResponseEntity<Integer>(HttpStatus.CREATED);
    }


    @GetMapping("")
    public ResponseEntity<List<UpRequest>> all(){
        return new ResponseEntity<List<UpRequest>>(this.upRequestRepository.findAll(), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<UpRequest> update(@RequestBody UpRequest upRequest){
        upRequest.setUpdatedAt(Timestamp.from(Instant.now()));
        this.upRequestRepository.save(upRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UpRequest> findById(@PathVariable Integer id){
        Optional<UpRequest> studentRequest = this.upRequestRepository.findById(id);

        if (studentRequest.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(studentRequest.get(), HttpStatus.OK);
    }

    @PostMapping("{id}/add-subject")
    public ResponseEntity addSubject(@RequestParam SubjectsRequest subjectsRequest, @PathVariable Integer id){

        Optional<UpRequest> opReq = this.upRequestRepository.findById(id);

        if (opReq.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        UpRequest request = opReq.get();

        request.addSubjectRequest(subjectsRequest);
        request.setUpdatedAt(Timestamp.from(Instant.now()));
        this.upRequestRepository.save(request);


        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("{id}/get-all-subjects")
    public ResponseEntity allSubjects(@PathVariable Integer id){

        return new ResponseEntity<List<SubjectsRequest>>(subjectsRequestRepository.findByUpRequest(id), HttpStatus.OK);
    }

    @PostMapping("/set-subjects")
    public ResponseEntity setSubjectsForStudent(@RequestParam List<SubjectsRequest> subjectsRequestsList){
        @Nullable
        List<SubjectsRequest> stored = this.subjectsRequestRepository.saveAll(subjectsRequestsList);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("{id}/delete-subject")
    public ResponseEntity deleteSubject(@PathVariable Integer id){

        subjectsRequestRepository.deleteById(id);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/update-request")
    public ResponseEntity updateUpRequest(@RequestBody UpRequest upRequest){
        Optional<UpRequest> stored = upRequestRepository.findById(upRequest.getId());

        if (stored.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        UpRequest upRe = stored.get();

        upRe.setId(upRequest.getId());
        upRe.setName(upRequest.getName());
        upRe.setBirthDate(upRequest.getBirthDate());
        upRe.setBirthTown(upRequest.getBirthTown());
        upRe.setBirthIdentificationNumber(upRequest.getBirthIdentificationNumber());
        upRe.setAddress(upRequest.getAddress());
        upRe.setPsc(upRequest.getPsc());
        upRe.setYear(upRequest.getYear());
        upRe.setTypeOfStudy(upRequest.getTypeOfStudy());
        upRe.setKindOfStudy(upRequest.getKindOfStudy());
        upRe.setConsultantPlace(upRequest.getConsultantPlace());
        upRe.setFaculty(upRequest.getFaculty());
        upRe.setUniversity(upRequest.getUniversity());
        upRe.setDeanDecision(upRequest.getDeanDecision());
        upRe.setStudyYear(upRequest.getStudyYear());
        upRe.setDeanDecisionDate(upRequest.getDeanDecisionDate());
        upRe.setReferentStatement(upRequest.getReferentStatement());
        upRe.setReferentId(upRequest.getReferentId());
        upRe.setReferentName(upRequest.getReferentName());
        upRe.setDeletedAt(upRequest.getDeletedAt());
        upRe.setUpdatedAt(upRequest.getUpdatedAt());
        upRe.setCreatedAt(upRequest.getCreatedAt());

        List<SubjectsRequest> newSubjectList = upRequest.getSubjectList();
        List<SubjectsRequest> listOfSubjects = new ArrayList<>();
        for (SubjectsRequest subject : newSubjectList){
            if (subject.getId() == null){
                //doplnit id requestu
                subject.setUpRequest(upRe.getId());
                subjectsRequestRepository.save(subject);
                listOfSubjects.add(subject);
            }
            else{
                SubjectsRequest old = subjectsRequestRepository.findById(subject.getId()).get();
                old.setName(subject.getName());
                old.setExamDate(subject.getExamDate());
                old.setExamGrade(subject.getExamGrade());
                old.setExaminerName(subject.getExaminerName());
                if(!old.getStatementOfCatedraHead().equals(subject.getStatementOfCatedraHead())){
                    subject.setUpdateddAt(Timestamp.from(Instant.now()));
                }
                if(!old.getSimiliarSubject().equals(subject.getSimiliarSubject())){
                    subject.setUpdateddAt(Timestamp.from(Instant.now()));
                }
                old.setStatementOfCatedraHead(subject.getStatementOfCatedraHead());
                old.setSimiliarSubject(subject.getSimiliarSubject());
                old.setCreatedAt(subject.getCreatedAt());
                old.setDeletedAt(subject.getDeletedAt());
                old.setUpdateddAt(subject.getUpdateddAt());
                subjectsRequestRepository.save(old);
                listOfSubjects.add(old);
            }
        }
        upRe.setUpdatedAt(Timestamp.from(Instant.now()));
        upRe.setSubjectList(listOfSubjects);
        upRequestRepository.save(upRe);


        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<FileResource> getRequestFile(@PathVariable Integer id) throws FileNotFoundException {
        Optional<UpRequest> studentRequest = this.upRequestRepository.findById(id);

        if (studentRequest.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UpRequest request = studentRequest.get();

        String path = Paths.get("").toAbsolutePath().toString() + "\\src\\main\\resources";
        String source = path + "\\templates\\ziadost_up.docx";
        String output =  path + "\\saved\\ziadost_up.docx";

        HashMap<String, String> values = new HashMap<>();
        values.put("${name}", request.getName() == null ? " " : request.getName());
        values.put("${address}", request.getAddress() == null ? " " : request.getAddress());
        values.put("${birthDate}", request.getBirthDate() == null ? " " : new SimpleDateFormat("dd.MM.yyyy").format(request.getBirthDate()));
        values.put("${birthTown}", request.getBirthTown() == null ? " " : request.getBirthTown());
        values.put("${birthIdentificationNumber}", request.getBirthIdentificationNumber() == null ? "" : request.getBirthIdentificationNumber());
        values.put("${psc}", request.getPsc() == null ? " " : request.getPsc());
        values.put("${year}", request.getYear() == 0 ? " " : String.valueOf(request.getYear()));
        values.put("${kindOfStudy}", request.getKindOfStudy() == null ? " " : request.getKindOfStudy());
        values.put("${typeOfStudy}", request.getTypeOfStudy() == null ? " " : request.getTypeOfStudy() );
        values.put("${consultantPlace}", request.getConsultantPlace() == null ? " " : request.getConsultantPlace());
        values.put("${studyYear}", request.getStudyYear() == null ? " " : request.getStudyYear());
        values.put("${faculty}", request.getFaculty() == null ? " " : request.getFaculty());
        values.put("${university}", request.getUniversity() == null ? " " : request.getUniversity());
        values.put("${createdAt}", request.getCreatedAt() == null ? " " :  new SimpleDateFormat("dd.MM.yyyy").format(request.getCreatedAt()));
        values.put("${deanDecision}", request.getDeanDecision() == null ? " " : request.getDeanDecision());
        values.put("${deanDecisionDate}",request.getDeanDecisionDate() == null ? " " : new SimpleDateFormat("dd.MM.yyyy").format(request.getDeanDecisionDate()));
        values.put("${referentStatement}", request.getReferentStatement() == null ? " " : request.getReferentStatement());

        for(int i = 0; i < 10; i++){
            if(i >= request.getSubjectList().size()){
                values.put("${subject["+ String.valueOf(i) +"][1]}", " ");
                values.put("${subject["+ String.valueOf(i) +"][2]}", " ");
                values.put("${subject["+ String.valueOf(i) +"][3]}", " ");
                values.put("${subject["+ String.valueOf(i) +"][4]}", " ");
                values.put("${subject["+ String.valueOf(i) +"][5]}", " ");
                values.put("${subject["+ String.valueOf(i) +"][6]}", " ");
                values.put("${subject["+ String.valueOf(i) +"][7]}", " ");
            }else{
                SubjectsRequest subject = request.getSubjectList().get(i);
                values.put("${subject["+ String.valueOf(i) +"][1]}", subject.getName() == null ? " " : subject.getName());
                values.put("${subject["+ String.valueOf(i) +"][2]}", subject.getExamDate() == null ? " " : new SimpleDateFormat("dd.MM.yyyy").format(subject.getExamDate()));
                values.put("${subject["+ String.valueOf(i) +"][3]}", subject.getExamGrade() == null ? " " : subject.getExamGrade());
                values.put("${subject["+ String.valueOf(i) +"][4]}", subject.getExaminerName() == null ? " " : subject.getExaminerName());
                values.put("${subject["+ String.valueOf(i) +"][5]}", subject.getStatementOfCatedraHead() == null ? " " : subject.getStatementOfCatedraHead());
                values.put("${subject["+ String.valueOf(i) +"][6]}", subject.getSimiliarSubject() == null ? " " : subject.getSimiliarSubject());
                values.put("${subject["+ String.valueOf(i) +"][7]}", subject.getUpdateddAt() == null ? " " : new SimpleDateFormat("dd.MM.yyyy").format(subject.getUpdateddAt()));
            }
        }


        byte[] out = null;

        try {
            out = WordReplacer.getDocument(Files.readAllBytes(Paths.get(source)), values);
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
        try (FileOutputStream fos = new FileOutputStream("testCopy.docx")) {
            fos.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new ResponseEntity<>(new FileResource( "UP -" + request.getName(), ".docx", out), HttpStatus.OK);
    }

}
