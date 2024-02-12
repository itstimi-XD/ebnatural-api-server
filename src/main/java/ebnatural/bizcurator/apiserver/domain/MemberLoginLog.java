package ebnatural.bizcurator.apiserver.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "member_id"),
        @Index(columnList = "memberUserName")
})
@Entity
public class MemberLoginLog  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String memberUsername;

    @Column(nullable = false)
    private String userAgent;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime loginTime;

    protected MemberLoginLog() {
    }

    private MemberLoginLog(Member member, String userAgent, String ip) {
        this.member = member;
        this.memberUsername = member.getUsername();
        this.userAgent = userAgent;
        this.ip = ip;
        this.loginTime = LocalDateTime.now();
    }

    public static MemberLoginLog of(Member member, String userAgent, String ip) {
        return new MemberLoginLog(member, userAgent, ip);
    }
}
