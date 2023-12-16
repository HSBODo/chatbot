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
    대기("대기"),
    판매완료("판매완료"),
    삭제("삭제");


    private static final int STATUS_INDEX = 0;

    private final String utterance;

    ProductStatus(String utterance) {
        this.utterance = utterance;
    }

    public static ProductStatus getProductStatus(String utterance){
        List<ProductStatus> status = Arrays.stream(ProductStatus.values())
                .filter(productStatus ->
                    productStatus.getUtterance().equals(utterance)
                ).collect(Collectors.toList());

        return status.get(STATUS_INDEX);
    }

    public String getUtterance() {
        return utterance;
    }
}
