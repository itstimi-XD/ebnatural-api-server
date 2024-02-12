package ebnatural.bizcurator.apiserver.common.exception.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
