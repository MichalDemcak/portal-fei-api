package portal.fei.api.domain.up;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "subject_requests")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubjectsRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Nullable
    private String name;

    @Nullable
    private Timestamp examDate;

    @Nullable
    private String examGrade;

    @Nullable
    private String examinerName;

    @Nullable
    private String statementOfCatedraHead;

    @Nullable
    private String similiarSubject;

    @Nullable
    @Column(name = "up_request_id")
    private Integer upRequest;

    @Nullable
    private Timestamp createdAt;

    @Nullable
    private Timestamp updateddAt;

    @Nullable
    private Timestamp deletedAt;

    public SubjectsRequest(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getExamDate() {
        return examDate;
    }

    public void setExamDate(Timestamp examDate) {
        this.examDate = examDate;
    }

    public String getExamGrade() {
        return examGrade;
    }

    public void setExamGrade(String examGrade) {
        this.examGrade = examGrade;
    }

    public String getExaminerName() {
        return examinerName;
    }

    public void setExaminerName(String examinerName) {
        this.examinerName = examinerName;
    }

    public String getStatementOfCatedraHead() {
        return statementOfCatedraHead;
    }

    public void setStatementOfCatedraHead(String statementOfCatedraHead) {
        this.statementOfCatedraHead = statementOfCatedraHead;
    }

    public String getSimiliarSubject() {
        return similiarSubject;
    }

    public void setSimiliarSubject(String similiarSubject) {
        this.similiarSubject = similiarSubject;
    }

    public Integer getUpRequest() {
        return upRequest;
    }

    public void setUpRequest(Integer studentRequest) {
        this.upRequest = studentRequest;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdateddAt() {
        return updateddAt;
    }

    public void setUpdateddAt(Timestamp updateddAt) {
        this.updateddAt = updateddAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }
}
