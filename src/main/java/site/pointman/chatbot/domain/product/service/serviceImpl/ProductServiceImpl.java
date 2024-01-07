package site.pointman.chatbot.domain.product.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.order.constatnt.OrderStatus;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.domain.product.dto.ProductImageDto;
import site.pointman.chatbot.domain.product.dto.ProductCondition;
import site.pointman.chatbot.exception.NotFoundMember;
import site.pointman.chatbot.exception.NotFoundProduct;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.domain.product.service.ProductService;
import site.pointman.chatbot.globalservice.S3FileService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Value("${host.url}")
    private String HOST_URL;

    private final boolean isUse = true;
    private final int size = 10;

    private final S3FileService s3FileService;

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public void addProduct(ProductDto productDto) {
        Member member = memberRepository.findByUserKey(productDto.getUserKey(),isUse)
                .orElseThrow(() -> new NotFoundMember("회원이 존재하지 않습니다."));

        List<String> imageUrls = productDto.getImageUrls();
        ProductImageDto productImageDto = s3FileService.uploadImages(imageUrls, productDto.getUserKey(),productDto.getName(),"image");
        ProductImage productImage = productImageDto.toEntity();

        Product product = Product.createProduct(productDto,member,productImage);

        productRepository.saveProduct(product);
    }

    @Override
    public Page<Product> getProducts(ProductCondition productCondition, int pageNumber) {
        Page<Product> Products = productRepository.findAll(productCondition, PageRequest.of(pageNumber, size));

        return Products;
    }


    @Override
    public Optional<Product> getProduct(Long productId) {
        Optional<Product> mayBeProduct = productRepository.findByProductId(productId,isUse);

        return mayBeProduct;
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findByProductId(productId, isUse)
                .orElseThrow(() -> new NotFoundProduct("상품이 존재하지 않습니다."));

        product.deleteProduct();
    }
    @Override
    public Page<Product> getSalesContractProducts(String userKey, int pageNumber) {

        ProductCondition productCondition = ProductCondition.builder()
                .userKey(userKey)
                .firstProductStatus(ProductStatus.판매대기)
                .build();

        Page<Product> contractProducts = getProducts(productCondition ,pageNumber);

        return contractProducts;
    }

    @Override
    public List<Product> getProductsAll() {
        List<Product> products = productRepository.findByAll();

        return products;
    }

    @Override
    public List<Product> getMemberProducts(String userKey) {
        List<Product> products = productRepository.findByUserKey(userKey, isUse);

        return products;
    }

    @Transactional
    @Override
    public void updateProductStatus(Long productId, ProductStatus updateStatus) {
        Product product = productRepository.findByProductId(productId, isUse)
                .orElseThrow(() -> new NotFoundProduct("상품이 존재하지 않습니다."));

        product.changeStatus(updateStatus);
    }
}
