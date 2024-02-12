package ebnatural.bizcurator.apiserver.domain;

import ebnatural.bizcurator.apiserver.domain.constant.MemberRole;
import ebnatural.bizcurator.apiserver.dto.request.PasswordFindRequest;
import ebnatural.bizcurator.apiserver.dto.request.UpdateMemberRequest;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Member extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Length(max = 320, message = "이메일은 320자리를 넘을 수 없습니다.")
    private String username;
    String password;
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
    @NotBlank
    String representative;
    @NotBlank
    String businessName;
    @NotBlank
    String businessNumber;
    @NotBlank
    String postalCode;
    @NotBlank
    String address;
    @NotBlank
    String businessRegistration;
    @NotBlank
    String manager;
    @NotBlank
    String managerEmail;
    @NotBlank
    String managerPhoneNumber;
    Boolean isEnable = true;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private List<OrderDetail> orderDetail;

    @OrderBy("loginTime DESC")
    @JoinColumn(name = "member_login_logs_id")
    @OneToMany(cascade = CascadeType.ALL)
    private final Set<MemberLoginLog> memberLoginLogs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Cart> carts;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_of_service_agreement_id")
    List<TermsOfServiceAgreement> termsOfServiceAgreement;

    @Column
    LocalDate lastLoginTime = LocalDate.now();

    @Setter
    @Column
    private String refreshToken;

    private Member(String username, String password, MemberRole memberRole, String representative, String businessName, String businessNumber, String postalCode, String address,
                   String businessRegistration, String manager, String managerEmail, String managerPhoneNumber) {
        this.username = username;
        this.password = password;
        this.memberRole = memberRole;
        this.representative = representative;
        this.businessName = businessName;
        this.businessNumber = businessNumber;
        this.postalCode = postalCode;
        this.address = address;
        this.businessRegistration = businessRegistration;
        this.manager = manager;
        this.managerEmail = managerEmail;
        this.managerPhoneNumber = managerPhoneNumber;
    }

    public static Member of(String username,
                            String password,
                            MemberRole memberRole,
                            String representative,
                            String businessName,
                            String businessNumber,
                            String postalCode,
                            String address,
                            String businessRegistration,
                            String manager,
                            String managerEmail,
                            String managerPhoneNumber) {
        return new Member(username,
                password,
                memberRole,
                representative,
                businessName,
                businessNumber,
                postalCode,
                address,
                businessRegistration,
                manager,
                managerEmail,
                managerPhoneNumber
        );
    }

    public Member expire() {
        this.username = "xxxxxx@xxxxxx";
        this.password = "x";
        this.representative = "x";
        this.businessName = "x";
        this.businessNumber = "x";
        this.postalCode = "x";
        this.address = "x";
        this.businessRegistration = "x";
        this.manager = "x";
        this.managerEmail = "x";
        this.managerPhoneNumber = "x";
        this.isEnable = false;
        this.refreshToken = "";
        return this;
    }

    public Member update(UpdateMemberRequest memberDto) {
        this.password = memberDto.getPassword();
        this.businessName = memberDto.getBusinessName();
        this.businessNumber = memberDto.getBusinessNumber();
        this.postalCode = memberDto.getPostalCode();
        this.address = memberDto.getAddress();
        if (!memberDto.getBusinessRegistration().equals(""))
            this.businessRegistration = memberDto.getBusinessRegistration();
        this.manager = memberDto.getManager();
        this.managerEmail = memberDto.getManagerEmail();
        this.managerPhoneNumber = memberDto.getManagerPhoneNumber();
        this.lastLoginTime = null;
        return this;
    }

    public Member setNewPassword(PasswordFindRequest memberDto, BCryptPasswordEncoder passwordEncoder) {
        memberDto.encodePrivacy(passwordEncoder);
        this.password = memberDto.getPassword();

        return this;
    }
}
