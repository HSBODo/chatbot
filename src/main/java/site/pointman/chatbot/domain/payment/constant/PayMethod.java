package site.pointman.chatbot.domain.payment.constant;

import java.util.Arrays;

public enum PayMethod {
    카드("CARD"),
    카카오페이머니("MONEY"),
    없음("");

    private final String value;

    PayMethod(String value) {
        this.value = value;
    }

    public static PayMethod getPayMethod(String value){
        return Arrays.stream(PayMethod.values())
                .filter(payMethod -> payMethod.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("결제방식에 %s가 존재하지 않습니다.", value)));
    }

    public String getValue() {
        return value;
    }
}
