package site.pointman.chatbot.repository.customrepository.impl;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
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
    public List<Product> findByUserKey(String userKey) {
        return em.createQuery("SELECT p FROM Product p WHERE p.member.userKey=:user_Key AND p.isUse=:isUse ORDER BY p.createDate DESC", Product.class)
                .setParameter("user_Key", userKey)
                .setParameter("isUse", true)
                .getResultList();
    }

    @Override
    public List<Product> findByUserKey(String userKey, ProductStatus status) {
        return em.createQuery("SELECT p FROM Product p WHERE p.member.userKey=:user_Key AND p.status =:status AND p.isUse=:isUse ORDER BY p.createDate DESC", Product.class)
                .setParameter("user_Key", userKey)
                .setParameter("status", status)
                .setParameter("isUse", true)
                .getResultList();
    }

    @Override
    public Optional<Product> findByProductId(Long productId) {
        return em.createQuery("SELECT p FROM Product p WHERE p.id=:id AND p.isUse=:isUse", Product.class)
                .setParameter("id", productId)
                .setParameter("isUse", true)
                .getResultList()
                .stream().findAny();
    }

    @Override
    public List<Product> findByCategory(Category category,ProductStatus status) {
        return em.createQuery("SELECT p FROM Product p WHERE p.category=:category AND p.status =:status AND p.isUse=:isUse ORDER BY p.createDate DESC", Product.class)
                .setParameter("category", category)
                .setParameter("status", status)
                .setParameter("isUse", true)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public void updateStatus(Long productId, ProductStatus productStatus) {
        Product product = em.createQuery("SELECT p FROM Product p WHERE p.id=:id AND p.isUse=:isUse", Product.class)
                .setParameter("id", productId)
                .setParameter("isUse", true)
                .getSingleResult();

        product.changeStatus(productStatus);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product findProduct = em.createQuery("SELECT p FROM Product p WHERE p.id=:id AND p.isUse=:isUse", Product.class)
                .setParameter("id", productId)
                .setParameter("isUse", true)
                .getSingleResult();

        Long productImageId = findProduct.getProductImages().getId();
        ProductImage findProductImage = em.createQuery("SELECT p FROM ProductImage p WHERE p.id=:id AND p.isUse=:isUse", ProductImage.class)
                .setParameter("id", productImageId)
                .setParameter("isUse", true)
                .getSingleResult();

        findProduct.delete();
        findProductImage.delete();
    }

    @Override
    public List<Product> findBySearchWord(String searchWord, ProductStatus status) {
        return em.createQuery("SELECT p FROM Product p WHERE p.name LIKE concat('%', :searchWord, '%') OR p.description LIKE concat('%', :searchWord, '%') AND p.status =:status AND p.isUse=:isUse ORDER BY p.createDate DESC", Product.class)
                .setParameter("searchWord", searchWord)
                .setParameter("status",status)
                .setParameter("isUse", true)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public List<Product> findByAll() {
        return em.createQuery("SELECT p FROM Product p ORDER BY p.createDate DESC", Product.class)
                .getResultList();
    }

    @Override
    public List<Product> findByStatus(ProductStatus firstStatus,ProductStatus secondStatus) {
        return em.createQuery("SELECT p FROM Product p WHERE p.status =:firstStatus OR p.status =:secondStatus AND p.isUse=:isUse ORDER BY p.createDate DESC", Product.class)
                .setParameter("firstStatus",firstStatus)
                .setParameter("secondStatus",secondStatus)
                .setParameter("isUse", true)
                .getResultList();
    }

    @Override
    public List<Product> findByStatus(ProductStatus firstStatus) {
        return em.createQuery("SELECT p FROM Product p WHERE p.status =:firstStatus OR p.status =:secondStatus AND p.isUse=:isUse ORDER BY p.createDate DESC", Product.class)
                .setParameter("firstStatus",firstStatus)
                .getResultList();

    }
}
