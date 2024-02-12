package ebnatural.bizcurator.apiserver.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ebnatural.bizcurator.apiserver.domain.constant.BoardType;
import ebnatural.bizcurator.apiserver.dto.ArticleDto;
import ebnatural.bizcurator.apiserver.dto.QArticleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.util.List;

import static ebnatural.bizcurator.apiserver.domain.QArticle.article;

@Slf4j
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ArticleDto> findByIdLessThanAndBoardTypeOrderByIdDescForFirstPage(Long lastArticleId, BoardType boardType, PageRequest pageRequest) {

        List<ArticleDto> articles = queryFactory
                .select(new QArticleDto(
                        article.id,
                        article.title,
                        article.content,
                        article.isFixed,
                        article.createdAt))
                .from(article)
                .where(idLessThan(lastArticleId), boardTypeEq(boardType))
                .orderBy(article.isFixed.desc(), article.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        int total = articles.size();
        return new PageImpl<>(articles, pageRequest, total);
    }

    @Override
    public Page<ArticleDto> findByIdLessThanAndBoardTypeOrderByIdDesc(Long lastArticleId, BoardType boardType, PageRequest pageRequest) {
        List<ArticleDto> content = queryFactory
                .select(new QArticleDto(
                        article.id,
                        article.title,
                        article.content,
                        article.isFixed,
                        article.createdAt))
                .from(article)
                .where(idLessThan(lastArticleId), boardTypeEq(boardType), article.isFixed.isFalse())
                .orderBy(article.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        int total = queryFactory.selectFrom(article)
                .where(idLessThan(lastArticleId), boardTypeEq(boardType))
                .fetch().size();

        return new PageImpl<>(content, pageRequest, total);
    }

    private BooleanExpression idLessThan(Long lastArticleId) {
        return lastArticleId != null ? article.id.lt(lastArticleId) : null;
    }

    private BooleanExpression boardTypeEq(BoardType boardType) {
        return boardType != null ? article.boardType.eq(boardType) : null;
    }
}
