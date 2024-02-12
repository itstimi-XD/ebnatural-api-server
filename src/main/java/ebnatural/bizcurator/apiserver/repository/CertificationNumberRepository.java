package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.CertificationNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationNumberRepository extends JpaRepository<CertificationNumber, Long> {
    Optional<CertificationNumber> findByUsername(String username);
}
