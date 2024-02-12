package ebnatural.bizcurator.apiserver.domain.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RequestStateType {
    WAIT("대기"),
    APPROVE("승인"),
    REJECT("반려");

    private final String status;

    public String getStatus() {
        return status;
    }
}
