package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.KakaoMember;
import site.pointman.chatbot.domain.KakaoMemberLocation;

import java.util.Map;

public interface MemberService {
    Map<String,String> join(KakaoMember user);
    Map<String,String> saveLocation(KakaoMemberLocation userLocation);
}
