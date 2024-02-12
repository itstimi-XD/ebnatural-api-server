package ebnatural.bizcurator.apiserver.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Getter
@NoArgsConstructor
public class CommonResponse {

    private ResponseStatusType status = ResponseStatusType.OK;
    private Integer code;
    private String message;
    private Map<String, Object> result;

    private CommonResponse(Integer code, String message, Map<String, Object> result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static CommonResponse of(Integer code, String message, Map<String, Object> result) {
        return new CommonResponse(code, message, result);
    }

    public static CommonResponse of(Integer code, String message) {
        return new CommonResponse(code, message, null);
    }

    public static ResponseEntity<CommonResponse> ok(Integer code, String message) {
        return ResponseEntity.ok(CommonResponse.of(code, message));
    }

    public static ResponseEntity<CommonResponse> ok(Integer code, String message, Map<String, Object> result) {
        return ResponseEntity.ok(CommonResponse.of(code, message, result));
    }

    public static ResponseEntity<CommonResponse> created(Integer code, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.of(code, message));
    }
}
