package site.pointman.chatbot.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import site.pointman.chatbot.domain.KakaoMemberLocation;
import site.pointman.chatbot.repository.KakaoUserLocationRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Slf4j
public class KakaoUserLocationRepositoryImpl implements KakaoUserLocationRepository {
    private static Map<String, KakaoMemberLocation> store = new HashMap<>();
    @Override
    public void update(KakaoMemberLocation updateParam) {
//        KakaoUserLocation findKakaoUserLocation = store.get(updateParam.getKakaoUserkey());
//        findKakaoUserLocation.setX(updateParam.getX());
//        findKakaoUserLocation.setY(updateParam.getY());
    }

    @Override
    public KakaoMemberLocation save(KakaoMemberLocation userLocation) {
        store.put(userLocation.getKakaoUserkey(), userLocation);
        log.info("store=={}",store);
        return userLocation;
    }

    @Override
    public Optional<KakaoMemberLocation> findByUserkey(String kakaoUserkey) {
        return store.values().stream()
                .filter(user -> user.getKakaoUserkey().equals(kakaoUserkey))
                .findAny();
    }

    @Override
    public Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey) {
        return store.values().stream()
                .filter(user -> user.getKakaoUserkey().equals(kakaoUserkey))
                .findAny();

    }
}
