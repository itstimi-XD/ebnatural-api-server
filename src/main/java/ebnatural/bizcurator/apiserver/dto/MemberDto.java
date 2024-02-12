package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ebnatural.bizcurator.apiserver.domain.Member;
import ebnatural.bizcurator.apiserver.domain.constant.MemberRole;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberDto {
    private Long id;
    private String username;
    String password;
    MemberRole memberRole = MemberRole.ROLE_USER;
    String businessName;
    String representative;
    String businessNumber;
    String postalCode;
    String address;
    String businessRegistration;
    String manager;
    String managerEmail;
    String managerPhoneNumber;
    private TokenDto tokenDto;
    Boolean termsOfService;
    public static MemberDto from(Member member, TokenDto tokenDto) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(member.getId());
        memberDto.setUsername(member.getUsername());
        memberDto.setMemberRole(member.getMemberRole());
        memberDto.setTokenDto(tokenDto);
        return memberDto;
    }
    public static MemberDto from(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(member.getId());
        memberDto.setUsername(member.getUsername());
        memberDto.setMemberRole(member.getMemberRole());
        memberDto.setBusinessName(member.getBusinessName());
        memberDto.setRepresentative(member.getRepresentative());
        memberDto.setBusinessNumber(member.getBusinessNumber());
        memberDto.setBusinessRegistration(member.getBusinessRegistration());
        memberDto.setAddress(member.getAddress());
        memberDto.setManager(member.getManager());
        memberDto.setManagerEmail(member.getManagerEmail());
        memberDto.setManagerPhoneNumber(member.getManagerPhoneNumber());
        return memberDto;
    }

    public Member toEntity () {
        return Member.of(username, password, memberRole, representative, businessName, businessNumber, postalCode, address,
                businessRegistration, manager, managerEmail, managerPhoneNumber);
    }
    public void encodePrivacy(BCryptPasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    private MemberDto(Long id) {
        this.id = id;
    }

    public static MemberDto of(Long id) {
        return new MemberDto(id);
    }
}
