package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.repository.ProductRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
public class ProductRepositoryImpl implements ProductRepository {
    private final EntityManager em;

    public ProductRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(ProductDto productDto) {
        Product product = productDto.toEntity();
        em.persist(product);
    }

    @Override
    public void productImageSave(ProductImageDto productImageDto) {
        ProductImage productImage = productImageDto.toEntity();
        em.persist(productImage);
    }
}
