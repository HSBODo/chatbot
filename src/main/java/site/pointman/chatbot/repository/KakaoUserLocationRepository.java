package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.KakaoMemberLocation;

import java.util.Optional;

public interface KakaoUserLocationRepository {
    KakaoMemberLocation save (KakaoMemberLocation userLocation);
    void update (KakaoMemberLocation userLocation);
    Optional<KakaoMemberLocation> findByUserkey(String  kakaoUserkey);

    Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey);
}
