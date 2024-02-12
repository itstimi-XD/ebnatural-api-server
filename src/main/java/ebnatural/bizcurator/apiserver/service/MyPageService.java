package ebnatural.bizcurator.apiserver.service;

import ebnatural.bizcurator.apiserver.domain.CancelApplication;
import ebnatural.bizcurator.apiserver.domain.Member;
import ebnatural.bizcurator.apiserver.domain.OrderDetail;
import ebnatural.bizcurator.apiserver.domain.Product;
import ebnatural.bizcurator.apiserver.domain.RefundApplication;
import ebnatural.bizcurator.apiserver.domain.constant.DeliveryState;
import ebnatural.bizcurator.apiserver.domain.constant.OrderCancelType;
import ebnatural.bizcurator.apiserver.domain.constant.OrderRefundType;
import ebnatural.bizcurator.apiserver.domain.constant.ReceiveAddressType;
import ebnatural.bizcurator.apiserver.domain.constant.ReceiveWayType;
import ebnatural.bizcurator.apiserver.dto.ApplicationDetailDto;
import ebnatural.bizcurator.apiserver.dto.ApplicationDto;
import ebnatural.bizcurator.apiserver.dto.MyPageHomeDto;
import ebnatural.bizcurator.apiserver.dto.PaymentDetailDto;
import ebnatural.bizcurator.apiserver.dto.PaymentDetailDto.OrderDetailDto;
import ebnatural.bizcurator.apiserver.dto.PaymentHistoryDto;
import ebnatural.bizcurator.apiserver.dto.PaymentHistoryDto.OrderHistoryDto;
import ebnatural.bizcurator.apiserver.dto.request.CancelOrderRequest;
import ebnatural.bizcurator.apiserver.dto.request.RefundOrderRequest;
import ebnatural.bizcurator.apiserver.repository.CancelApplicationRepository;
import ebnatural.bizcurator.apiserver.repository.MemberRepository;
import ebnatural.bizcurator.apiserver.repository.OrderDetailRepository;
import ebnatural.bizcurator.apiserver.repository.ProductRepository;
import ebnatural.bizcurator.apiserver.repository.RefundApplicationRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final RefundApplicationRepository refundApplicationRepository;
    private final CancelApplicationRepository cancelApplicationRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    private final ProductService productService;

    /**
     * 홈화면 조회
     */
    public MyPageHomeDto showHome(Long memberId) {
        if(!memberRepository.existsById(memberId)){
            throw new EntityNotFoundException();
        }
        MyPageHomeDto myPageHomeDto = new MyPageHomeDto();
        myPageHomeDto.tupleToDto(orderDetailRepository.countByDeliveryState(memberId));
        return myPageHomeDto;
    }

    /**
     * 주문 내역 리스트 조회
     * @param filterMonth
     * @param deliveryStateText
     * @return
     */
    public List<PaymentHistoryDto> getAllPaymentHistory(Integer filterMonth, String deliveryStateText) {

        List<OrderDetail> orderDetailList = null;
        // todo: 시큐리티 완성되면 수정
        Long memberId = 3L; // jwtProvider.getUserIDByToken(accessToken);

        if (filterMonth != null && deliveryStateText != null) {
            LocalDateTime filterDate = LocalDateTime.now().minusDays(filterMonth);
            DeliveryState deliveryState = DeliveryState.valueOf(deliveryStateText.toUpperCase());
            orderDetailList = orderDetailRepository.findAllByMemberIdAndOrderTimeAfterAndDeliveryState(memberId, filterDate, deliveryState);
        } else if (filterMonth != null) {
            LocalDateTime filterDate = LocalDateTime.now().minusDays(filterMonth);
            orderDetailList = orderDetailRepository.findAllByMemberIdAndOrderTimeAfter(memberId, filterDate);
        } else if (deliveryStateText != null) {
            DeliveryState deliveryState = DeliveryState.valueOf(deliveryStateText.toUpperCase());
            orderDetailList = orderDetailRepository.findAllByMemberIdAndDeliveryState(memberId, deliveryState);
        } else {
            orderDetailList = orderDetailRepository.findAllByMemberId(memberId);
        }

        if (orderDetailList.isEmpty()) {
            return null;
        }

        Map<Long, List<OrderDetail>> paymentGroupMap = new HashMap<>();
        for (OrderDetail orderDetail : orderDetailList) {
            Long paymentId = orderDetail.getPaymentId();

            List<OrderDetail> paymentGroup = paymentGroupMap.getOrDefault(paymentId, new ArrayList<>());
            paymentGroup.add(orderDetail);
            paymentGroupMap.put(paymentId, paymentGroup);
        }

        List<PaymentHistoryDto> paymentHistoryDtoList = paymentGroupMap.entrySet().stream()
                .map(entry -> createPaymentHistoryDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return paymentHistoryDtoList;
    }

    /**
     * 결제 내역 map형 정보를 PaymentHistoryDto형태로 만들어주는 함수
     * @param paymentId
     * @param orderDetails
     * @return
     */
    private PaymentHistoryDto createPaymentHistoryDto(Long paymentId, List<OrderDetail> orderDetails) {
        List<OrderHistoryDto> orderHistoryDtoList = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetails) {
            Product product = productRepository.findById(orderDetail.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException());

            OrderHistoryDto orderHistoryResponse = OrderHistoryDto.of(orderDetail.getId(),
                    productService.getProductMainImage(product.getId()).getImgUrl(),
                    product.getRegularPrice(),
                    orderDetail.getDeliveryState().getMeaning(),
                    orderDetail.getOrderTime(),
                    product.getName(),
                    orderDetail.getQuantity(),
                    orderDetail.getCost());

            orderHistoryDtoList.add(orderHistoryResponse);
        }

        PaymentHistoryDto paymentHistoryDto = PaymentHistoryDto.of(paymentId, orderHistoryDtoList);
        return paymentHistoryDto;
    }

    /**
     * 주문내역 상세보기
     * @param paymentId
     * @return
     */
    public PaymentDetailDto getAllPaymentDetails(Long paymentId) {

        List<OrderDetail> orderDetailList = null;
        // todo: 시큐리티 완성되면 수정
        Long memberId = 3L; // jwtProvider.getUserIDByToken(accessToken);

        orderDetailList = orderDetailRepository.findAllByPaymentIdAndMemberId(paymentId, memberId);

        if (orderDetailList.isEmpty()) {
            return null;
        }

        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        int totalCost = 0;

        for (OrderDetail orderDetail : orderDetailList) {
            Product product = productRepository.findById(orderDetail.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException());

            OrderDetailDto orderDetailDto = OrderDetailDto.of(orderDetail.getId(),
                    product.getName(),
                    productService.getProductMainImage(product.getId()).getImgUrl(),
                    orderDetail.getCost(),
                    orderDetail.getQuantity(),
                    orderDetail.getDeliveryState().getMeaning());

            totalCost += orderDetail.getCost();
            orderDetailDtoList.add(orderDetailDto);
        }

        Optional<Member> member = memberRepository.findById(memberId);

        PaymentDetailDto paymentDetailDto = PaymentDetailDto.of(paymentId,
                orderDetailList.get(0).getOrderTime(),
                member.map(Member::getUsername).orElse("name not found"),
                orderDetailList.get(0).getPaymentMethod(),
                totalCost,
                orderDetailList.get(0).getShippingFee(),
                orderDetailList.get(0).getPostalCode(),
                orderDetailList.get(0).getAddress(),
                orderDetailList.get(0).getRequestContent(),
                orderDetailDtoList
        );

        return paymentDetailDto;
    }

    /**
     * 주문한 물건 취소 신청하기
     * @param cancelOrderRequest
     */
    public void cancelOrder(CancelOrderRequest cancelOrderRequest) {
        // request body가 유효한지 검사
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(cancelOrderRequest.getOrderId());
        if(!orderDetail.isPresent()){
            throw new EntityNotFoundException("취소하려고 하는 주문내역이 없습니다.");
        }

        // 주문 상태가 "결제 완료"인 상태만 취소 가능함
        if(DeliveryState.PAID != orderDetail.get().getDeliveryState()) {
            throw new IllegalArgumentException("결제완료인 상태만 취소 가능합니다.");
        }

        // 이미 취소신청한 내역이라면
        if(cancelApplicationRepository.existsByOrderDetailId(cancelOrderRequest.getOrderId())){
            throw new EntityExistsException("이미 취소한 내역입니다.");
        }

        OrderCancelType orderCancelType = OrderCancelType.valueOf(cancelOrderRequest.getOpinion());
        if (orderCancelType == null) {
            throw new IllegalArgumentException();
        }

        // todo: 시큐리티 완성되면 수정
        Long memberId = 3L; // jwtProvider.getUserIDByToken(accessToken);
        Optional<Member> member = memberRepository.findById(memberId);
        if(!member.isPresent()){
            throw new EntityNotFoundException();
        }

        // CancelApplication 객체 생성
        CancelApplication cancelApplication = CancelApplication.of(
                member.get(),
                orderDetail.get(),
                orderCancelType);

        // db에 저장
        cancelApplicationRepository.save(cancelApplication);
    }


    /**
     * 주문한 물건 환불 신청하기
     * @param refundOrderRequest RefundOrderRequest
     */
    public void refundOrder(RefundOrderRequest refundOrderRequest) {
        // request body가 유효한지 검사
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(refundOrderRequest.getOrderId());
        if(!orderDetail.isPresent()){
            throw new EntityNotFoundException("환불하려고 하는 주문내역이 없습니다.");
        }

        // 주문 상태가 "배송 완료"인 상태만 환불 가능함
        if(DeliveryState.DELIVER_DONE != orderDetail.get().getDeliveryState()) {
            throw new IllegalArgumentException("배송 완료인 상태만 환불 가능합니다.");
        }

        // 이미 환불신청한 내역이라면
        if(refundApplicationRepository.existsByOrderDetailId(refundOrderRequest.getOrderId())){
            throw new EntityExistsException("이미 환불 신청한 내역입니다.");
        }

        // todo: 시큐리티 완성되면 수정
        Long memberId = 3L; // jwtProvider.getUserIDByToken(accessToken);
        Optional<Member> member = memberRepository.findById(memberId);
        if(!member.isPresent()){
            throw new EntityNotFoundException();
        }

        // enum타입 검사 - 환불 사유
        OrderRefundType orderRefundType = OrderRefundType.valueOf(refundOrderRequest.getOpinionCategory());
        if (orderRefundType == null) {
            throw new IllegalArgumentException();
        }

        // enum타입 검사 - 제품 발송 방법
        ReceiveWayType receiveWayType = ReceiveWayType.valueOf(refundOrderRequest.getReceiveWayType());
        if (receiveWayType == null) {
            throw new IllegalArgumentException();
        }

        // 직접 발송일 때 -> 더 세팅해줄 데이터 없어서 db에 저장하고 return
        if (ReceiveWayType.SEND_BY_USER == receiveWayType) {
            // RefundApplication 객체 생성
            RefundApplication refundApplication = RefundApplication.of(
                    member.get(),
                    orderDetail.get(),
                    orderRefundType,
                    receiveWayType);

            // db에 저장
            refundApplicationRepository.save(refundApplication);
            return;
        }

        // enum타입 검사 - 수거지 선택
        ReceiveAddressType receiveAddressType = ReceiveAddressType.valueOf(refundOrderRequest.getReceiveAddressType());
        if (receiveAddressType == null) {
            throw new IllegalArgumentException();
        }

        String address = "";
        String postalCode = "";
        // 수거지 선택값(receiveAddressType)이 "배송지 정보와 동일"일 때
        switch(receiveAddressType){
            case UNSELECTED:{
                throw new IllegalArgumentException();
            }
            case SAME_WITH_MEMBER_INFO:{
                // 배송지 정보와 동일하면, 주문 내역에서 가져온다.
                address = orderDetail.get().getAddress();
                postalCode = orderDetail.get().getPostalCode();
            }break;
            case CHANGE_ADDRESS:{
                // 수거지 변경이면, request 값에서 가져온다.
                address = refundOrderRequest.getAddress();
                postalCode = refundOrderRequest.getPostalCode();
            }break;
        }

        // RefundApplication 객체 생성
        RefundApplication refundApplication = RefundApplication.of(
                member.get(),
                orderDetail.get(),
                orderRefundType,
                receiveWayType,
                receiveAddressType,
                address,
                postalCode
                );

        // db에 저장
        refundApplicationRepository.save(refundApplication);
    }


    /**
     * 취소 신청 내역 조회
     * @param filterMonth
     * @return
     */
    public List<ApplicationDto> showCancelApplicationList(Integer filterMonth) {
        // todo: 시큐리티 완성되면 수정
        Long memberId = 3L; // jwtProvider.getUserIDByToken(accessToken);

        List<CancelApplication> cancelHistories = null;
        if (null != filterMonth) {
            LocalDateTime filterDate = LocalDateTime.now().minusDays(filterMonth);
            cancelHistories = cancelApplicationRepository.findAllByMemberIdAndCreatedAtAfter(memberId, filterDate);
        } else{
            cancelHistories = cancelApplicationRepository.findAllByMemberId(memberId);
        }

        if(cancelHistories.isEmpty()){
            return null;
        }

        List<ApplicationDto> applicationDtoList = new ArrayList<>();
        for (CancelApplication cancelApplication : cancelHistories) {
            OrderDetail orderDetail = cancelApplicationRepository.findOrderDetailById(cancelApplication.getId());
            Product product = orderDetailRepository.findProductById(orderDetail.getId());

            ApplicationDto applicationDto = ApplicationDto.of(
                    cancelApplication.getId(),
                    orderDetail.getPaymentId(),
                    productService.getProductMainImage(product.getId()).getImgUrl(),
                    orderDetail.getId(),
                    orderDetail.getOrderTime().toString(),
                    product.getName(),
                    orderDetail.getQuantity(),
                    orderDetail.getCost(),
                    cancelApplication.getState().getMeaning()
            );

            applicationDtoList.add(applicationDto);
        }

        return applicationDtoList;
    }

    /**
     * 환불 신청 내역 조회
     * @param filterMonth
     * @return
     */
    public List<ApplicationDto> showRefundApplicationList(Integer filterMonth) {
        Long memberId = 3L;

        // todo: 시큐리티 완성되면 수정
        //Long memberId = MemberUtil.getMemberId();

        List<RefundApplication> refundHistories = null;
        if (null != filterMonth) {
            LocalDateTime filterDate = LocalDateTime.now().minusDays(filterMonth);
            refundHistories = refundApplicationRepository.findAllByMemberIdAndCreatedAtAfter(memberId, filterDate);
        } else{
            refundHistories = refundApplicationRepository.findAllByMemberId(memberId);
        }

        if(refundHistories.isEmpty()){
            return null;
        }

        List<ApplicationDto> applicationDtoList = new ArrayList<>();
        for (RefundApplication refundApplication : refundHistories) {
            OrderDetail orderDetail = refundApplicationRepository.findOrderDetailById(refundApplication.getId());
            Product product = orderDetailRepository.findProductById(orderDetail.getId());

            ApplicationDto applicationDto = ApplicationDto.of(
                    refundApplication.getId(),
                    orderDetail.getPaymentId(),
                    productService.getProductMainImage(product.getId()).getImgUrl(),
                    orderDetail.getId(),
                    orderDetail.getOrderTime().toString(),
                    product.getName(),
                    orderDetail.getQuantity(),
                    orderDetail.getCost(),
                    refundApplication.getState().getMeaning()
            );

            applicationDtoList.add(applicationDto);
        }

        return applicationDtoList;
    }

    /**
     * 취소 신청 상세내역 조회
     * @param cancelId
     * @return
     */
    public ApplicationDetailDto showCancelApplicationDetail(Long cancelId) {
        Member member = memberRepository.findById(3L)
                .orElseThrow(() -> new EntityNotFoundException());

        // todo: 시큐리티 설정 on시 주석 해제
//        Member member = memberRepository.findById(MemberUtil.getMemberId())
//                .orElseThrow(() -> new EntityNotFoundException());
        Optional<CancelApplication> cancelApplicationOptional= cancelApplicationRepository.findById(cancelId);
        if(!cancelApplicationOptional.isPresent()){
            throw new EntityNotFoundException();
        }

        CancelApplication cancelApplication = cancelApplicationOptional.get();
        OrderDetail orderDetail = cancelApplicationRepository.findOrderDetailById(cancelId);
        Product product = orderDetailRepository.findProductById(orderDetail.getId());
        return ApplicationDetailDto.of(
                product.getName(),
                productService.getProductMainImage(product.getId()).getImgUrl(),
                product.getRegularPrice(),
                orderDetail.getQuantity(),
                orderDetail.getPaymentMethod(),
                orderDetail.getShippingFee(),
                cancelApplication.getApproveTime() != null ? cancelApplication.getApproveTime().toString() : "",
                orderDetail.getCost(),
                member.getUsername(),
                orderDetail.getPostalCode(),
                orderDetail.getAddress(),
                cancelApplication.getOpinionCategory().getMeaning()
        );
    }

    /**
     * 환불 신청 상세내역 조회
     * @param refundId
     * @return
     */
    public ApplicationDetailDto showRefundApplicationDetail(Long refundId) {
        Member member = memberRepository.findById(3L)
                .orElseThrow(() -> new EntityNotFoundException());

        // todo: 시큐리티 설정 on시 주석 해제
//        Member member = memberRepository.findById(MemberUtil.getMemberId())
//                .orElseThrow(() -> new EntityNotFoundException());
        Optional<RefundApplication> refundApplicationOptional = refundApplicationRepository.findById(refundId);
        if(!refundApplicationOptional.isPresent()){
            throw new EntityNotFoundException();
        }
        RefundApplication refundApplication = refundApplicationOptional.get();

        OrderDetail orderDetail = refundApplicationRepository.findOrderDetailById(refundId);
        Product product = orderDetailRepository.findProductById(orderDetail.getId());
        return ApplicationDetailDto.of(
                product.getName(),
                productService.getProductMainImage(product.getId()).getImgUrl(),
                product.getRegularPrice(),
                orderDetail.getQuantity(),
                orderDetail.getPaymentMethod(),
                orderDetail.getShippingFee(),
                refundApplication.getApproveTime() != null ? refundApplication.getApproveTime().toString() : "",
                orderDetail.getCost(),
                member.getUsername(),
                refundApplication.getPostalCode(),
                refundApplication.getAddress(),
                refundApplication.getOpinionCategory().getMeaning()
        );
    }

}
