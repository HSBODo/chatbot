package site.pointman.chatbot.domain.product.converter;

import site.pointman.chatbot.domain.product.constatnt.Category;

import javax.persistence.AttributeConverter;

public class CategoryEnumConverter implements AttributeConverter<Category, String> {
    @Override
    public String convertToDatabaseColumn(Category attribute) {
        if (attribute == null) return null;

        return attribute.getValue();
    }

    @Override
    public Category convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        try {
            return Category.getCategory(dbData);
        }catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
