package ebnatural.bizcurator.apiserver.dto.request;

import ebnatural.bizcurator.apiserver.domain.constant.BoardType;
import ebnatural.bizcurator.apiserver.dto.ArticleDto;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Getter
public class ArticleModifyRequest {
    @Length(max = 100, message = "제목은 100자리를 넘을 수 없습니다.")
    private String title;
    @Length(max = 1000, message = "내용은 1000자리를 넘을 수 없습니다.")
    private String content;
    @Pattern(regexp = "^true$|^false$", message = "입력 가능한 값: true 또는 false")
    private String isFixed;

    public ArticleModifyRequest() {
    }

    private ArticleModifyRequest(String title, String content, String isFixed) {
        this.title = title;
        this.content = content;
        this.isFixed = isFixed;
    }

    public static ArticleModifyRequest of(String title, String content, String isFixed) {
        return new ArticleModifyRequest(title, content, isFixed);
    }

    public ArticleDto toDto() {
        return ArticleDto.of(title, content, BoardType.NOTICE, Boolean.parseBoolean(isFixed));
    }
}
