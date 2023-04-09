package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.KakaoMember;
import site.pointman.chatbot.domain.KakaoMemberLocation;

import java.util.Optional;

public interface KakaoMemberRepository {
    KakaoMember save (KakaoMember user);
    KakaoMemberLocation saveLocation (KakaoMemberLocation userLocation);
    Optional<KakaoMember> findByUserkey(String  kakaoUserkey);
    void update (KakaoMemberLocation userLocation);
    Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey);
}
