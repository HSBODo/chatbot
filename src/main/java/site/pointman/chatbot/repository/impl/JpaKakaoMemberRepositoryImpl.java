package site.pointman.chatbot.repository.impl;


import lombok.extern.slf4j.Slf4j;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.KakaoMemberRepository;

import javax.persistence.EntityManager;

import java.math.BigDecimal;

import java.util.Map;
import java.util.Optional;
@Slf4j
public class JpaKakaoMemberRepositoryImpl implements KakaoMemberRepository {
    private final EntityManager em;

    public JpaKakaoMemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public KakaoMember save(KakaoMember member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<KakaoMember> findByMember(String kakaoUserkey) {
        KakaoMember member = em.find(KakaoMember.class,kakaoUserkey);
        return Optional.ofNullable(member);
    }

    @Override
    public KakaoMemberLocation saveLocation(KakaoMemberLocation memberLocation) {
        em.persist(memberLocation);
        return memberLocation;
    }

    @Override
    public void updateLocation(String kakaoUserkey, Map<String, BigDecimal> updateParams) {
        KakaoMemberLocation memberLocation = em.find(KakaoMemberLocation.class,kakaoUserkey);
        log.info("X={},Y={}",updateParams.get("X"),updateParams.get("Y"));
        memberLocation.setX(updateParams.get("X"));
        memberLocation.setY(updateParams.get("Y"));
    }

    @Override
    public Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey) {
        KakaoMemberLocation member = em.find(KakaoMemberLocation.class,kakaoUserkey);
        return Optional.ofNullable(member);
    }
}
