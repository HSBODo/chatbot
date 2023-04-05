package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.KakaoUser;
import site.pointman.chatbot.domain.KakaoUserLocation;

import java.util.Optional;

public interface KakaoUserLocationRepository {
    KakaoUserLocation save (KakaoUserLocation userLocation);
    Optional<KakaoUserLocation> findByXY(String  kakaoUserkey);
    void update (KakaoUserLocation userLocation);
    Optional<KakaoUserLocation> findByUserkey(String  kakaoUserkey);
}
