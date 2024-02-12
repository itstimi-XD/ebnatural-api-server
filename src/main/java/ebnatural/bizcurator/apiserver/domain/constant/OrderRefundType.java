package ebnatural.bizcurator.apiserver.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderRefundType {
    UNSELECTED("선택안됨"),
    CHANGE_OF_MIND("고객변심"),     // 배송비 O
    UNSATISFIED("서비스 불만족"),     // 배송비 O
    DEFECT("상품불량"),
    SHIPPING_ERROR("오류배송");

    @Getter
    private final String meaning;
}
