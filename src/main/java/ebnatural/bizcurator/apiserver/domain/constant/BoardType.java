package ebnatural.bizcurator.apiserver.domain.constant;

import lombok.Getter;

public enum BoardType {
    NOTICE("공지사항"),
    FAQ("자주 묻는 질문");

    @Getter
    private final String description;

    BoardType(String description) {
        this.description = description;
    }
}
