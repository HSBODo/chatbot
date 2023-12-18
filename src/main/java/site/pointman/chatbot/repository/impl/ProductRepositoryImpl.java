package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.repository.ProductRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class ProductRepositoryImpl implements ProductRepository {
    private final EntityManager em;

    public ProductRepositoryImpl(EntityManager em) {
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
        return em.createQuery("select p from Product p where p.customer.userKey=:user_Key", Product.class)
                .setParameter("user_Key", userKey)
                .getResultList();
    }

    @Override
    public Optional<Product> findByProductId(Long productId) {
        return em.createQuery("select p from Product p where p.id=:id", Product.class)
                .setParameter("id", productId)
                .getResultList()
                .stream().findAny();
    }

    @Override
    public List<Product> findByCategory(Category category,ProductStatus status) {
        return em.createQuery("select p from Product p where p.category=:category AND p.status =:status", Product.class)
                .setParameter("category", category)
                .setParameter("status", status)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public void updateStatus(Long productId, ProductStatus productStatus) {
        Product product = em.createQuery("select p from Product p where p.id=:id", Product.class)
                .setParameter("id", productId)
                .getSingleResult();
        product.changeStatus(productStatus);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product findProduct = em.createQuery("select p from Product p where p.id=:id", Product.class)
                .setParameter("id", productId)
                .getSingleResult();

        Long productImageId = findProduct.getProductImages().getId();
        ProductImage findProductImage = em.createQuery("select p from ProductImage p where p.id=:id", ProductImage.class)
                .setParameter("id", productImageId)
                .getSingleResult();


        em.remove(findProduct);
        em.remove(findProductImage);
    }

    @Override
    public List<Product> findBySearchWord(String searchWord, ProductStatus status) {
        return em.createQuery("select p from Product p where p.name like concat('%', :searchWord, '%') OR p.description like concat('%', :searchWord, '%') AND p.status =:status", Product.class)
                .setParameter("searchWord", searchWord)
                .setParameter("status",status)
                .setMaxResults(10)
                .getResultList();
    }
}
