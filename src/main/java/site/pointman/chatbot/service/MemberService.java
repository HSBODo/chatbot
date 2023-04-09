package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;

import java.util.Map;

public interface MemberService {
    Map<String,String> join(KakaoMember user);
    Map<String,String> saveLocation(KakaoMemberLocation userLocation);
}
