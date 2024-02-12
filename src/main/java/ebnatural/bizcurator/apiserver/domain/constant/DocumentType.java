package ebnatural.bizcurator.apiserver.domain.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import ebnatural.bizcurator.apiserver.service.DocumentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DocumentType {
    purchase("purchase"),
    make("make");

    private final String value;
    @JsonCreator
    public static DocumentType from(String value) {
        for (DocumentType status : DocumentType.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
    @JsonValue
    public String getValue() {
        return value;
    }
}
