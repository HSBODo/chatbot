package site.pointman.chatbot.constant;

public enum BlockId {
    MAIN("메인", "65262b36ddb57b43495c18f8"),

    CUSTOMER_JOIN("회원가입","652635d7b3e7fb0df1fc357c"),
    CUSTOMER_UPDATE_PHONE_NUMBER("연락처변경","6576dfef287a164bd6cd8f62"),
    CUSTOMER_ASK_DELETE("회원탈퇴 재묻기","6576f55b6286e53ab448ee38"),
    CUSTOMER_DELETE("회원탈퇴","6576f5f56286e53ab448ee46"),
    CUSTOMER_GET_PRODUCTS("회원 등록상품 조회","65262bf631101d1cb11060b8"),
    CUSTOMER_GET_PRODUCT_DETAIL("회원 등록상품 상세보기","657aa39383cd6b068ef93501"),
    CUSTOMER_GET_PROFILE("회원 프로필 조회","6576ddd3a7e99f5af9c441ab"),
    MY_PAGE("마이페이지","658137d28738f66395472720"),
    SALES_HISTORY_PAGE("회원 판매내역","65813d5a000f1242126d9cd6"),
    PRODUCT_ADD_INFO("상품정보입력","652a0a9a27e3c4125a33f6eb"),
    PRODUCT_ADD("상품등록","652e659087e33b27c8ba3a4a"),
    PRODUCT_PROFILE_PREVIEW("상품 프로필 미리보기","657d68558a8123272a649cae"),
    PRODUCT_UPDATE_STATUS("회원 등록상품 상태변경","657aba23eca3c21b078678c3"),
    PRODUCT_DELETE("회원 등록상품 삭제","657ad7bfbe9b5a099b5335b6"),
    PRODUCT_GET_CATEGORIES("추천상품 카테고리 선택","657d588e8a8123272a649b03"),
    PRODUCT_GET_PURCHASE("회원 구매내역","6582f378def18a0edd17a189"),
    PRODUCT_GET_PURCHASE_PROFILE("나의 구매상품 프로필 조회","6583c5c6dcd6f94a82e2a865"),
    PRODUCT_GET_CONTRACT("나의 판매대기 상품 조회","658438b378a18b06c3169d27"),
    PRODUCT_GET_CONTRACT_PROFILE("판매대기 상품 프로필 조회","658440c5dcd6f94a82e2b683"),
    PRODUCT_GET_MAIN("메인 상품 조회","658a684e1e9a5e5b2660bffd"),
    ORDER_ADD_TRACKING_NUMBER("판매상품 운송장번호 등록","6583d9c93fa0457f23d7ac03"),
    FIND_PRODUCTS_BY_CATEGORY("카테고리로 상품 조회","657da1bac75579280e900af9"),
    FIND_NOTICES("공지사항 목록","65262c152dd06e457d91c7c2"),
    FIND_NOTICE("게시글 조회","658088bd73cb0d4d1977c880"),
    PURCHASE_SUCCESS_RECONFIRM("상품 구매확정 재확인","65846277aca99e51cc5be11d"),
    PURCHASE_SUCCESS_CONFIRM("상품 구매확정","658463e835bee670bd302d52"),
    SALE_SUCCESS_RECONFIRM("상품 판매확정 재확인","6586749af96d71675b454bd4"),
    SALE_SUCCESS_CONFIRM("상품 판매확정","658674b53fa0457f23d7fcdf"),
    PRODUCT_HOT_DEAL("핫딜 상품 조회","65882697def18a0edd183edf"),
    CUSTOMER_UPDATE_PROFILE_IMAGE("프로필 이미지 변경","6586c039dcd6f94a82e30203")
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
