package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;

import java.util.Map;
import java.util.Optional;

public interface MemberService {
    void join(KakaoMember member);
    void saveLocation(KakaoMemberLocation userLocation);

    void saveAttribute(JSONObject buttonParams, String kakaoUserkey);

    void updateAttribute(JSONObject buttonParams, String kakaoUserkey);
}
