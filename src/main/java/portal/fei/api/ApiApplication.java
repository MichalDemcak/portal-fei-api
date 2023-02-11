package portal.fei.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import portal.fei.api.tasks.ScheduledTasks;
import portal.fei.api.tasks.SendEmailsTask;

import java.util.Date;
import java.util.Properties;

@SpringBootApplication
public class ApiApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApiApplication.class, args);
		/*SpringApplication.run(ScheduledTasks.class, args);*/
	}

}
