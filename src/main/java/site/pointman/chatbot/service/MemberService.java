package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;

import java.util.Map;

public interface MemberService {
    void join(KakaoMember member);
    void saveLocation(KakaoMemberLocation userLocation);
}
