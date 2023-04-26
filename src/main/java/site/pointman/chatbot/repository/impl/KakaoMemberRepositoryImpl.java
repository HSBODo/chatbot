package site.pointman.chatbot.repository.impl;


import lombok.extern.slf4j.Slf4j;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.KakaoMemberRepository;

import javax.persistence.EntityManager;

import java.math.BigDecimal;

import java.util.Optional;
@Slf4j
public class KakaoMemberRepositoryImpl implements KakaoMemberRepository {
    private final EntityManager em;

    public KakaoMemberRepositoryImpl(EntityManager em) {
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
    public Optional<KakaoMemberLocation> updateLocation(String kakaoUserkey, BigDecimal x , BigDecimal y) {
        KakaoMemberLocation memberLocation = em.find(KakaoMemberLocation.class,kakaoUserkey);
        memberLocation.changeX(x);
        memberLocation.changeY(y);
        return Optional.ofNullable(memberLocation);
    }

    @Override
    public Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey) {
        KakaoMemberLocation member = em.find(KakaoMemberLocation.class,kakaoUserkey);
        return Optional.ofNullable(member);
    }
}