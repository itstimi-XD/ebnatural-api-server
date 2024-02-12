package ebnatural.bizcurator.apiserver.domain;

import ebnatural.bizcurator.apiserver.domain.constant.BoardType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article extends WriterTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ToString.Exclude
    @NotNull(message = "회원은 필수 입력값입니다.")
    @JoinColumn(name = "memberId")
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Member member;

    @Setter
    @Length(max = 100, message = "제목은 100자리를 넘을 수 없습니다.")
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Column(nullable = false, length = 100)
    private String title;

    @Setter
    @Length(max = 1000, message = "내용은 1000자리를 넘을 수 없습니다.")
    @NotBlank(message = "내용은 필수 입력값입니다.")
    @Column(nullable = false, length = 1000)
    private String content;

    @Setter
    @NotNull(message = "게시판 종류는 필수 입력값입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @Setter
    @NotNull(message = "고정 여부는 필수 입력값입니다.")
    @Column(nullable = false)
    private Boolean isFixed;

    private Article(Member member, String title, String content, BoardType boardType, Boolean isFixed) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.isFixed = isFixed;
    }

    public static Article of(Member member, String title, String content, BoardType boardType, Boolean isFixed) {
        return new Article(member, title, content, boardType, isFixed);
    }

    public static Article of(Member member, String title, String content, BoardType boardType) {
        return new Article(member, title, content, boardType, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        Article that = (Article) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}