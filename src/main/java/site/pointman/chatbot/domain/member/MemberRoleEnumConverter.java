package site.pointman.chatbot.domain.member;


import site.pointman.chatbot.constant.member.MemberRole;

import javax.persistence.AttributeConverter;

public class MemberRoleEnumConverter implements AttributeConverter<MemberRole, String> {
    @Override
    public String convertToDatabaseColumn(MemberRole attribute) {
        if (attribute == null) return null;

        return attribute.getValue();
    }

    @Override
    public MemberRole convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        try {
            return MemberRole.getRoleByRoleName(dbData);
        }catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
