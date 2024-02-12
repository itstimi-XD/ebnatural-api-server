package ebnatural.bizcurator.apiserver.repository.querydsl;

import ebnatural.bizcurator.apiserver.domain.constant.BoardType;
import ebnatural.bizcurator.apiserver.dto.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ArticleRepositoryCustom {
    Page<ArticleDto> findByIdLessThanAndBoardTypeOrderByIdDesc(Long lastArticleId, BoardType boardType, PageRequest pageRequest);
    Page<ArticleDto> findByIdLessThanAndBoardTypeOrderByIdDescForFirstPage(Long lastArticleId, BoardType boardType, PageRequest pageRequest);
}
