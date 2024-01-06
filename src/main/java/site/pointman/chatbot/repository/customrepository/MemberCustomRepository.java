package site.pointman.chatbot.repository.customrepository;

import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.member.dto.MemberSearchParamDto;

import java.util.List;
import java.util.Optional;


public interface MemberCustomRepository {
    Optional<Member> findMember(MemberSearchParamDto memberSearchParamDto);
}
