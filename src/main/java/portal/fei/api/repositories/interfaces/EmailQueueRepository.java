package portal.fei.api.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import portal.fei.api.domain.EmailQueue;

import java.util.Collection;

public interface EmailQueueRepository extends JpaRepository<EmailQueue, Long> {

    @Query("SELECT eq FROM EmailQueue  eq where eq.startSendingAt IS NULL")
    Collection<EmailQueue> getNotSentEmails();

}
