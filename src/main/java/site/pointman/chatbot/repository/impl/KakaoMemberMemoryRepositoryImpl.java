package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.KakaoMemberRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class KakaoMemberMemoryRepositoryImpl implements KakaoMemberRepository {


    private static Map<Long, KakaoMember> store = new HashMap<>();
    private static long sequence = 0L;
    @Override
    public KakaoMember save(KakaoMember user) {
        user.setIdx(++sequence);
        store.put(user.getIdx(), user);
        return user;
    }

    @Override
    public KakaoMemberLocation saveLocation(KakaoMemberLocation userLocation) {
        return null;
    }

    @Override
    public Optional<KakaoMember> findByUserkey(String kakaoUserkey) {
        return store.values().stream()
                .filter(user -> user.getKakaoUserkey().equals(kakaoUserkey))
                .findAny();
    }

    @Override
    public void update(KakaoMemberLocation userLocation) {

    }

    @Override
    public Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey) {
        return Optional.empty();
    }
}
