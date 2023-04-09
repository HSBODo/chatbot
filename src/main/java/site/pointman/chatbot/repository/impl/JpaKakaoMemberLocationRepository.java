package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.KakaoMember;
import site.pointman.chatbot.domain.KakaoMemberLocation;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.repository.KakaoUserLocationRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class JpaKakaoMemberLocationRepository implements KakaoUserLocationRepository {
    private final EntityManager em;

    public JpaKakaoMemberLocationRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public KakaoMemberLocation save(KakaoMemberLocation userLocation) {
        em.persist(userLocation);
        return userLocation;
    }

    @Override
    public void update(KakaoMemberLocation userLocation) {

    }

    @Override
    public Optional<KakaoMemberLocation> findByUserkey(String kakaoUserkey) {
        return Optional.empty();
    }

    @Override
    public Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey) {
        return Optional.empty();
    }
}
