package site.pointman.chatbot.globalservice.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.member.service.MemberService;
import site.pointman.chatbot.globalservice.ValidationService;
import site.pointman.chatbot.utill.CustomNumberUtils;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final String KAKAO_OPEN_CHAT_URL_REQUIRED = "https://open.kakao.com/o";

    MemberService memberService;

    public ValidationServiceImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean isValidMemberPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("-", "");

        if (!CustomNumberUtils.isNumber(phoneNumber)) return false;
        if (phoneNumber.length() != 11) return false;

        return true;
    }

    @Override
    public boolean isValidMemberName(String name) {
        if (memberService.isDuplicationName(name)) return false;

        return true;
    }

    @Override
    public boolean isValidProductName(String productName) {

        if(productName.length()>30){
            return false;
        }

        return true;
    }

    @Override
    public boolean isValidProductPrice(String productPrice) {

        if (CustomNumberUtils.isNumber(productPrice)) return true;

        return false;
    }

    @Override
    public boolean isValidProductDescription(String productDescription) {

        if (productDescription.length()>400) return false;

        return true;
    }

    @Override
    public boolean isValidKakaoOpenChatUrl(String kakaoOpenChatUrl) {

        if (kakaoOpenChatUrl.contains(KAKAO_OPEN_CHAT_URL_REQUIRED)) return true;

        return false;
    }

    @Override
    public boolean isValidTrackingNumber(String trackingNumber) {

        if (CustomNumberUtils.isNumber(trackingNumber)) return true;

        return false;
    }

    @Override
    public boolean isValidReservationMember(String name) {

        if (memberService.isCustomerByName(name)) return true;

        return false;
    }
}
