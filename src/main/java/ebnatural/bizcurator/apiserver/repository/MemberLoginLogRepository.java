package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.MemberLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLoginLogRepository extends JpaRepository<MemberLoginLog, Long> {
}
