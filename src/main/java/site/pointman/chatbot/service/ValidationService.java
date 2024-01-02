package site.pointman.chatbot.service;

public interface ValidationService {
    boolean isValidMemberPhoneNumber(String phoneNumber);
    boolean isValidMemberName(String name);
    boolean isValidProductName(String productName);
    boolean isValidProductPrice(String productPrice);
    boolean isValidProductDescription(String productDescription);
    boolean isValidKakaoOpenChatUrl(String kakaoOpenChatUrl);
    boolean isValidTrackingNumber(String trackingNumber);

    boolean isValidReservationMember(String name);

}
