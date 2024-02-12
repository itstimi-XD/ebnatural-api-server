package ebnatural.bizcurator.apiserver.controller;

import com.amazonaws.Response;
import ebnatural.bizcurator.apiserver.common.exception.custom.AlreadyRegisteredUserException;
import ebnatural.bizcurator.apiserver.common.exception.custom.ErrorCode;
import ebnatural.bizcurator.apiserver.dto.MemberDto;
import ebnatural.bizcurator.apiserver.dto.TokenDto;
import ebnatural.bizcurator.apiserver.dto.request.*;
import ebnatural.bizcurator.apiserver.dto.response.CommonResponse;
import ebnatural.bizcurator.apiserver.service.EmailServiceImpl;
import ebnatural.bizcurator.apiserver.service.MemberAuthService;
import ebnatural.bizcurator.apiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class MemberController {
    private final MemberService memberService;
    private final MemberAuthService memberAuthService;

    private final EmailServiceImpl emailService;

    /**
     * 로그인 시 access 토큰, refresh 토큰 모두 새로 만들어준다.
     *
     * @param loginDto
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@Valid @RequestBody LoginRequest loginDto) throws Exception {
        MemberDto member = memberAuthService.login(loginDto);
        Map<String, Object> mp = new HashMap<>();
        mp.put("login", member);
        return CommonResponse.ok(HttpStatus.OK.value(), "login Success", mp);
    }

    /**
     * accessToken에 담긴 유저정보를 꺼내서 refresh token을 지워준다.
     *
     * @return
     */
    @GetMapping("/logout")
    public ResponseEntity<CommonResponse> logout() {
        return CommonResponse.ok(HttpStatus.OK.value(), "logout success",
                Map.of("result", (Object) (memberAuthService.logout())));
    }

    /**
     * access토큰 내부의 유저 정보를 확인한 후 access 토큰 새로 만들어준다.
     *
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("/refresh")
    public ResponseEntity<CommonResponse> refresh() {
        Map<String, Object> mp = new HashMap<>();
        TokenDto tokenDto = memberAuthService.refreshToken();
        mp.put("result", tokenDto);
        return CommonResponse.ok(HttpStatus.OK.value(), "refresh success", mp);
    }

    /**
     * 전체 회원 정보 조회(관리자만 가능)
     * todo
     * 사업자 등록증 확인을 원할 시 따로 처리해야됨
     * https://www.sunny-son.space/spring/Springboot%EB%A1%9C%20S3%20%ED%8C%8C%EC%9D%BC%20%EC%97%85%EB%A1%9C%EB%93%9C/
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<CommonResponse> getAllMember() {
        return CommonResponse.ok(HttpStatus.OK.value(), "get all member info success",
                Map.of("result", (memberService.getAllMember())));
    }

    /**
     * 회원정보 수정 페이지에 기본값을 채워주기 위한
     * 로그인한 사용자의 개인 정보 조회
     *
     * @return
     */
    @GetMapping("/check")
    public ResponseEntity<CommonResponse> getMyInfo() {
        return CommonResponse.ok(HttpStatus.OK.value(), "getInfo success",
                Map.of("info", (memberService.getMyInfo())));
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@Valid @RequestPart(value = "post", required = true) MemberRequest memberDto,
                                                 @RequestPart(value = "image", required = true) MultipartFile image) {
        memberService.signup(memberDto, image);
        return CommonResponse.ok(HttpStatus.CREATED.value(), "signup success");
    }

    @PatchMapping
    public ResponseEntity<CommonResponse> updateMember(@Valid @RequestPart(value = "post", required = true) UpdateMemberRequest memberDto,
                                                       @RequestPart(value = "image") MultipartFile image) {
        memberService.updateMember(memberDto, image);
        return CommonResponse.ok(HttpStatus.CREATED.value(), "update success");
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse> deleteMember() {
        memberService.delete();

        return CommonResponse.ok(HttpStatus.OK.value(), "deleteMember success");
    }

    /**
     * 이메일 중복 확인 API
     *
     * @param emailMap
     * @return
     * @throws Exception result:
     *                   code: 409(이미 사용중인 이메일), 410(사용 가능한 이메일)
     */
    @PostMapping("/findEmail")
    public ResponseEntity<CommonResponse> findEmail(@RequestBody Map<String, String> emailMap) {
        CommonResponse commonResponse = memberService.findEmail(emailMap.get("email"));
        return CommonResponse.ok(commonResponse.getCode(), commonResponse.getMessage());
    }

    @PostMapping("/emailConfirm")
    public ResponseEntity<CommonResponse> emailConfirm(@RequestBody Map<String, String> emailMap) throws Exception {
        CommonResponse commonResponse = memberService.findEmail(emailMap.get(("email")));
        if (commonResponse.getCode() == 409)
            throw new AlreadyRegisteredUserException(ErrorCode.ALREADY_REGISTERED_USER_EXCEPTION);
        String confirm = emailService.sendCertificationNumberMessage(emailMap.get("email"));
        return CommonResponse.ok(HttpStatus.OK.value(),"이메일로 인증번호가 전송되었습니다." ,Map.of("certification_number", confirm));
    }

    @PostMapping("/certificationNumberConfirm")
    public ResponseEntity<CommonResponse> certificationNumberConfirm(@Valid @RequestBody CertificationNumberRequest certificationNumberRequest) throws Exception {
        CommonResponse commonResponse = memberService.certificationNumberConfirm(certificationNumberRequest);
        return CommonResponse.ok(commonResponse.getCode(), commonResponse.getMessage());
    }

    @PostMapping("/findPassword")
    public ResponseEntity<CommonResponse> findPassword(@RequestBody Map<String, String> emailMap) throws Exception {
        CommonResponse commonResponse = memberService.findEmail(emailMap.get(("email")));
        if (commonResponse.getCode() == 410)
            throw new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage());
        Map<String, Object> confirm = emailService.sendSetNewPwdMessage(emailMap.get("email"));
        return CommonResponse.ok(HttpStatus.OK.value(),"비밀번호 재설정 링크가 전송되었습니다." ,Map.of("certification_number" ,confirm));
    }

    @PostMapping("/setNewPwd")
    public ResponseEntity<CommonResponse> setNewPassword(@Valid @RequestBody PasswordFindRequest passwordFindRequest){
        CommonResponse commonResponse = memberService.setNewPassword(passwordFindRequest);
        return CommonResponse.ok(commonResponse.getCode(), commonResponse.getMessage());
    }
}
