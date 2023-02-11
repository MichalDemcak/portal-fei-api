package portal.fei.api.domain.isp;

import org.springframework.lang.Nullable;
import portal.fei.api.domain.Referent;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "student_requests")
public class StudentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "address")
    private String address;

    @Nullable
    @JoinColumn(name = "referent_id")
    @ManyToOne()
    private Referent referent;
    @Nullable
    @Column(name = "study_programme")
    private String studyProgramme;

    @Column(name = "degree")
    private String degree;

    @Column(name = "year")
    private int year;

    @Column(name = "purpose")
    private String purpose;
    @Nullable
    @Column(name = "psc")
    private String psc;

    //ma byt asi student_request_state_id
    @JoinColumn(name = "student_request_sate_id")
    @OneToOne
    private StudentRequestStates studentRequestState;
    @Nullable
    @Column(name = "returned_to_student_at")
    private Timestamp returnedToStudentAt;
    @Nullable
    @Column(name = "subdean_statement")
    private String subdeanStatement;
    @Nullable
    @Column(name = "dean_statement")
    private String deanStatement;
    @Nullable
    @Column(name = "ready_for_vk")
    private boolean readyForVk;
    @Nullable
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Nullable
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Nullable
    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @OneToMany(mappedBy = "studentRequestId", fetch = FetchType.EAGER)
    private List<IspSubjects> subjectsList;
    @Nullable
    @Column(name = "approved_by_vk")
    private boolean approvedByVk;
    @Nullable
    @Column(name = "approved_by_subdean")
    private boolean approvedBySubdean;
    @Nullable
    @Column(name = "permission_approved")
    private boolean permissionApproved;

    @Nullable
    @Column(name = "attachment_hash")
    private String attachmentHash;

    public Long getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getAddress() {
        return address;
    }

    public String getStudyProgramme() {
        return studyProgramme;
    }

    public String getDegree() {
        return degree;
    }

    public int getYear() {
        return year;
    }

    public StudentRequestStates getStudentRequestState() {
        return studentRequestState;
    }

    public Timestamp getReturnedToStudentAt() {
        return returnedToStudentAt;
    }

    public String getSubdeanStatement() {
        return subdeanStatement;
    }

    public String getDeanStatement() {
        return deanStatement;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStudyProgramme(String studyProgramme) {
        this.studyProgramme = studyProgramme;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setStudentRequestState(StudentRequestStates studentRequestState) {
        this.studentRequestState = studentRequestState;
    }

    public void setReturnedToStudentAt(Timestamp returnedToStudentAt) {
        this.returnedToStudentAt = returnedToStudentAt;
    }

    public void setSubdeanStatement(String subdeanStatement) {
        this.subdeanStatement = subdeanStatement;
    }

    public void setDeanStatement(String deanStatement) {
        this.deanStatement = deanStatement;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<IspSubjects> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(List<IspSubjects> subjectsList) {
        this.subjectsList = subjectsList;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String studentRequestPurpose) {
        this.purpose = studentRequestPurpose;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public boolean isReadyForVk() {
        return readyForVk;
    }

    public void setReadyForVk(boolean readyForVk) {
        this.readyForVk = readyForVk;
    }

    public boolean isApprovedBySubdean() {
        return approvedBySubdean;
    }

    public void setApprovedBySubdean(boolean approvedBySubdean) {
        this.approvedBySubdean = approvedBySubdean;
    }

    public boolean isApprovedByVk() {
        return approvedByVk;
    }

    public void setApprovedByVk(boolean approvedByVk) {
        this.approvedByVk = approvedByVk;
    }

    public boolean isPermissionApproved() {
        return permissionApproved;
    }

    public void setPermissionApproved(boolean permissionApproved) {
        this.permissionApproved = permissionApproved;
    }

    @Nullable
    public String getAttachmentHash() {
        return attachmentHash;
    }

    public void setAttachmentHash(@Nullable String attachmentHash) {
        this.attachmentHash = attachmentHash;
    }

    @Nullable
    public Referent getReferent() {
        return referent;
    }

    public void setReferent(@Nullable Referent referent) {
        this.referent = referent;
    }
}
