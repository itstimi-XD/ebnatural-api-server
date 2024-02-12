package ebnatural.bizcurator.apiserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponse {

    private ResponseStatusType status;
    private Integer code;
    private String message;
    private List<ValidationError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {

        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
