package portal.fei.api.tasks;

import org.springframework.stereotype.Component;
import portal.fei.api.domain.EmailQueue;
import portal.fei.api.domain.isp.IspSubjects;
import portal.fei.api.domain.isp.StudentRequest;
import portal.fei.api.repositories.interfaces.EmailQueueRepository;
import portal.fei.api.repositories.interfaces.isp.StudentRequestRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

@Component
public class CheckApprovedSubjectsTask implements Runnable {

    private final StudentRequestRepository studentRequestRepository;
    private final EmailQueueRepository emailQueueRepository;

    public CheckApprovedSubjectsTask(StudentRequestRepository studentRequestRepository, EmailQueueRepository emailQueueRepository){
        this.studentRequestRepository = studentRequestRepository;
        this.emailQueueRepository = emailQueueRepository;
    }

    @Override
    public void run() {
        Collection<StudentRequest> studentRequests = this.studentRequestRepository.allNotDone();
        if(studentRequests.isEmpty()){
            return;
        }

        for (StudentRequest studentRequest : studentRequests) {
            if (!studentRequest.getSubjectsList().isEmpty()){
                boolean allApproved = true;
                for (IspSubjects subject : studentRequest.getSubjectsList()){
                    if (!subject.isApproved()){
                        allApproved = false;
                        break;
                    }
                }

                if (allApproved){
                    EmailQueue emailQueue = new EmailQueue();
                    emailQueue.setFrom("no-reply@tuke-portal.sk");
                    emailQueue.setTo("veduci@katedry.sk");
                    emailQueue.setSubject("Nový IŠP na schválenie");
                    emailQueue.setText("V systéme pribudla nová žiadosť IŠP na schválenie.");
                    emailQueue.setCreatedAt(Timestamp.from(Instant.now()));
                    this.emailQueueRepository.save(emailQueue);

                    studentRequest.setReadyForVk(true);
                    studentRequest.setUpdatedAt(Timestamp.from(Instant.now()));
                    this.studentRequestRepository.save(studentRequest);
                }
            }
        }
    }
}
