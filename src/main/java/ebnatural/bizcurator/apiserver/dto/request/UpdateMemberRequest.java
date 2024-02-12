package ebnatural.bizcurator.apiserver.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ebnatural.bizcurator.apiserver.domain.Member;
import ebnatural.bizcurator.apiserver.domain.constant.MemberRole;
import ebnatural.bizcurator.apiserver.dto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateMemberRequest {
    private Long id;
    @Pattern(regexp = "[a-zA-Z1-9!@#$%^&*()]{8,16}",
            message = "비밀번호는 영어, 숫자, 특수문자(!@#$%^&*())를 포함한 8~16자리로 입력해주세요.")
    String password;
    @NotBlank
    String passwordConfirm;
    MemberRole memberRole = MemberRole.ROLE_USER;
    @NotBlank
    String businessName;
    @NotBlank
    String representative;
    @NotBlank
    String businessNumber;
    @NotBlank
    String postalCode;
    @NotBlank
    String address;
    String businessRegistration;
    @NotBlank
    String manager;
    @NotBlank
    String managerEmail;
    @NotBlank
    String managerPhoneNumber;
    public void encodePrivacy(BCryptPasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean checkPassword() {
        return !password.equals(passwordConfirm);
    }
}
