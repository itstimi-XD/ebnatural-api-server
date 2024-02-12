package ebnatural.bizcurator.apiserver.repository.querydsl;

import ebnatural.bizcurator.apiserver.domain.MakeDocument;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MakeDocumentRepositoryCustom {

    Page<MakeDocument> findByAllDocumentCategoryContainingOrderByCreatedAtDesc(String search, Pageable pageable);

    List<MakeDocument> findAllByAfterFilteredDate(Long memberId, LocalDateTime filteredDate);
}
