package site.pointman.chatbot.domain.notice.constant;

import java.util.Arrays;

public enum NoticeType {
    BASIC_CARD("basicCard"),
    TEXT_CARD("textCard");

    private final String value;

    NoticeType(String value) {
        this.value = value;
    }

    public static NoticeType getType(String type){
        return Arrays.stream(NoticeType.values())
                .filter(noticeType -> noticeType.getValue().equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("게시글 타입에 %s가 존재하지 않습니다.", type)));
    }

    public String getValue() {
        return value;
    }
}
