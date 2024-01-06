package site.pointman.chatbot.domain.notice.converter;

import site.pointman.chatbot.domain.notice.constant.NoticeType;

import javax.persistence.AttributeConverter;

public class NoticeTypeEnumConverter implements AttributeConverter<NoticeType, String> {
    @Override
    public String convertToDatabaseColumn(NoticeType attribute) {
        if (attribute == null) return null;

        return attribute.getValue();
    }

    @Override
    public NoticeType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        try {
            return NoticeType.getType(dbData);
        }catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
