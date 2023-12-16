package site.pointman.chatbot.constant;

public enum BlockId {
    MAIN("메인", "65262b36ddb57b43495c18f8"),

    CUSTOMER_JOIN("회원가입","652635d7b3e7fb0df1fc357c"),
    CUSTOMER_UPDATE_PHONE_NUMBER("연락처변경","6576dfef287a164bd6cd8f62"),
    CUSTOMER_ASK_DELETE("회원탈퇴 재묻기","6576f55b6286e53ab448ee38"),
    CUSTOMER_DELETE("회원탈퇴","6576f5f56286e53ab448ee46"),
    CUSTOMER_GET_PRODUCTS("회원 등록상품 조회","65262bf631101d1cb11060b8"),
    CUSTOMER_GET_PRODUCT_DETAIL("회원 등록상품 상세보기","657aa39383cd6b068ef93501"),

    PRODUCT_ADD_INFO("상품정보입력","652a0a9a27e3c4125a33f6eb"),
    PRODUCT_ADD("상품등록","652e659087e33b27c8ba3a4a"),
    PRODUCT_PROFILE_PREVIEW("상품 프로필 미리보기","657d68558a8123272a649cae"),
    PRODUCT_UPDATE_STATUS("회원 등록상품 상태변경","657aba23eca3c21b078678c3"),
    PRODUCT_DELETE("회원 등록상품 삭제","657ad7bfbe9b5a099b5335b6")
    ;
    private final String name;
    private final String blockId;

    BlockId(String name, String blockId) {
        this.name = name;
        this.blockId = blockId;
    }

    public String getName() {
        return name;
    }

    public String getBlockId() {
        return blockId;
    }
}
