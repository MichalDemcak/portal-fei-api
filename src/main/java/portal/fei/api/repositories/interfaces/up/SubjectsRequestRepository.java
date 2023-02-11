package portal.fei.api.repositories.interfaces.up;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.fei.api.domain.up.SubjectsRequest;
import portal.fei.api.domain.up.UpRequest;

import java.util.List;

public interface SubjectsRequestRepository extends JpaRepository<SubjectsRequest,Integer> {
    List<SubjectsRequest> findByUpRequest(Integer id);
}
