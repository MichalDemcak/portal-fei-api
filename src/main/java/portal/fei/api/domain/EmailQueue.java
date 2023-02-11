package portal.fei.api.domain;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "email_queue")
public class EmailQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text")
    private String text;

    @Column(name = "mail_from")
    private String from;

    @Column(name = "mail_to")
    private String to;

    @Column(name = "start_sending_at")
    private Timestamp startSendingAt;

    @Column(name = "finish_sending_at")
    private Timestamp finishSendingAt;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Timestamp getStartSendingAt() {
        return startSendingAt;
    }

    public void setStartSendingAt(Timestamp startSendingAt) {
        this.startSendingAt = startSendingAt;
    }

    public Timestamp getFinishSendingAt() {
        return finishSendingAt;
    }

    public void setFinishSendingAt(Timestamp finishSendingAt) {
        this.finishSendingAt = finishSendingAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
