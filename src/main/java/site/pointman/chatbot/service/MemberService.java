package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.member.MemberDto;

public interface MemberService {

    boolean isKakaoMember(String kakaoUserkey);
    boolean isWithdrawalKakaoMember(String kakaoUserkey);
    void join(MemberDto memberDto);

    void Withdrawal(String kakaoUserkey);
    void saveLocation(KakaoMemberLocation userLocation);

    void saveAttribute(JSONObject buttonParams, String kakaoUserkey);

    void updateAttribute(JSONObject buttonParams, String kakaoUserkey);
}
