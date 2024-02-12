package ebnatural.bizcurator.apiserver.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 물건 수거지 카테고리
 */
@RequiredArgsConstructor
public enum ReceiveAddressType {
    UNSELECTED("선택안됨"),
    
    SAME_WITH_MEMBER_INFO("배송지 정보와 동일"),
    CHANGE_ADDRESS("수거지 변경");

    @Getter
    private final String meaning;
}
