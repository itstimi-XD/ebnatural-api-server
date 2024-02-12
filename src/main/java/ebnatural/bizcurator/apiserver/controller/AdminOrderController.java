package ebnatural.bizcurator.apiserver.controller;

import ebnatural.bizcurator.apiserver.domain.constant.DocumentType;
import ebnatural.bizcurator.apiserver.dto.*;
import ebnatural.bizcurator.apiserver.dto.request.LoginRequest;
import ebnatural.bizcurator.apiserver.dto.response.CommonResponse;
import ebnatural.bizcurator.apiserver.service.AdminOrderService;
import ebnatural.bizcurator.apiserver.service.MemberAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 관리자페이지 
 *      - 홈화면
 *      - 주문 배송 관련
 *      - 취소, 환불 신청 내역 관련
 */
@Tag(name = "관리자페이지", description = "관리자페이지 주문내역 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admins")
public class AdminOrderController {

    private final MemberAuthService memberAuthService;
    private final AdminOrderService adminOrderService;

    @Operation(summary = "홈화면 메서드", description = "홈화면 메서드입니다.")
    @GetMapping("/home")
    public ResponseEntity<CommonResponse> showHome() {
        AdminHomeInfoDto adminHomeInfoDto = adminOrderService.showHomeInformation();
        HashMap<String, Object> historyMap = new HashMap<>();
        historyMap.put("histories", adminHomeInfoDto);
        return CommonResponse.ok(HttpStatus.OK.value(), "관리자 페이지 홈화면 로드 완료했습니다.", historyMap);
    }

