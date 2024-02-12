package ebnatural.bizcurator.apiserver.dto;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@NoArgsConstructor
@Getter
public class AdminDocumentChangeStateDto {

    @Pattern(regexp = "^approve$|^reject$", message = "입력 가능한 값: approve 또는 reject")
    private String type;

    private String rejectReason;

    private AdminDocumentChangeStateDto(String type, String rejectReason) {
        this.type = type;
        this.rejectReason = rejectReason;
    }

    public static AdminDocumentChangeStateDto of(String type, String rejectReason) {
        return new AdminDocumentChangeStateDto(type, rejectReason);
    }
}
