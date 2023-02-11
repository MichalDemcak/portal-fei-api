package portal.fei.api.repositories.interfaces.isp;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.fei.api.domain.isp.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
