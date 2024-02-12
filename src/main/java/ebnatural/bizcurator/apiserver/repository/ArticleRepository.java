package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.Article;
import ebnatural.bizcurator.apiserver.repository.querydsl.ArticleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
}
