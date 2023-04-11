package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public interface KakaoMemberRepository {
    KakaoMember save (KakaoMember member);
    KakaoMemberLocation saveLocation (KakaoMemberLocation memberLocation);
    Optional<KakaoMember> findByMember(String  kakaoUserkey);
    void updateLocation (String kakaoUserkey, Map<String, BigDecimal> updateParams);
    Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey);
}
