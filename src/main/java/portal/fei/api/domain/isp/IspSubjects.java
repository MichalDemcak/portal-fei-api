package portal.fei.api.domain.isp;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "isp_subjects")
public class IspSubjects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_request_id")
    private Long studentRequestId;
    @Nullable
    @Column(name = "name")
    private String name;
    @Nullable
    @Column(name = "rozsah")
    private String rozsah;
    @Nullable
    @Column(name = "sposob_ukoncenia")
    private String sposobUkoncenia;
    @Nullable
    @Column(name = "podmienky_uznania")
    private String podmienkyUznania;
    @Nullable
    @Column(name = "evaluation_terms")
    private String evaluationTerms;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teachers teacher;

    @Column(name = "approved")
    private boolean approved;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public IspSubjects(){

    }

    public Long getId() {
        return id;
    }


    public String getRozsah() {
        return rozsah;
    }

    public String getSposobUkoncenia() {
        return sposobUkoncenia;
    }

    public String getPodmienkyUznania() {
        return podmienkyUznania;
    }

    public String getEvaluationTerms() {
        return evaluationTerms;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRozsah(String rozsah) {
        this.rozsah = rozsah;
    }

    public void setSposobUkoncenia(String sposobUkoncenia) {
        this.sposobUkoncenia = sposobUkoncenia;
    }

    public void setPodmienkyUznania(String podmienkyUznania) {
        this.podmienkyUznania = podmienkyUznania;
    }

    public void setEvaluationTerms(String evaluationTerms) {
        this.evaluationTerms = evaluationTerms;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teachers getTeacher() {
        return teacher;
    }

    public void setTeacher(Teachers teacher) {
        this.teacher = teacher;
    }

    public Long getStudentRequestId() {
        return studentRequestId;
    }

    public void setStudentRequestId(Long studentRequestId) {
        this.studentRequestId = studentRequestId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
