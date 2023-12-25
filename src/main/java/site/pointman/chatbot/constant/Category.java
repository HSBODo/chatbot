package site.pointman.chatbot.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Category {
    패션_잡화("패션/잡화"),
    생활가전("생활가전"),
    가구_인테리어("가구/인테리어"),
    뷰티_미용("뷰티/미용"),
    티켓_교환권("티켓/교환권"),
    디지털기기("디지털기기"),
    도서("도서"),
    취미_게임_음반("취미/게임/음반"),
    스포츠_레저("스포츠/레저"),
    기타("기타"),
    ;

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public static Category getCategory(String value){
        return Arrays.stream(Category.values())
                .filter(category -> category.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("상품 카테고리에 %s가 존재하지 않습니다.", value)));
    }

    public String getValue() {
        return value;
    }
}
