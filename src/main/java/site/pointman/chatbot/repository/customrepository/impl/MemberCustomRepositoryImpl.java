package site.pointman.chatbot.repository.customrepository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.QMember;
import site.pointman.chatbot.domain.member.dto.MemberSearchParamDto;
import site.pointman.chatbot.repository.customrepository.MemberCustomRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public MemberCustomRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Member> findMember(MemberSearchParamDto memberSearchParamDto) {
        QMember member = QMember.member;

        return query
                .select(member)
                .from(member)
                .where(
                        isName(memberSearchParamDto.getName()),
                        isUserKey(memberSearchParamDto.getUserKey()),
                        isUseDefault()
                )
                .fetch().stream().findAny();
    }

    private BooleanExpression isUseDefault() {
        return QMember.member.isUse.eq(true);
    }

    private BooleanExpression isName(String name) {
        if (!StringUtils.hasText(name)){
            return null;
        }
        return QMember.member.name.eq(name);
    }

    private BooleanExpression isUserKey(String userKey) {
        if (!StringUtils.hasText(userKey)){
            return null;
        }
        return QMember.member.userKey.eq(userKey);
    }
}
