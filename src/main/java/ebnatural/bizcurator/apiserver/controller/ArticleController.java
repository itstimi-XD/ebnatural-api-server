package ebnatural.bizcurator.apiserver.controller;

import ebnatural.bizcurator.apiserver.domain.constant.BoardType;
import ebnatural.bizcurator.apiserver.dto.ArticleDto;
import ebnatural.bizcurator.apiserver.dto.MemberDto;
import ebnatural.bizcurator.apiserver.dto.request.ArticleCreateRequest;
import ebnatural.bizcurator.apiserver.dto.request.ArticleModifyRequest;
import ebnatural.bizcurator.apiserver.dto.response.CommonResponse;
import ebnatural.bizcurator.apiserver.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/api/notices")
    public ResponseEntity<CommonResponse> getNoticesLowerThanId(@RequestParam(value = "lastArticleId", required = false) Long lastArticleId, @RequestParam("size") Integer size, @RequestParam("firstPage") Boolean firstPage) {
        List<ArticleDto> notices = articleService.fetchNoticePagesBy(lastArticleId, BoardType.NOTICE, size, firstPage);
        HashMap<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("notices", notices);
        return CommonResponse.ok(HttpStatus.OK.value(), "공지사항 조회가 완료되었습니다.", noticeMap);
    }

    @GetMapping("/api/faqs")
    public ResponseEntity<CommonResponse> getFaqsLowerThanId(@RequestParam("lastArticleId") Long lastArticleId, @RequestParam("size") Integer size) {
        List<ArticleDto> faqs = articleService.fetchFaqPagesBy(lastArticleId, BoardType.FAQ, size);
        HashMap<String, Object> faqsMap = new HashMap<>();
        faqsMap.put("faqs", faqs);
        return CommonResponse.ok(HttpStatus.OK.value(), "자주 묻는 질문 조회가 완료되었습니다.", faqsMap);
    }

    // TODO: 인증기능 재개 전까지 임시로 1번 회원으로 고정, 후에 삭제 필요
    @PostMapping("/api/notices")
    public ResponseEntity<CommonResponse> postNewNotice(@Valid @RequestBody ArticleCreateRequest articleCreateRequest) {
        articleService.saveArticle(articleCreateRequest.toDto(MemberDto.of(1L)));
        return CommonResponse.created(HttpStatus.CREATED.value(), "공지사항 등록이 완료되었습니다.");
    }

//    @PostMapping("/api/notices")
//    public ResponseEntity<CommonResponse> postNewNotice(@AuthenticationPrincipal MemberPrincipalDetails memberPrincipalDetails, @Valid @RequestBody ArticleCreateRequest articleCreateRequest) {
//        articleService.saveArticle(articleCreateRequest.toDto(MemberDto.of(memberPrincipalDetails.getId())));
//        return CommonResponse.created(HttpStatus.CREATED.value(), "공지사항 등록이 완료되었습니다.");
//    }

    @PutMapping("/api/notices/{articleId}")
    public ResponseEntity<CommonResponse> updateNotice(@PathVariable("articleId") Long articleId, @Valid @RequestBody ArticleModifyRequest articleModifyRequest) {
        articleService.updateArticle(articleId, articleModifyRequest.toDto());
        return CommonResponse.ok(HttpStatus.OK.value(), "공지사항 수정이 완료되었습니다.");
    }

    @DeleteMapping("/api/notices/{articleId}")
    public ResponseEntity<CommonResponse> deleteNotice(@PathVariable("articleId") Long articleId) {
        articleService.deleteArticle(articleId);
        return CommonResponse.ok(HttpStatus.OK.value(), "공지사항 삭제가 완료되었습니다.");
    }
}
