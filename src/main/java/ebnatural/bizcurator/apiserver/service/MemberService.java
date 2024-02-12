package ebnatural.bizcurator.apiserver.service;

import ebnatural.bizcurator.apiserver.common.exception.custom.*;
import ebnatural.bizcurator.apiserver.common.util.MemberUtil;
import ebnatural.bizcurator.apiserver.domain.*;
import ebnatural.bizcurator.apiserver.repository.TermsOfServiceAgreementRepository;
import ebnatural.bizcurator.apiserver.dto.MemberDto;
import ebnatural.bizcurator.apiserver.dto.MemberPrincipalDetails;
import ebnatural.bizcurator.apiserver.dto.request.CertificationNumberRequest;
import ebnatural.bizcurator.apiserver.dto.request.MemberRequest;
import ebnatural.bizcurator.apiserver.dto.request.PasswordFindRequest;
import ebnatural.bizcurator.apiserver.dto.request.UpdateMemberRequest;
import ebnatural.bizcurator.apiserver.dto.response.CommonResponse;
import ebnatural.bizcurator.apiserver.repository.CertificationNumberRepository;
import ebnatural.bizcurator.apiserver.repository.MemberLoginLogRepository;
import ebnatural.bizcurator.apiserver.repository.MemberRepository;
import ebnatural.bizcurator.apiserver.repository.TermsOfServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberLoginLogRepository memberLoginLogRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final S3ImageUploadService s3ImageUploadService;
    private final CertificationNumberRepository certificationNumberRepository;
    private final TermsOfServiceAgreementRepository termsOfServiceAgreementRepository;
    private final TermsOfServiceRepository termsOfServiceRepository;
    @Value("${cloud.aws.s3.business-registration-dir}")
    private String dir;

    /**
     * https://seop00.tistory.com/39
     *
     * @param memberDto
     * @param image
     */
    public void signup(MemberRequest memberDto, MultipartFile image) {

        if (memberDto.checkPassword())
            throw new InvalidUsernamePasswordException(ErrorCode.PASSWORD_WRONG);

        //이미지 로컬 디렉토리에 저장 및 해당 위치 반환
        String storedPath = s3ImageUploadService.uploadImage(dir, image);
        memberDto.setBusinessRegistration(storedPath);

        //아이디 중복 확인
        String username = memberDto.getUsername();
        Optional.ofNullable(memberRepository.findByUsername(username))
                .ifPresent(foundedMember -> {
                    throw new AlreadyRegisteredUserException(ErrorCode.ALREADY_REGISTERED_USER_EXCEPTION);
                });

        memberDto.encodePrivacy(passwordEncoder);
        Member member = memberDto.toEntity();
        memberRepository.save(member);

        //이용 약관 동의 여부 저장
        List<Boolean> termsOfServices = memberDto.getTermsOfService();
        for (long i = 1; i <= termsOfServices.size(); i++){
            TermsOfService termsOfService = termsOfServiceRepository.findById(i).get();
            if (termsOfService.getNeedAgreement() == true && termsOfServices.get((int)i - 1) == false)
                throw new TermsOfServiceAgreementNeedException(ErrorCode.TERMS_OF_SERVICE_AGREEMENT_EXCEPTION);
            TermsOfServiceAgreement termsOfServiceAgreement =
                    TermsOfServiceAgreement.of(member, termsOfService, termsOfServices.get((int)i - 1));
            termsOfServiceAgreementRepository.save(termsOfServiceAgreement);
        }


    }

    public void updateMember(UpdateMemberRequest memberDto, MultipartFile image) {
        if (memberDto.checkPassword())
            throw new InvalidUsernamePasswordException(ErrorCode.PASSWORD_WRONG);

        Long memberId = MemberUtil.getMemberId();
        memberRepository.findById(memberId)
                .map(foundedMember -> {
                    if (!image.isEmpty()) {
                        s3ImageUploadService.deleteFile(foundedMember.getBusinessRegistration());
                        String storedPath = s3ImageUploadService.uploadImage(dir, image);
                        memberDto.setBusinessRegistration(storedPath);
                    }
                    memberDto.encodePrivacy(passwordEncoder);
                    return foundedMember.update(memberDto);
                })
                .orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new MemberPrincipalDetails(
                Optional.ofNullable(memberRepository.findByUsername(username))
                        .orElseThrow(() -> new UsernameNotFoundException("Count not found user" + username))
        );
    }

    public void logSuccessfulLogin(MemberDto memberDto, String userAgent, String ipAddress) {
        Member member = Optional.ofNullable(memberRepository.findByUsername((String) memberDto.getUsername()))
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        memberLoginLogRepository.save(MemberLoginLog.of(member, userAgent, ipAddress));
    }

    public void delete() {
        Long userId = MemberUtil.getMemberId();
        Member member = memberRepository.findByUserId(userId);
        s3ImageUploadService.deleteFile(member.getBusinessRegistration());
        member.expire();
    }

    /**
     * 모든 멤버 정보를 불로오는 함수 (관리자용)
     *
     * @return
     */
    public List<MemberDto> getAllMember() {
        return memberRepository.getAllMember()
                .stream()
                .map(m -> MemberDto.from(m))
                .collect(Collectors.toList());
    }

    public MemberDto getMyInfo() {
        Long userId = MemberUtil.getMemberId();
        MemberDto member = MemberDto.from(memberRepository.findByUserId(userId));
        return member;
    }

    public CommonResponse findEmail(String email) {
        Member member = memberRepository.findByUsername(email);
        if (member == null)
            return CommonResponse.of(410, "해당 이메일은 존재하지 않습니다.");
        else
            return CommonResponse.of(409, "해당 이메일은 사용중입니다.");
    }

    public CommonResponse certificationNumberConfirm(CertificationNumberRequest certificationNumberRequest) {
        CertificationNumber certificationNumber =
                certificationNumberRepository.findByUsername(certificationNumberRequest.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        if (certificationNumber.isExpired()){
            certificationNumberRepository.delete(certificationNumber);
            return CommonResponse.of(HttpStatus.FORBIDDEN.value(), "인증번호가 만료되었습니다. (유효시간 : 1시간)");
        }

        CommonResponse commonResponse;
        if (passwordEncoder.matches(certificationNumber.getCertificationNumber(), certificationNumberRequest.getCertificationNumber()))
            commonResponse = CommonResponse.of(HttpStatus.OK.value(), "인증번호가 일치합니다.");
        else
            commonResponse = CommonResponse.of(HttpStatus.NOT_ACCEPTABLE.value(), "인증번호가 일치하지 않습니다.");

        certificationNumberRepository.delete(certificationNumber);

        return commonResponse;
    }


    public CommonResponse setNewPassword(PasswordFindRequest passwordFindRequest) {
        CertificationNumber certificationNumber =
                certificationNumberRepository.findByUsername(passwordFindRequest.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        if (certificationNumber.isExpired()){
            certificationNumberRepository.delete(certificationNumber);
            return CommonResponse.of(HttpStatus.FORBIDDEN.value(), "인증번호가 만료되었습니다. (유효시간 : 1시간)");
        }

        if (!passwordFindRequest.getPassword().equals(passwordFindRequest.getPasswordConfirm()))
            throw new InvalidUsernamePasswordException(ErrorCode.PASSWORD_WRONG);

        if (!passwordEncoder.matches(passwordFindRequest.getCertificationNumber(), certificationNumber.getCertificationNumber()))
            throw new InvalidUsernamePasswordException(ErrorCode.CERTIFICATION_NUMBER_WRONG);

        memberRepository.findByUsername(passwordFindRequest.getUsername())
                .setNewPassword(passwordFindRequest, passwordEncoder);

        certificationNumberRepository.delete(certificationNumber);

        return CommonResponse.of(HttpStatus.OK.value(), "비밀번호 재설정에 성공했습니다.");
    }
}
