package site.pointman.chatbot.repository.customrepository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.product.dto.ProductCondition;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.repository.customrepository.ProductCustomRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static site.pointman.chatbot.domain.product.QProduct.product;
import static site.pointman.chatbot.domain.product.QProductImage.productImage;

@Transactional(readOnly = true)
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public ProductCustomRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }
    @Transactional
    @Override
    public void saveProduct(Product product) {
        em.persist(product);
    }

    @Override
    public List<Product> findByUserKey(String userKey, boolean isUse) {
        return em.createQuery("SELECT p " +
                        "FROM Product p " +
                        "JOIN FETCH p.member " +
                        "JOIN FETCH p.productImages " +
                        "WHERE p.member.userKey=:user_Key " +
                        "AND " +
                        "p.isUse=:isUse " +
                        "ORDER BY p.createDate DESC", Product.class)
                .setParameter("user_Key", userKey)
                .setParameter("isUse", isUse)
                .getResultList();
    }

    @Override
    public Optional<Product> findByProductId(Long productId, boolean isUse) {
        return em.createQuery("SELECT p " +
                        "FROM Product p " +
                        "JOIN FETCH p.member " +
                        "JOIN FETCH p.productImages " +
                        "WHERE p.id=:id " +
                        "AND " +
                        "p.isUse=:isUse", Product.class)
                .setParameter("id", productId)
                .setParameter("isUse", isUse)
                .getResultList()
                .stream().findAny();
    }

    @Override
    public Page<Product> findAll(ProductCondition productCondition, Pageable pageable) {

        QueryResults<Product> results = query
                .select(product)
                .from(product)
                .join(product.productImages, productImage).fetchJoin()
                .where(
                        eqUserKey(productCondition.getUserKey()),
                        eqProductStatus(productCondition.getFirstProductStatus(),productCondition.getSecondProductStatus()),
                        eqProductCategory(productCondition.getProductCategory()),
                        likeSearchWord(productCondition.getSearchWord()),
                        isUseDefault()
                )
                .orderBy(product.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Product> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public Page<ProductDto> findDtoAll(ProductCondition productCondition, Pageable pageable) {
        QueryResults<ProductDto> results = query
                .select(Projections.constructor(
                        ProductDto.class,
                        product.id,
                        product.member.userKey,
                        product.member.name,
                        product.name,
                        product.price,
                        product.description,
                        product.tradingLocation,
                        product.kakaoOpenChatUrl,
                        product.category,
                        product.productImages.imageUrls,
                        product.status,
                        product.createDate
                        ))
                .from(product)
                .where(
                        eqUserKey(productCondition.getUserKey()),
                        eqProductStatus(productCondition.getFirstProductStatus(),productCondition.getSecondProductStatus()),
                        eqProductCategory(productCondition.getProductCategory()),
                        likeSearchWord(productCondition.getSearchWord()),
                        isUseDefault()
                )
                .orderBy(product.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ProductDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    private BooleanExpression eqProductStatus(ProductStatus firstStatus, ProductStatus secondStatus) {
        if (Objects.isNull(firstStatus) && Objects.isNull(secondStatus)) return null;

        if (Objects.nonNull(firstStatus) && Objects.isNull(secondStatus)){
            return product.status.eq(firstStatus);
        }

        if (Objects.isNull(firstStatus) && Objects.nonNull(secondStatus)){
            return product.status.eq(secondStatus);
        }

        return product.status.eq(firstStatus).or(product.status.eq(secondStatus));
    }

    private BooleanExpression eqProductCategory(Category category) {
        if (Objects.isNull(category)){
            return null;
        }
        return product.category.eq(category);
    }

    private BooleanExpression likeSearchWord(String searchWord) {
        if (!StringUtils.hasText(searchWord)){
            return null;
        }
        return product.name.like(searchWord+"%");
    }

    private BooleanExpression eqUserKey(String userKey) {
        if (!StringUtils.hasText(userKey)){
            return null;
        }
        return product.member.userKey.eq(userKey);
    }

    private BooleanExpression isUseDefault() {
        return product.isUse.eq(true);
    }

}
