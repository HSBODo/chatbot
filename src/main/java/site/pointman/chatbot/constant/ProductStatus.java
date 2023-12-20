package site.pointman.chatbot.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ProductStatus {
    판매중("판매중"),
    숨김("숨김"),
    예약("예약"),
    예약취소("예약취소"),
    결제완료("결제완료"),
    판매대기("판매대기"),
    판매완료("판매완료"),
    삭제("삭제");




    private final String value;

    ProductStatus(String value) {
        this.value = value;
    }

    public static ProductStatus getProductStatus(String value){
        return Arrays.stream(ProductStatus.values())
                .filter(productStatus -> productStatus.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("상품 상태에 %s가 존재하지 않습니다.", value)));
    }

    public String getValue() {
        return value;
    }
}
