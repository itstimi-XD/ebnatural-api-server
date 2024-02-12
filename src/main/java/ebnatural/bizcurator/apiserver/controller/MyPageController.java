package ebnatural.bizcurator.apiserver.controller;

import ebnatural.bizcurator.apiserver.dto.*;
import ebnatural.bizcurator.apiserver.dto.request.CancelOrderRequest;
import ebnatural.bizcurator.apiserver.dto.request.RefundOrderRequest;
import ebnatural.bizcurator.apiserver.dto.request.UpdateMemberRequest;
import ebnatural.bizcurator.apiserver.dto.response.CommonResponse;
import ebnatural.bizcurator.apiserver.service.DocumentService;
import ebnatural.bizcurator.apiserver.service.MemberService;
import ebnatural.bizcurator.apiserver.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Tag(name = "마이페이지", description = "마이페이지 관련 api 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/mypages")
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    private final MemberService memberService;

    private final DocumentService documentService;

    @Operation(summary = "홈화면")
    @GetMapping
    // todo: 시큐리티 설정하면 주석 해제
    //public ResponseEntity<CommonResponse> showHome(@AuthenticationPrincipal MemberPrincipalDetails memberPrincipalDetails) {
    //    MyPageHomeDto myPageHomeDto = myPageService.showHome(memberPrincipalDetails.getId());
    public ResponseEntity<CommonResponse> showHome() {
        MyPageHomeDto myPageHomeDto = myPageService.showHome(3L);
        HashMap<String, Object> historyMap = new HashMap<>();
        historyMap.put("histories", myPageHomeDto);
        return CommonResponse.ok(HttpStatus.OK.value(), "홈 화면 로드 완료됐습니다.", historyMap);
    }

    @Operation(summary = "주문내역 목록 조회")
    @GetMapping("/orders/products")
    public ResponseEntity<CommonResponse> showOrderHistoryList(
            @RequestParam(value = "filter-month", required = false) Integer filterMonth,
            @RequestParam(value = "delivery-state", required = false) String deliveryStateText) {

        List<PaymentHistoryDto> paymentHistoryResponseList = myPageService.getAllPaymentHistory(filterMonth, deliveryStateText);
        HashMap<String, Object> historyMap = new HashMap<>();
        historyMap.put("histories", paymentHistoryResponseList);
        return CommonResponse.ok(HttpStatus.OK.value(), "조회가 완료됐습니다.", historyMap);
    }

    @Operation(summary = "주문내역 상세내역 조회")
    @GetMapping("/orders/products/details")
    public ResponseEntity<CommonResponse> showOrderHistoryDetailByPaymentId(
            @RequestParam(value = "payment-id") Long paymentId){

        PaymentDetailDto paymentDetailDtoList = myPageService.getAllPaymentDetails(paymentId);
        HashMap<String, Object> historyMap = new HashMap<>();
        historyMap.put("details", paymentDetailDtoList);
        return CommonResponse.ok(HttpStatus.OK.value(), "상세 조회가 완료됐습니다.", historyMap);
    }

    @Operation(summary = "주문 취소 신청하기")
    @PostMapping("/orders/cancellations")
    public ResponseEntity<CommonResponse> cancelOrder(@RequestBody CancelOrderRequest cancelOrderRequest) {
        myPageService.cancelOrder(cancelOrderRequest);
        return CommonResponse.ok(HttpStatus.OK.value(), "취소 신청이 완료되었습니다.");
    }

    @Operation(summary = "주문 환불 신청하기")
    @PostMapping("/orders/refunds")
    public ResponseEntity<CommonResponse> refundOrder(@RequestBody RefundOrderRequest refundOrderRequest) {
        myPageService.refundOrder(refundOrderRequest);
        return CommonResponse.ok(HttpStatus.OK.value(), "환불 신청이 완료되었습니다.");
    }

    @Operation(summary = "주문 취소 신청한 내역 조회")
    @GetMapping("/orders/applications/cancellations")
    public ResponseEntity<CommonResponse> showCancelApplicationsList(
                                @RequestParam(value = "filter-month", required = false) Integer filterMonth) {

        List<ApplicationDto> applicationDtoList = myPageService.showCancelApplicationList(filterMonth);
        HashMap<String, Object> historyMap = new HashMap<>();
        historyMap.put("details", applicationDtoList);
        return CommonResponse.ok(HttpStatus.OK.value(), "주문 취소 리스트 조회가 완료되었습니다.", historyMap);
    }

    @Operation(summary = "주문 환불 신청한 내역 조회")
    @GetMapping("/orders/applications/refunds")
    public ResponseEntity<CommonResponse> showRefundApplicationsList(
            @RequestParam(value = "filter-month", required = false) Integer filterMonth) {

        List<ApplicationDto> applicationDtoList = myPageService.showRefundApplicationList(filterMonth);
        HashMap<String, Object> historyMap = new HashMap<>();
        historyMap.put("details", applicationDtoList);
        return CommonResponse.ok(HttpStatus.OK.value(), "주문 환불 리스트 조회가 완료되었습니다.", historyMap);
    }

    @Operation(summary = "주문 취소 신청한 상세내역 조회")
    @GetMapping("/orders/applications/cancellations/details")
    public ResponseEntity<CommonResponse> showCancelApplicationDetail(
            @RequestParam(value = "cancel-id") Long cancelId) {
        ApplicationDetailDto applicationDetailDto = myPageService.showCancelApplicationDetail(cancelId);
        HashMap<String, Object> historyMap = new HashMap<>();
        historyMap.put("details", applicationDetailDto);
        return CommonResponse.ok(HttpStatus.OK.value(), "주문 취소 상세 조회가 완료되었습니다.", historyMap);
    }

    @Operation(summary = "주문 환불 신청한 상세내역 조회")
    @GetMapping("/orders/applications/refunds/details")
    public ResponseEntity<CommonResponse> showRefundApplicationDetail(
            @RequestParam(value = "refund-id") Long refundId) {
        ApplicationDetailDto applicationDetailDto = myPageService.showRefundApplicationDetail(refundId);
        HashMap<String, Object> historyMap = new HashMap<>();
        historyMap.put("details", applicationDetailDto);
        return CommonResponse.ok(HttpStatus.OK.value(), "주문 환불 상세 조회가 완료되었습니다.", historyMap);
    }

    @Operation(summary = "회원 정보 수정")
    @PutMapping("/info")
    public ResponseEntity<CommonResponse> changeMemberInfo(
            @Valid @RequestPart(value = "post", required = true) UpdateMemberRequest memberDto,
            @RequestPart(value = "image") MultipartFile image) {

        memberService.updateMember(memberDto, image);
        return CommonResponse.ok(HttpStatus.CREATED.value(), "update success");
    }

    @Operation(summary = "내 의뢰 내역 조회")
    @GetMapping("/requests/histories")
    public ResponseEntity<CommonResponse> showDocumentList(
            @RequestParam(value = "filter-month", required = false) Integer filterMonth) {

        List<MyPageDocumentDto> documentList = documentService.showMyDocumentList(filterMonth);
        HashMap<String, Object> historyMap = new HashMap<>();
        historyMap.put("details", documentList);
        return CommonResponse.ok(HttpStatus.OK.value(), "내 의뢰 내역 조회가 완료되었습니다.", historyMap);
    }

    @Operation(summary = "의뢰 내역 상세조회")
    @GetMapping("/requests/histories/{request-id}")
    public ResponseEntity<CommonResponse> showDocumentDetails(
            @PathVariable("request-id") Long requestId,
            @RequestParam(value = "type") String documentType)  {

        MyPageDocumentDetailDto detailDto = documentService.showMyDocumentDetail(requestId, documentType);
        HashMap<String, Object> historyMap = new HashMap<>();
        historyMap.put("details", detailDto);
        return CommonResponse.ok(HttpStatus.OK.value(), "내 의뢰 내역 상세조회가 완료되었습니다.", historyMap);
    }

    @Operation(summary = "의뢰 내역 수정")
    @PutMapping("/requests/histories/{request-id}/edit")
    public ResponseEntity<CommonResponse> changeDocument(
            @Valid @RequestPart(value = "post", required = true) DocumentChangeDto documentChangeDto,
            @RequestPart(value = "image") MultipartFile image,
            @PathVariable("request-id") Long requestId,
            @RequestParam(value = "type") String documentType)  {

        documentService.changeDocument(requestId, documentType, documentChangeDto, image);
        return CommonResponse.ok(HttpStatus.OK.value(), "의뢰서 수정 완료했습니다.");
    }
}
