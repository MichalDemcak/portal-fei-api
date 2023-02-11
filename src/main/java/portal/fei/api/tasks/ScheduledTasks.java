package portal.fei.api.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import portal.fei.api.repositories.interfaces.EmailQueueRepository;
import portal.fei.api.repositories.interfaces.isp.StudentRequestRepository;


@Component
public class ScheduledTasks implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private EmailQueueRepository emailQueueRepository;

    @Autowired
    private StudentRequestRepository studentRequestRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //taskScheduler.scheduleAtFixedRate(new SendEmailsTask(emailQueueRepository), 60000);
        taskScheduler.scheduleAtFixedRate(new CheckApprovedSubjectsTask(studentRequestRepository, emailQueueRepository), 6000);
    }
}
