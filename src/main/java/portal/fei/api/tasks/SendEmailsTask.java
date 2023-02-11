package portal.fei.api.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import portal.fei.api.domain.EmailQueue;
import portal.fei.api.repositories.interfaces.EmailQueueRepository;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Collection;
import java.util.Properties;

@Component
public class SendEmailsTask implements Runnable{
    private final EmailQueueRepository emailQueueRepository;
    public SendEmailsTask(EmailQueueRepository emailQueueRepository){
        this.emailQueueRepository = emailQueueRepository;
    }

    @Override
    public void run() {
        Collection<EmailQueue> emailQueues = this.emailQueueRepository.getNotSentEmails();

        final String username = "b73a50d421219c";
        final String password = "5d1516e91abeae";

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        for (EmailQueue email : emailQueues) {

            try{
                // Vytvorenie inštancie triedy Message pre posielanie e-mailu
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email.getFrom()));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(email.getTo()));
                message.setSubject(email.getSubject());
                message.setText(email.getText());

                // Pridanie prílohy k e-mailu
                /*MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart = new MimeBodyPart();
                String file = "path_of_file.pdf";
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName("file.pdf");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);*/

                // Odoslanie e-mailu
                Transport.send(message);

            } catch (MessagingException e){
                throw new RuntimeException(e);
            }
        }

    }
}
