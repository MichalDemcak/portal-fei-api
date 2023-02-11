package portal.fei.api.domain.up;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "up_requests")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Nullable
    private String name;

    @Nullable
    private Timestamp birthDate;

    @Nullable
    private String BirthTown;

    @Nullable
    private String birthIdentificationNumber;

    @Nullable
    private String address;

    @Nullable
    private String psc;

    @Nullable
    private int year;

    @Nullable
    private String typeOfStudy;

    @Nullable
    private String kindOfStudy;

    @Nullable
    private String consultantPlace;

    @Nullable
    @OneToMany(mappedBy = "upRequest", fetch = FetchType.EAGER)
    private List<SubjectsRequest> subjectList;

    @Nullable
    private String faculty;

    @Nullable
    private String university;

    @Nullable
    private String deanDecision;

    @Nullable
    private String studyYear;

    @Nullable
    private Timestamp deanDecisionDate;

    @Nullable
    private String referentStatement;

    @Nullable
    private String referentId;

    @Nullable
    private String referentName;

    @Nullable
    private Timestamp createdAt;

    @Nullable
    private Timestamp updatedAt;

    @Nullable
    private Timestamp deletedAt;

    public UpRequest() {

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

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthTown() {
        return BirthTown;
    }

    public void setBirthTown(String birthTown) {
        BirthTown = birthTown;
    }

    public String getBirthIdentificationNumber() {
        return birthIdentificationNumber;
    }

    public void setBirthIdentificationNumber(String birthIdenteficationNumber) {
        this.birthIdentificationNumber = birthIdenteficationNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTypeOfStudy() {
        return typeOfStudy;
    }

    public void setTypeOfStudy(String typeOfstudy) {
        this.typeOfStudy = typeOfstudy;
    }

    public String getKindOfStudy() {
        return kindOfStudy;
    }

    public void setKindOfStudy(String kindOfStudy) {
        this.kindOfStudy = kindOfStudy;
    }

    public String getConsultantPlace() {
        return consultantPlace;
    }

    public void setConsultantPlace(String consultantPlace) {
        this.consultantPlace = consultantPlace;
    }

    public List<SubjectsRequest> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectsRequest> subjectsRequest) {
        this.subjectList = subjectsRequest;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDeanDecision() {
        return deanDecision;
    }

    public void setDeanDecision(String deanDecision) {
        this.deanDecision = deanDecision;
    }

    public String getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(String studyYear) {
        this.studyYear = studyYear;
    }

    public Timestamp getDeanDecisionDate() {
        return deanDecisionDate;
    }

    public void setDeanDecisionDate(Timestamp deanDecisionDate) {
        this.deanDecisionDate = deanDecisionDate;
    }

    public String getReferentStatement() {
        return referentStatement;
    }

    public void setReferentStatement(String referentStatement) {
        this.referentStatement = referentStatement;
    }

    public String getReferentId() {
        return referentId;
    }

    public void setReferentId(String referentId) {
        this.referentId = referentId;
    }

    public String getReferentName() {
        return referentName;
    }

    public void setReferentName(String referentName) {
        this.referentName = referentName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updateddAt) {
        this.updatedAt = updateddAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void addSubjectRequest(SubjectsRequest subjectsRequestTemp){
        subjectList.add(subjectsRequestTemp);
    }
}