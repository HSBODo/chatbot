package site.pointman.chatbot.repository.customrepository.impl;

import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.domain.product.dto.ProductImageDto;
import site.pointman.chatbot.repository.customrepository.ProductCustomRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final EntityManager em;

    public ProductCustomRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void saveProduct(Product productDto) {
        Product product = productDto;
        em.persist(product);
    }

    @Override
    public void saveProductImage(ProductImage productImage) {
        em.persist(productImage);
    }

    @Override
    public void insertProduct(ProductDto productDto, ProductImageDto productImageDto) {
        Product product = productDto.toEntity();
        ProductImage productImage = productImageDto.toEntity();

        product.changeProductImage(productImage);

        saveProduct(product);
        saveProductImage(productImage);
    }

    @Override
    public List<Product> findByUserKey(String userKey, boolean isUse) {
        return em.createQuery("SELECT p FROM Product p WHERE p.member.userKey=:user_Key AND p.isUse=:isUse ORDER BY p.createDate DESC", Product.class)
                .setParameter("user_Key", userKey)
                .setParameter("isUse", isUse)
                .getResultList();
    }

    @Override
    public Optional<Product> findByProductId(Long productId, boolean isUse) {
        return em.createQuery("SELECT p FROM Product p WHERE p.id=:id AND p.isUse=:isUse", Product.class)
                .setParameter("id", productId)
                .setParameter("isUse", isUse)
                .getResultList()
                .stream().findAny();
    }

    @Override
    public void updateStatus(Long productId, ProductStatus productStatus, boolean isUse) {
        Product product = em.createQuery("SELECT p FROM Product p WHERE p.id=:id AND p.isUse=:isUse", Product.class)
                .setParameter("id", productId)
                .setParameter("isUse", isUse)
                .getSingleResult();

        product.changeStatus(productStatus);
    }

    @Override
    public void deleteProduct(Long productId, boolean isUse) {
        Product findProduct = em.createQuery("SELECT p FROM Product p WHERE p.id=:id AND p.isUse=:isUse", Product.class)
                .setParameter("id", productId)
                .setParameter("isUse", isUse)
                .getSingleResult();

        Long productImageId = findProduct.getProductImages().getId();
        ProductImage findProductImage = em.createQuery("SELECT p FROM ProductImage p WHERE p.id=:id AND p.isUse=:isUse", ProductImage.class)
                .setParameter("id", productImageId)
                .setParameter("isUse", isUse)
                .getSingleResult();

        findProduct.delete();
        findProductImage.delete();
    }

    @Override
    public List<Product> findByAll() {
        return em.createQuery("SELECT p FROM Product p ORDER BY p.createDate DESC", Product.class)
                .getResultList();
    }

}
