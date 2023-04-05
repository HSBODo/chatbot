package site.pointman.chatbot.repository.impl;

import org.springframework.stereotype.Repository;
import site.pointman.chatbot.domain.KakaoUser;
import site.pointman.chatbot.repository.KakaoUserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class KakaoUserRepositoryImpl implements KakaoUserRepository {


    private static Map<Long,KakaoUser> store = new HashMap<>();
    private static long sequence = 0L;
    @Override
    public KakaoUser save(KakaoUser user) {
        user.setIdx(++sequence);
        store.put(user.getIdx(), user);
        return user;
    }

    @Override
    public Optional<KakaoUser> findByUserkey(String kakaoUserkey) {
        return store.values().stream()
                .filter(user -> user.getKakaoUserkey().equals(kakaoUserkey))
                .findAny();
    }
}
