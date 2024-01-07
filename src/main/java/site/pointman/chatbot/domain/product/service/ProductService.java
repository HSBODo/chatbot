package site.pointman.chatbot.domain.product.service;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.domain.product.dto.ProductCondition;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * 상품 등록
     * 상품 이미지 등록
     */
    void addProduct(ProductDto productDto);

    /**
     * 상품 상태 변경
     */
    void updateProductStatus(Long productId, ProductStatus updateStatus);

    /**
     * 상품 삭제
     * 상품 이미지 삭제
     */
    void deleteProduct(Long productId);

    /**
     * 조건 상품 전체 조회
     */
    Page<Product> getProducts(ProductCondition productCondition, int pageNumber);

    /**
     * 조건 상품Dto 전체 조회
     */
    Page<ProductDto> getProductDtos(ProductCondition productCondition, int pageNumber);

    /**
     * 거래 체결된 판매 상품 조회
     */
    Page<Product> getSalesContractProducts(String userKey, int pageNumber);

    /**
     * 상품 조회
     */
    Product getProduct(Long productId);

    /**
     * 상품Dto 조회
     */
    ProductDto getProductDto(Long productId);
}
