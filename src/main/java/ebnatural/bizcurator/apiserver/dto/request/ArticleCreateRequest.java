package ebnatural.bizcurator.apiserver.dto.request;

import ebnatural.bizcurator.apiserver.domain.constant.BoardType;
import ebnatural.bizcurator.apiserver.dto.ArticleDto;
import ebnatural.bizcurator.apiserver.dto.MemberDto;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class ArticleCreateRequest {

    @Length(max = 100, message = "제목은 100자리를 넘을 수 없습니다.")
    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String title;
    @Length(max = 1000, message = "내용은 1000자리를 넘을 수 없습니다.")
    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;
    @NotBlank
    @Pattern(regexp = "^true$|^false$", message = "입력 가능한 값: true 또는 false")
    private String isFixed;

    public ArticleCreateRequest() {
    }

    private ArticleCreateRequest(String title, String content, String isFixed) {
        this.title = title;
        this.content = content;
        this.isFixed = isFixed;
    }

    public static ArticleCreateRequest of(String title, String content, String isFixed) {
        return new ArticleCreateRequest(title, content, isFixed);
    }

    public ArticleDto toDto(MemberDto memberDto) {
        return ArticleDto.of(memberDto, title, content, BoardType.NOTICE, Boolean.parseBoolean(isFixed));
    }
}
