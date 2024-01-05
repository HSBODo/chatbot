package site.pointman.chatbot.domain.product.constatnt;

import java.util.Arrays;

public enum ProductStatus {
    판매중("판매중",""),
    숨김("숨김",""),
    예약("예약",""),
    예약취소("예약취소",""),
    결제완료("결제완료",""),
    판매대기("판매대기","구매중"),
    판매완료("판매완료","구매완료"),
    삭제("삭제","");




    private final String value;
    private final String oppositeValue;

    ProductStatus(String value, String oppositeValue) {
        this.value = value;
        this.oppositeValue = oppositeValue;
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

    public String getOppositeValue() {
        return oppositeValue;
    }
}
