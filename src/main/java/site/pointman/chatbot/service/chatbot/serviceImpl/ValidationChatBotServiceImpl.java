package site.pointman.chatbot.service.chatbot.serviceImpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotValidationResponse;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.service.chatbot.ValidationChatBotService;
import site.pointman.chatbot.utill.CustomNumberUtils;

import java.util.Optional;

@Service
public class ValidationChatBotServiceImpl implements ValidationChatBotService {
    private final String KAKAO_OPEN_CHAT_URL_REQUIRED = "https://open.kakao.com/o";

    MemberRepository memberRepository;

    public ValidationChatBotServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public ChatBotValidationResponse validationCustomerPhoneNumber(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String inputPhone = chatBotRequest.getValidationData();
        inputPhone = inputPhone.replaceAll("-", "");

        if(!CustomNumberUtils.isNumber(inputPhone)){
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }

        if (inputPhone.length() != 11) {
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationSuccess(inputPhone);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationCustomerName(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String inputName = chatBotRequest.getValidationData();

        Optional<Member> mayBeMember = memberRepository.findByName(inputName);

        if(!mayBeMember.isEmpty()){
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationSuccess(inputName);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductName(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String productName = chatBotRequest.getValidationData();
        if(productName.length()>30){
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }
        chatBotValidationResponse.validationSuccess(productName);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductPrice(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String productPrice= chatBotRequest.getValidationData();

        if(!CustomNumberUtils.isNumber(productPrice)){
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationSuccess(productPrice);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductDescription(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String productDescription= chatBotRequest.getValidationData();

        if(productDescription.length()>400){
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationSuccess(productDescription);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductKakaoOpenChatUrl(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String kakaoOpenChayUrl= chatBotRequest.getValidationData();

        if(kakaoOpenChayUrl.contains(KAKAO_OPEN_CHAT_URL_REQUIRED)){
            chatBotValidationResponse.validationSuccess(kakaoOpenChayUrl);
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationTradingLocation(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String tradingLocation= chatBotRequest.getValidationData();
        chatBotValidationResponse.validationSuccess(tradingLocation);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationReservationCustomer(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String reservationCustomerName= chatBotRequest.getValidationData();

        Optional<Member> mayBeMember = memberRepository.findByName(reservationCustomerName);
        if(mayBeMember.isEmpty()){
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationSuccess(reservationCustomerName);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationTrackingNumber(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String trackingNumber = chatBotRequest.getValidationData();

        if(CustomNumberUtils.isNumber(trackingNumber)){
            chatBotValidationResponse.validationSuccess(trackingNumber);
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }
}
