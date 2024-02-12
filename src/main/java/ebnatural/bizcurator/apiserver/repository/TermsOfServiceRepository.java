package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.TermsOfService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TermsOfServiceRepository extends JpaRepository<TermsOfService, Long> {
}