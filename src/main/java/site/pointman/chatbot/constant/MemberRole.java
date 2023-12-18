package site.pointman.chatbot.constant;

import java.util.Arrays;

public enum MemberRole {
    CUSTOMER_BRONZE("브론즈",1000L),
    CUSTOMER_SILVER("실버",10000L),
    CUSTOMER_PLATINUM("플래티넘",100000L),
    CUSTOMER_DIAMOND("다이아몬드",1000000L),
    ADMIN("관리자",Long.MAX_VALUE)
    ;

    private final String rankName;
    private final Long score;

    MemberRole(String value, Long score) {
        this.rankName = value;
        this.score = score;
    }

    public static MemberRole getRank(Long score){
        return Arrays.stream(MemberRole.values())
                .filter(memberRole -> memberRole.getScore() >= score)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s점수 이상의 등급이 존재하지 않습니다.", score)));
    }

    public static MemberRole getRankName(String rankName){
        return Arrays.stream(MemberRole.values())
                .filter(memberRole -> memberRole.getValue().equals(rankName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("회원등급에 %s가 존재하지 않습니다.", rankName)));
    }

    public String getValue() {
        return rankName;
    }

    public Long getScore() {
        return score;
    }
}
