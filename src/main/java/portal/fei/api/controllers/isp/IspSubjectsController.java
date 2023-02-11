package portal.fei.api.controllers.isp;

import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portal.fei.api.domain.EmailQueue;
import portal.fei.api.domain.isp.IspSubjects;
import portal.fei.api.repositories.interfaces.EmailQueueRepository;
import portal.fei.api.repositories.interfaces.isp.IspSubjectsRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/api/isp-subject")
@CrossOrigin("http://localhost:4200")
public class IspSubjectsController {

    @Autowired
    private IspSubjectsRepository repository;

    @Autowired
    private EmailQueueRepository emailQueueRepository;

    @GetMapping("")
    public ResponseEntity<List<IspSubjects>> all(){
        return new ResponseEntity<List<IspSubjects>>(this.repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity update(@RequestBody IspSubjects subject){
        Optional<IspSubjects> oldSub = this.repository.findById(subject.getId());

        if (oldSub.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        boolean sendEmailToTeacher = (oldSub.get().getTeacher() == null && subject.getTeacher() != null) || (oldSub.get().getTeacher() != null && subject.getTeacher() != null && !Objects.equals(oldSub.get().getTeacher().getId(), subject.getTeacher().getId()));

        subject.setCreatedAt(oldSub.get().getCreatedAt());
        subject.setUpdatedAt(Timestamp.from(Instant.now()));
        this.repository.save(subject);

        if (sendEmailToTeacher){
            EmailQueue emailQueue = new EmailQueue();
            emailQueue.setFrom("no-reply@tuke-portal.sk");
            emailQueue.setTo(subject.getTeacher().getEmail());
            emailQueue.setSubject("Nová žiadosť - Individuálny štúdijný plán");
            emailQueue.setText("Práve pribudla nová žiadosť pre IŠP, v ktorej je potrebné aj vaše vyjadrenie k predmetu: " + subject.getName());
            emailQueue.setCreatedAt(Timestamp.from(Instant.now()));
            this.emailQueueRepository.save(emailQueue);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