    /**
     * 로그인 시 access 토큰, refresh 토큰 모두 새로 만들어준다.
     *
     * @param loginDto
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@Valid @RequestBody LoginRequest loginDto) {
        MemberDto memberDto = memberAuthService.login(loginDto);
        Map<String, Object> loginDataMap = new HashMap<>();
        loginDataMap.put("login", memberDto);
        return CommonResponse.ok(HttpStatus.OK.value(), "로그인 성공했습니다.", loginDataMap);
    }

    @Operation(summary = "주문내역 조회 메서드", description = "주문내역 조회 메서드입니다.")
    @GetMapping("/orders")
    public ResponseEntity<CommonResponse> showOrderDetailList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "search", required = false) String search) {

        // 시간 관계상 프론트에서 페이지네이션을 처리할 시간이 없다고 하여 프론트팀 요청으로 페이지네이션 기능을 주석처리 함
//        Pair<Integer, List<AdminOrderDetailDto>> adminOrderDetailsPair =
//                adminOrderService.showOrderDetailListByPageIndexAndSearchKeyword(page, search);

        Pair<Integer, List<AdminOrderDetailDto>> adminOrderDetailsPair =
                adminOrderService.showOrderDetailListByPageIndexAndSearchKeyword(search);
        // dataTotalCount가 histories 보다 앞에 출력됐으면 해서 순서가 보장되는 LinkedHashMap으로 수정함.
        LinkedHashMap<String, Object> historyMap = new LinkedHashMap<>();
        historyMap.put("dataTotalCount", adminOrderDetailsPair.getFirst());
        historyMap.put("histories", adminOrderDetailsPair.getSecond());
        return CommonResponse.ok(HttpStatus.OK.value(), "관리자 페이지 주문내역 조회 완료했습니다.", historyMap);
    }

    @Operation(summary = "주문 취소신청 내역", description = "조회 메서드")
    @GetMapping("/applications/cancellations")
    public ResponseEntity<CommonResponse> showCancelApplications(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "search", required = false) String search) {

        Pair<Integer, List<AdminApplicationDto>> adminApplicationPair =
                adminOrderService.showOrderCancelListByPageIndexAndSearchKeyword(page, search);
        // dataTotalCount가 histories 보다 앞에 출력됐으면 해서 순서가 보장되는 LinkedHashMap으로 수정함.
        LinkedHashMap<String, Object> historyMap = new LinkedHashMap<>();
        historyMap.put("dataTotalCount", adminApplicationPair.getFirst());
        historyMap.put("histories", adminApplicationPair.getSecond());
        return CommonResponse.ok(HttpStatus.OK.value(), "관리자 페이지 주문 취소신청 조회 완료했습니다.", historyMap);
    }

    @Operation(summary = "주문 환불신청 내역", description = "조회 메서드")
    @GetMapping("/applications/refunds")
    public ResponseEntity<CommonResponse> showRefundApplications(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "search", required = false) String search) {

        Pair<Integer, List<AdminApplicationDto>> adminApplicationPair =
                adminOrderService.showOrderRefundListByPageIndexAndSearchKeyword(page, search);
        // dataTotalCount가 histories 보다 앞에 출력됐으면 해서 순서가 보장되는 LinkedHashMap으로 수정함.
        LinkedHashMap<String, Object> historyMap = new LinkedHashMap<>();
        historyMap.put("dataTotalCount", adminApplicationPair.getFirst());
        historyMap.put("histories", adminApplicationPair.getSecond());
        return CommonResponse.ok(HttpStatus.OK.value(), "관리자 페이지 주문 환불신청 조회 완료했습니다.", historyMap);
    }

    @Operation(summary = "주문 취소 신청서 상태 처리", description = "승인,거절 처리를 합니다.")
    @PutMapping("/applications/cancellations/{application-id}")
    public ResponseEntity<CommonResponse> changeStateCancelApplication(
            @PathVariable("application-id") Long applicationId,
            @Valid @RequestBody ApplicationChangeStateDto applicationChangeStateDto) {

        adminOrderService.changeStateCancelApplication(applicationId, applicationChangeStateDto.getType().equals("approve"));
        return CommonResponse.ok(HttpStatus.OK.value(), "관리자페이지 주문 취소신청서 처리 완료했습니다.");
    }

    @Operation(summary = "주문 환불 신청서 상태 처리", description = "승인,거절 처리를 합니다.")
    @PutMapping("/applications/refunds/{application-id}")
    public ResponseEntity<CommonResponse> changeStateRefundApplication(
            @PathVariable("application-id") Long applicationId,
            @Valid @RequestBody ApplicationChangeStateDto applicationChangeStateDto) {

        adminOrderService.changeStateRefundApplication(applicationId, applicationChangeStateDto.getType().equals("approve"));
        return CommonResponse.ok(HttpStatus.OK.value(), "관리자페이지 주문 환불신청서 처리 완료했습니다.");
    }

    @Operation(summary = "회원관리", description = "회원 리스트를 출력합니다.")
    @GetMapping("/users")
    public ResponseEntity<CommonResponse> showUserList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "search", required = false) String search) {
        Pair<Integer, List<AdminUserInfoDto>> adminUserInfoPair =
                adminOrderService.showUserListByPageIndexAndSearchKeyword(page, search);
        // dataTotalCount가 histories 보다 앞에 출력됐으면 해서 순서가 보장되는 LinkedHashMap으로 수정함.
        LinkedHashMap<String, Object> historyMap = new LinkedHashMap<>();
        historyMap.put("dataTotalCount", adminUserInfoPair.getFirst());
        historyMap.put("histories", adminUserInfoPair.getSecond());
        return CommonResponse.ok(HttpStatus.OK.value(), "관리자페이지 회원 조회 완료했습니다.", historyMap);
    }

    @Operation(summary = "입점판매사 관리", description = "입점판매사 리스트를 출력합니다.")
    @GetMapping("/partners")
    public ResponseEntity<CommonResponse> showCompanyList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "search", required = false) String search) {
        Pair<Integer, List<AdminPartnerDto>> adminPartnerDtoPair =
                adminOrderService.showPartnerListByPageIndexAndSearchKeyword(page, search);

        // dataTotalCount가 histories 보다 앞에 출력됐으면 해서 순서가 보장되는 LinkedHashMap으로 수정함.
        LinkedHashMap<String, Object> historyMap = new LinkedHashMap<>();
        historyMap.put("dataTotalCount", adminPartnerDtoPair.getFirst());
        historyMap.put("histories", adminPartnerDtoPair.getSecond());
        return CommonResponse.ok(HttpStatus.OK.value(), "관리자페이지 입점판매사 조회 완료했습니다.", historyMap);
    }

    @Operation(summary = "판매의뢰 신청서 조회", description = "판매의뢰 신청서 조회")
    @GetMapping("/sell")
    public ResponseEntity<CommonResponse> showSellDocumentList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "search", required = false) String search) {
        Pair<Integer, List<AdminSellDocumentDto>> adminSellDocumentDtoPair =
                adminOrderService.showSellDocumentListByPageIndexAndSearchKeyword(page, search);
        // dataTotalCount가 histories 보다 앞에 출력됐으면 해서 순서가 보장되는 LinkedHashMap으로 수정함.
        LinkedHashMap<String, Object> historyMap = new LinkedHashMap<>();
        historyMap.put("dataTotalCount", adminSellDocumentDtoPair.getFirst());
        historyMap.put("histories", adminSellDocumentDtoPair.getSecond());
        return CommonResponse.ok(HttpStatus.OK.value(), "판매의뢰 신청서 조회 완료했습니다.", historyMap);
    }

    @Operation(summary = "판매의뢰 승인, 거절 처리", description = "approve or reject로 보내주세요.")
    @PatchMapping("/sell/{id}")
    public ResponseEntity<CommonResponse> changeSellDocumentState(
            @PathVariable("id") Long id,
            @Valid @RequestBody AdminDocumentChangeStateDto adminDocumentChangeStateDto) {
        adminOrderService.changeSellDocumentState(id, adminDocumentChangeStateDto.getType().equals("approve"));
        return CommonResponse.ok(HttpStatus.OK.value(), "판매의뢰 처리 완료했습니다.");
    }

    @Operation(summary = "구매의뢰 신청서 조회", description = "구매의뢰 신청서 조회")
    @GetMapping("/purchases")
    public ResponseEntity<CommonResponse> showPurchaseDocumentList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "search", required = false) String search) {
        Pair<Integer, List<AdminPurchaseAndMakeDocumentDto>> adminPurchaseAndMakeDocumentDto =
                adminOrderService.showPurchaseAndMakeDocumentListByPageIndexAndSearchKeyword(DocumentType.purchase, page, search);
        // dataTotalCount가 histories 보다 앞에 출력됐으면 해서 순서가 보장되는 LinkedHashMap으로 수정함.
        LinkedHashMap<String, Object> historyMap = new LinkedHashMap<>();
        historyMap.put("dataTotalCount", adminPurchaseAndMakeDocumentDto.getFirst());
        historyMap.put("histories", adminPurchaseAndMakeDocumentDto.getSecond());
        return CommonResponse.ok(HttpStatus.OK.value(), "구매의뢰 신청서 조회 완료했습니다.", historyMap);
    }

    @Operation(summary = "구매의뢰 승인, 거절 처리", description = "approve or reject로 보내주세요.")
    @PatchMapping("/purchases/{id}")
    public ResponseEntity<CommonResponse> changePurchaseDocumentState(
            @PathVariable("id") Long id,
            @Valid @RequestBody AdminDocumentChangeStateDto adminDocumentChangeStateDto) {
        adminOrderService.changePurchaseOrMakeDocumentState(DocumentType.purchase, id, adminDocumentChangeStateDto.getType().equals("approve"));
        return CommonResponse.ok(HttpStatus.OK.value(), "구매의뢰 수정 처리 완료했습니다.");
    }

    @Operation(summary = "제작의뢰 신청서 조회", description = "제작의뢰 신청서 조회")
    @GetMapping("/make")
    public ResponseEntity<CommonResponse> showMakeDocumentList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "search", required = false) String search) {
        Pair<Integer, List<AdminPurchaseAndMakeDocumentDto>> adminPurchaseAndMakeDocumentDto =
                adminOrderService.showPurchaseAndMakeDocumentListByPageIndexAndSearchKeyword(DocumentType.make, page, search);
        // dataTotalCount가 histories 보다 앞에 출력됐으면 해서 순서가 보장되는 LinkedHashMap으로 수정함.
        LinkedHashMap<String, Object> historyMap = new LinkedHashMap<>();
        historyMap.put("dataTotalCount", adminPurchaseAndMakeDocumentDto.getFirst());
        historyMap.put("histories", adminPurchaseAndMakeDocumentDto.getSecond());
        return CommonResponse.ok(HttpStatus.OK.value(), "제작의뢰 신청서 조회 완료했습니다.", historyMap);
    }

    @Operation(summary = "제작의뢰 승인, 거절 처리", description = "approve or reject로 보내주세요.")
    @PatchMapping("/make/{id}")
    public ResponseEntity<CommonResponse> changeMakeDocumentState(
            @PathVariable("id") Long id,
            @Valid @RequestBody AdminDocumentChangeStateDto adminDocumentChangeStateDto) {
        adminOrderService.changePurchaseOrMakeDocumentState(DocumentType.make, id, adminDocumentChangeStateDto.getType().equals("approve"));
        return CommonResponse.ok(HttpStatus.OK.value(), "제작의뢰 수정 처리 완료했습니다.");
    }
}
