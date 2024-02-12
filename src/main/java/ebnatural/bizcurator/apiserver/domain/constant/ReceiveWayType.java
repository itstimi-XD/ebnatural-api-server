package ebnatural.bizcurator.apiserver.domain.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReceiveWayType {
    PICK_UP_BY_COMPANY("수거신청"),
    SEND_BY_USER("직접발송");

    private final String meaning;
}
