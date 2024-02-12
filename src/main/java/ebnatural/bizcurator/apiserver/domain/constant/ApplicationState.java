package ebnatural.bizcurator.apiserver.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 주문 취소, 환불 신청서 처리 상태
 */
@RequiredArgsConstructor
public enum ApplicationState {
    WAIT("신청접수"),
    APPROVE("승인"),
    REJECTED("거부"),
    FINISHED("처리완료");

    @Getter
    private final String meaning;
}
