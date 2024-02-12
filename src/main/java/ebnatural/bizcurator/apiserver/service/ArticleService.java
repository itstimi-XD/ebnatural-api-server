package ebnatural.bizcurator.apiserver.service;

import ebnatural.bizcurator.apiserver.domain.Article;
import ebnatural.bizcurator.apiserver.domain.Member;
import ebnatural.bizcurator.apiserver.domain.constant.BoardType;
import ebnatural.bizcurator.apiserver.dto.ArticleDto;
import ebnatural.bizcurator.apiserver.repository.ArticleRepository;
import ebnatural.bizcurator.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
// TODO : 로그인 파트 완성되면 전체적으로 수정 필요
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public List<ArticleDto> fetchNoticePagesBy(Long lastArticleId, BoardType boardType, Integer size, Boolean firstPage) {

        PageRequest pageRequest = PageRequest.of(0, size);

        if (firstPage) {
            lastArticleId = Long.MAX_VALUE;
            Page<ArticleDto> articlePage = articleRepository.findByIdLessThanAndBoardTypeOrderByIdDescForFirstPage(lastArticleId, boardType, pageRequest);
            return articlePage.getContent();
        }

        Page<ArticleDto> articlePage = articleRepository.findByIdLessThanAndBoardTypeOrderByIdDesc(lastArticleId, boardType, pageRequest);
        return articlePage.getContent();
    }

    public List<ArticleDto> fetchFaqPagesBy(Long lastArticleId, BoardType boardType, Integer size) {

        PageRequest pageRequest = PageRequest.of(0, size);
        Page<ArticleDto> articlePage = articleRepository.findByIdLessThanAndBoardTypeOrderByIdDesc(lastArticleId, boardType, pageRequest);
        return articlePage.getContent();
    }

    public void saveArticle(ArticleDto articleDto) {
        Member member = memberRepository.getReferenceById(articleDto.getMemberDto().getId());
        Article article = articleDto.toEntity(member);
        articleRepository.save(article);
    }

    public void updateArticle(Long articleId, ArticleDto articleDto) {
        Article article = articleRepository.getReferenceById(articleId);

        if (articleDto.getTitle() != null) {
            article.setTitle(articleDto.getTitle());
        }

        if (articleDto.getContent() != null) {
            article.setContent(articleDto.getContent());
        }

        if (articleDto.getIsFixed() != null) {
            article.setIsFixed(articleDto.getIsFixed());
        }
    }

    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
