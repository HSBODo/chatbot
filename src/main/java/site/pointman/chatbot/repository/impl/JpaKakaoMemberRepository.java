package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.KakaoMember;
import site.pointman.chatbot.domain.KakaoMemberLocation;
import site.pointman.chatbot.repository.KakaoMemberRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class JpaKakaoMemberRepository implements KakaoMemberRepository {
    private final EntityManager em;

    public JpaKakaoMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public KakaoMember save(KakaoMember user) {
        em.persist(user);
        return user;
    }

    @Override
    public Optional<KakaoMember> findByUserkey(String kakaoUserkey) {
        KakaoMember member = em.find(KakaoMember.class,kakaoUserkey);
        return Optional.ofNullable(member);
    }

    @Override
    public KakaoMemberLocation saveLocation(KakaoMemberLocation userLocation) {
        em.persist(userLocation);
        return userLocation;
    }

    @Override
    public void update(KakaoMemberLocation userLocation) {

    }

    @Override
    public Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey) {
        KakaoMemberLocation member = em.find(KakaoMemberLocation.class,kakaoUserkey);
        return Optional.ofNullable(member);
    }
}
