package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.HttpResponse;
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
    public HttpResponse addProduct(ProductDto productDto, String userKey, List<String> imageUrls) {
        try {
            Long productId = CustomNumberUtils.createNumberId();


            Member member = memberRepository.findByUserKey(userKey).get();
            String productName = productDto.getName();

            productDto.setStatus(ProductStatus.판매중);
            productDto.setMember(member);
            productDto.setId(productId);

            ProductImageDto productImageDto = s3FileService.uploadImages(imageUrls, userKey,productName,"image");

            productRepository.insertProduct(productDto,productImageDto);

            return new HttpResponse(ApiResultCode.OK,"성공적으로 상품을 등록하였습니다.");
        }catch (Exception e){
            return  new HttpResponse(ApiResultCode.FAIL,"상품등록을 실패하였습니다.");
        }
    }

    @Override
    public HttpResponse getProductsByCategory(Category category) {
        try {
            List<Product> products = productRepository.findByCategory(category,ProductStatus.판매중,ProductStatus.예약);

            if(products.isEmpty()) return new HttpResponse(ApiResultCode.FAIL,"등록된 상품이 없습니다.");

            return  new HttpResponse(ApiResultCode.OK,"정상적으로 상품을 조회하였습니다.",products);
        }catch (Exception e) {
            return  new HttpResponse(ApiResultCode.FAIL,"상품 조회를 실패하였습니다.");
        }
    }

    @Override
    public HttpResponse getMemberProductsByStatus(String userKey, ProductStatus productStatus) {
        try {
            List<Product> products = productRepository.findByUserKey(userKey,productStatus);

            if(products.isEmpty()) return new HttpResponse(ApiResultCode.FAIL,"등록된 상품이 없습니다.");

            return new HttpResponse(ApiResultCode.OK,"정상적으로 상품을 조회하였습니다.",products);
        }catch (Exception e){
            return new HttpResponse(ApiResultCode.EXCEPTION,"상품조회를 실패하였습니다.");
        }
    }

    @Override
    public HttpResponse getMainProducts(int page) {
        Sort sort = Sort.by("createDate").descending();
        Page<Product> mainProducts = productRepository.findMain(ProductStatus.판매중, ProductStatus.예약, PageRequest.of(page, 10, sort));
        if (!mainProducts.hasContent()) return new HttpResponse(ApiResultCode.EXCEPTION,"등록된 상품이 없습니다.");

        return new HttpResponse(ApiResultCode.OK,"정상적으로 상품을 조회하였습니다.",mainProducts);
    }

    @Override
    public HttpResponse getProduct(Long productId) {
        try {
            Optional<Product> mayBeProduct = productRepository.findByProductId(productId);

            if(mayBeProduct.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"상품이 존재하지 않습니다.");

            Product product = mayBeProduct.get();

            return new HttpResponse(ApiResultCode.OK,"정상적으로 상품을 조회하였습니다.",product);
        }catch (Exception e){
            return new HttpResponse(ApiResultCode.FAIL,"상품조회를 실패하였습니다.");
        }
    }

    @Override
    public HttpResponse deleteProduct(Long productId) {
        try {
            Optional<Product> mayBeProduct = productRepository.findByProductId(productId);
            if(mayBeProduct.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"상품이 존재하지 않습니다.");

            productRepository.deleteProduct(productId);
            return new HttpResponse(ApiResultCode.OK,"상품을 정상적으로 삭제하였습니다.");

        }catch (Exception e){
            return new HttpResponse(ApiResultCode.FAIL,"상품 삭제를 실패하였습니다.");
        }
    }

    @Override
    public HttpResponse getProductsBySearchWord(String searchWord, int pageNumber) {
        try {
            Sort sort = Sort.by("createDate").descending();
            Page<Product> products = productRepository.findBySearchWord(searchWord, ProductStatus.판매중, ProductStatus.예약,PageRequest.of(pageNumber,10,sort));


            if(products.getContent().isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"등록된 상품이 없어 상품을 찾을수 없습니다.");

            return new HttpResponse(ApiResultCode.OK,"정상적으로 상품을 조회하였습니다.",products);
        }catch (Exception e) {
            return new HttpResponse(ApiResultCode.FAIL,"상품조회에 실패하였습니다.");
        }
    }

    @Override
    public HttpResponse getSalesContractProducts(String userKey) {
        List<Product> contractProducts = productRepository.findByUserKey(userKey, ProductStatus.판매대기);
        if(contractProducts.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"결제가 체결된 상품이 없습니다.");

        return new HttpResponse(ApiResultCode.OK,"성공적으로 결제가 체결된 상품을 조회하였습니다.",contractProducts);
    }

    @Override
    public HttpResponse getSalesContractProduct(String userKey, Long orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId,OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"체결된 주문이 없습니다.");

        Order order = mayBeOrder.get();
        if(!userKey.equals(order.getProduct().getMember().getUserKey())) return new HttpResponse(ApiResultCode.EXCEPTION,"체결된 주문이 없습니다.");

        return new HttpResponse(ApiResultCode.OK,"성공적으로 결제가 체결된 상품을 조회하였습니다.",order);
    }

    @Override
    public HttpResponse getPurchaseProducts(String userKey) {
        List<Order> purchaseOrders = orderRepository.findByBuyerUserKey(userKey);
        if (purchaseOrders.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"구매내역이 없습니다.");

        return new HttpResponse(ApiResultCode.OK,"정상적으로 구매내역을 조회하였습니다.",purchaseOrders);
    }

    @Override
    public HttpResponse getPurchaseProduct(String userKey, String orderId) {
        try {
            Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId));
            if (mayBeOrder.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"구매내역이 없습니다.");

            Order order = mayBeOrder.get();

            return new HttpResponse(ApiResultCode.OK,"정상적으로 구매내역을 조회하였습니다.",order);
        }catch (Exception e) {
            return new HttpResponse(ApiResultCode.FAIL,"구매내역 조회에 실패하였습니다.");
        }
    }

    @Override
    public HttpResponse getProductsAll() {
        List<Product> products = productRepository.findByAll();
        if (products.isEmpty()) return new HttpResponse(ApiResultCode.FAIL,"상품이 존재하지 않습니다.");
        return  new HttpResponse(ApiResultCode.OK,"성공적으로 상품을 조회하였습니다.",products);
    }

    @Override
    public HttpResponse getMemberProducts(String userKey) {
        List<Product> products = productRepository.findByUserKey(userKey);
        if (products.isEmpty()) return new HttpResponse(ApiResultCode.FAIL,"상품이 존재하지 않습니다.");
        return new HttpResponse(ApiResultCode.OK,"성공적으로 상품을 조회하였습니다.",products);
    }

    @Override
    public HttpResponse updateProductStatus(Long productId, ProductStatus status) {
        try {
            Optional<Product> mayBeProduct = productRepository.findByProductId(productId);
            if(mayBeProduct.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION ,"상품이 존재하지 않습니다.");

            productRepository.updateStatus(productId,status);

            return new HttpResponse(ApiResultCode.OK,"상품 "+productId+"를 "+status+"상태로 변경하였습니다.");
        }catch (Exception e){
            return new HttpResponse(ApiResultCode.FAIL ,"상품 상태변경을 실패하였습니다.");
        }
    }
}
