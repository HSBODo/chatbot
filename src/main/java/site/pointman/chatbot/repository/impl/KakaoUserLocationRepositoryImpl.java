package site.pointman.chatbot.repository.impl;

import org.springframework.stereotype.Repository;
import site.pointman.chatbot.domain.KakaoUser;
import site.pointman.chatbot.domain.KakaoUserLocation;
import site.pointman.chatbot.repository.KakaoUserLocationRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Repository
public class KakaoUserLocationRepositoryImpl implements KakaoUserLocationRepository {
    private static Map<Long,KakaoUserLocation> store = new HashMap<>();
    private static long sequence = 0L;
    @Override
    public void update(KakaoUserLocation updateParam) {
        KakaoUserLocation findKakaoUserLocation = store.get(updateParam.getKakaoUserkey());
        findKakaoUserLocation.setX(updateParam.getX());
        findKakaoUserLocation.setY(updateParam.getY());
    }

    @Override
    public KakaoUserLocation save(KakaoUserLocation userLocation) {
        userLocation.setIdx(++sequence);
        store.put(userLocation.getIdx(), userLocation);
        return userLocation;
    }

    @Override
    public Optional<KakaoUserLocation> findByUserkey(String kakaoUserkey) {
        return store.values().stream()
                .filter(user -> user.getKakaoUserkey().equals(kakaoUserkey))
                .findAny();
    }

    @Override
    public Optional<KakaoUserLocation> findByXY(String kakaoUserkey) {
        return store.values().stream()
                .filter(user -> user.getKakaoUserkey().equals(kakaoUserkey))
                .findAny();
    
    }
}
