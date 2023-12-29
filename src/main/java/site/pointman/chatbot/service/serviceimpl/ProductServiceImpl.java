package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.service.S3FileService;
import site.pointman.chatbot.utill.CustomNumberUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Value("${host.url}")
    private String HOST_URL;
    private boolean isUse = true;

    S3FileService s3FileService;

    ProductRepository productRepository;
    MemberRepository memberRepository;
    OrderRepository orderRepository;

    public ProductServiceImpl(S3FileService s3FileService, ProductRepository productRepository, MemberRepository memberRepository, OrderRepository orderRepository) {
        this.s3FileService = s3FileService;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Response addProduct(ProductDto productDto, String userKey, List<String> imageUrls) {
        try {
            Long productId = CustomNumberUtils.createNumberId();


            Member member = memberRepository.findByUserKey(userKey,isUse).get();
            String productName = productDto.getName();

            productDto.setStatus(ProductStatus.판매중);
            productDto.setMember(member);
            productDto.setId(productId);

            ProductImageDto productImageDto = s3FileService.uploadImages(imageUrls, userKey,productName,"image");

            productRepository.insertProduct(productDto,productImageDto);

            return new Response(ResultCode.OK,"성공적으로 상품을 등록하였습니다.");
        }catch (Exception e){
            return  new Response(ResultCode.FAIL,"상품등록을 실패하였습니다.");
        }
    }

    @Override
    public Page<Product> getProductsByCategory(Category category, int pageNumber) {
        Sort sort = Sort.by("createDate").descending();

        Page<Product> products = productRepository.findByCategory(isUse,category, ProductStatus.판매중, ProductStatus.예약, PageRequest.of(pageNumber, 10, sort));

        return products;
    }

    @Override
    public Page<Product> getMemberProductsByStatus(String userKey, ProductStatus productStatus, int pageNumber) {
        Sort sort = Sort.by("createDate").descending();
        Page<Product> products = productRepository.findByStatusAndUserKey(isUse,productStatus, userKey, PageRequest.of(pageNumber, 10, sort));

        return products;
    }

    @Override
    public Page<Product> getMainProducts(int pageNumber) {
        Sort sort = Sort.by("createDate").descending();
        Page<Product> mainProducts = productRepository.findMain(isUse,ProductStatus.판매중, ProductStatus.예약, PageRequest.of(pageNumber, 10, sort));

        return mainProducts;
    }

    @Override
    public Optional<Product> getProduct(Long productId) {
        Optional<Product> mayBeProduct = productRepository.findByProductId(productId,isUse);

        return mayBeProduct;
    }

    @Override
    public Response deleteProduct(Long productId) {
        try {
            Optional<Product> mayBeProduct = productRepository.findByProductId(productId,isUse);
            if(mayBeProduct.isEmpty()) return new Response(ResultCode.EXCEPTION,"상품이 존재하지 않습니다.");

            productRepository.deleteProduct(productId,isUse);
            return new Response(ResultCode.OK,"상품을 정상적으로 삭제하였습니다.");

        }catch (Exception e){
            return new Response(ResultCode.FAIL,"상품 삭제를 실패하였습니다.");
        }
    }

    @Override
    public Page<Product> getProductsBySearchWord(String searchWord, int pageNumber) {

        Sort sort = Sort.by("createDate").descending();
        Page<Product> products = productRepository.findBySearchWord(isUse,searchWord, ProductStatus.판매중, ProductStatus.예약,PageRequest.of(pageNumber,10,sort));

        return products;
    }

    @Override
    public Page<Product> getSalesContractProducts(String userKey, int pageNumber) {
        Sort sort = Sort.by("createDate").descending();
        Page<Product> contractProducts = productRepository.findByStatusAndUserKey(isUse,ProductStatus.판매대기, userKey, PageRequest.of(pageNumber, 10, sort));

        return contractProducts;
    }

    @Override
    public Optional<Order> getSalesContractProduct(String userKey, Long orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId,OrderStatus.주문체결);
        return mayBeOrder;
    }

    @Override
    public Page<Order> getPurchaseProducts(String userKey, int pageNumber) {
        Sort sort = Sort.by("createDate").descending();
        Page<Order> purchaseOrders = orderRepository.findByBuyerUserKey(userKey, OrderStatus.주문취소, PageRequest.of(pageNumber, 10, sort));

        return purchaseOrders;
    }

    @Override
    public Optional<Order> getPurchaseProduct(String userKey, Long orderId) {

        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId);

        return mayBeOrder;
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

    @Override
    public Response updateProductStatus(Long productId, ProductStatus status) {
        try {
            Optional<Product> mayBeProduct = productRepository.findByProductId(productId,isUse);
            if(mayBeProduct.isEmpty()) return new Response(ResultCode.EXCEPTION ,"상품이 존재하지 않습니다.");

            productRepository.updateStatus(productId,status,isUse);

            return new Response(ResultCode.OK,"상품 "+productId+"를 "+status+"상태로 변경하였습니다.");
        }catch (Exception e){
            return new Response(ResultCode.FAIL ,"상품 상태변경을 실패하였습니다.");
        }
    }
}
