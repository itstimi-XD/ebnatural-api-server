package ebnatural.bizcurator.apiserver.dto.response;

import lombok.Getter;

@Getter
public enum ResponseStatusType {
    OK("OK"),
    ERROR("ERROR");

    private final String status;

    ResponseStatusType(String status) {
        this.status = status;
    }

}
