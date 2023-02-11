package portal.fei.api.repositories.interfaces.up;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.fei.api.domain.up.UpRequest;

public interface UpRequestRepository extends JpaRepository<UpRequest,Integer> {
}
