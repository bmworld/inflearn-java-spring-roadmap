package hello.springtx.propagation;

import hello.springtx.propagation.domain.log.Log;
import hello.springtx.propagation.domain.log.LogMessageExceptionName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class LogRepository {
  private final EntityManager em;

  @Transactional
  public void save(Log logMessage) {
    log.info("Save Log !");
    em.persist(logMessage);
    if (logMessage.getMessage().contains(LogMessageExceptionName.runtimeEx)) {
      log.info("Log 저장 시, Exception 발생 > Rollback !");
      throw new RuntimeException("--- LogRepository > save (): RuntimeException 발생");
    }

  }

  public Optional<Log> findByMessage(String message) {
    return em.createQuery("select l from Log l where l.message = :message", Log.class)
      .setParameter("message", message)
      .getResultList()
      .stream().findAny() // 2개 이상 나올 경우, 1개만 반환.
      ;
  }
}
