package portal.fei.api.repositories.interfaces.isp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import portal.fei.api.domain.isp.StudentRequest;

import java.util.Collection;

public interface StudentRequestRepository extends JpaRepository<StudentRequest,Long> {

    @Query("SELECT sr FROM StudentRequest sr WHERE sr.studentId = :studentId")
    Collection<StudentRequest> studentRequests(@Param("studentId") String studentId);

    @Query("SELECT sr FROM StudentRequest sr WHERE sr.studentRequestState.id = :stateId")
    Collection<StudentRequest> findByState(@Param("stateId") Long stateId);

    @Query("SELECT sr FROM StudentRequest sr WHERE sr.studentName LIKE %:name%")
    Collection<StudentRequest> findByName(@Param("name") String name);

    @Query("SELECT sr FROM StudentRequest sr WHERE sr.studentRequestState.id = :stateId and sr.studentName LIKE %:name%")
    Collection<StudentRequest> findByNameState(@Param("stateId") Long stateId, @Param("name") String name);
    @Query("SELECT sr FROM StudentRequest sr WHERE (sr.studentRequestState.id = 1 or sr.studentRequestState.id = 2) and sr.readyForVk = FALSE ")
    Collection<StudentRequest> allNotDone();
}
