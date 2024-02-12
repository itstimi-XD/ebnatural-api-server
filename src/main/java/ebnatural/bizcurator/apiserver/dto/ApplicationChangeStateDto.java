package ebnatural.bizcurator.apiserver.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationChangeStateDto {
    @Pattern(regexp = "^approve$|^reject$", message = "입력 가능한 값: approve 또는 reject")
    private String type;

    private ApplicationChangeStateDto(String type) {
        this.type = type;
    }

    public static ApplicationChangeStateDto of(String type) {
        return new ApplicationChangeStateDto(type);
    }
}
