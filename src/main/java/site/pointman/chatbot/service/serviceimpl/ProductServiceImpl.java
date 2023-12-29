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
    public Response getProductsByCategory(Category category, int pageNumber) {
        try {
            Sort sort = Sort.by("createDate").descending();

            Page<Product> products = productRepository.findByCategory(category, ProductStatus.판매중, ProductStatus.예약, PageRequest.of(pageNumber, 10, sort));

            if(products.getContent().isEmpty()) return new Response(ResultCode.FAIL,"등록된 상품이 없습니다.");

            return  new Response(ResultCode.OK,"정상적으로 상품을 조회하였습니다.",products);
        }catch (Exception e) {
            return  new Response(ResultCode.FAIL,"상품 조회를 실패하였습니다.");
        }
    }

    @Override
    public Response getMemberProductsByStatus(String userKey, ProductStatus productStatus, int pageNumber) {
        try {
            Sort sort = Sort.by("createDate").descending();
            Page<Product> products = productRepository.findByStatusAndUserKey(productStatus, userKey, PageRequest.of(pageNumber, 10, sort));

            if(products.getContent().isEmpty()) return new Response(ResultCode.FAIL,"등록된 상품이 없습니다.");

            return new Response(ResultCode.OK,"정상적으로 상품을 조회하였습니다.",products);
        }catch (Exception e){
            return new Response(ResultCode.EXCEPTION,"상품조회를 실패하였습니다.");
        }
    }

    @Override
    public Response getMainProducts(int page) {
        Sort sort = Sort.by("createDate").descending();
        Page<Product> mainProducts = productRepository.findMain(ProductStatus.판매중, ProductStatus.예약, PageRequest.of(page, 10, sort));
        if (!mainProducts.hasContent()) return new Response(ResultCode.EXCEPTION,"등록된 상품이 없습니다.");

        return new Response(ResultCode.OK,"정상적으로 상품을 조회하였습니다.",mainProducts);
    }

    @Override
    public Response getProduct(Long productId) {
        try {
            Optional<Product> mayBeProduct = productRepository.findByProductId(productId);

            if(mayBeProduct.isEmpty()) return new Response(ResultCode.EXCEPTION,"상품이 존재하지 않습니다.");

            Product product = mayBeProduct.get();

            return new Response(ResultCode.OK,"정상적으로 상품을 조회하였습니다.",product);
        }catch (Exception e){
            return new Response(ResultCode.FAIL,"상품조회를 실패하였습니다.");
        }
    }

    @Override
    public Response deleteProduct(Long productId) {
        try {
            Optional<Product> mayBeProduct = productRepository.findByProductId(productId);
            if(mayBeProduct.isEmpty()) return new Response(ResultCode.EXCEPTION,"상품이 존재하지 않습니다.");

            productRepository.deleteProduct(productId);
            return new Response(ResultCode.OK,"상품을 정상적으로 삭제하였습니다.");

        }catch (Exception e){
            return new Response(ResultCode.FAIL,"상품 삭제를 실패하였습니다.");
        }
    }

    @Override
    public Response getProductsBySearchWord(String searchWord, int pageNumber) {
        try {
            Sort sort = Sort.by("createDate").descending();
            Page<Product> products = productRepository.findBySearchWord(searchWord, ProductStatus.판매중, ProductStatus.예약,PageRequest.of(pageNumber,10,sort));


            if(products.getContent().isEmpty()) return new Response(ResultCode.EXCEPTION,"등록된 상품이 없어 상품을 찾을수 없습니다.");

            return new Response(ResultCode.OK,"정상적으로 상품을 조회하였습니다.",products);
        }catch (Exception e) {
            return new Response(ResultCode.FAIL,"상품조회에 실패하였습니다.");
        }
    }

    @Override
    public Response getSalesContractProducts(String userKey, int pageNumber) {
        Sort sort = Sort.by("createDate").descending();
        Page<Product> contractProducts = productRepository.findByStatusAndUserKey(ProductStatus.판매대기, userKey, PageRequest.of(pageNumber, 10, sort));

        if(contractProducts.isEmpty()) return new Response(ResultCode.EXCEPTION,"판매대기 중인 상품이 존재하지 않습니다.");

        return new Response(ResultCode.OK,"성공적으로 결제가 체결된 상품을 조회하였습니다.",contractProducts);
    }

    @Override
    public Response getSalesContractProduct(String userKey, Long orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId,OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) return new Response(ResultCode.EXCEPTION,"체결된 주문이 없습니다.");

        Order order = mayBeOrder.get();
        if(!userKey.equals(order.getProduct().getMember().getUserKey())) return new Response(ResultCode.EXCEPTION,"체결된 주문이 없습니다.");

        return new Response(ResultCode.OK,"성공적으로 결제가 체결된 상품을 조회하였습니다.",order);
    }

    @Override
    public Response getPurchaseProducts(String userKey, int pageNumber) {
        Sort sort = Sort.by("createDate").descending();
        Page<Order> purchaseOrders = orderRepository.findByBuyerUserKey(userKey, OrderStatus.주문취소, PageRequest.of(pageNumber, 10, sort));

        if (purchaseOrders.getContent().isEmpty()) return new Response(ResultCode.EXCEPTION,"구매내역이 없습니다.");

        return new Response(ResultCode.OK,"정상적으로 구매내역을 조회하였습니다.",purchaseOrders);
    }

    @Override
    public Response getPurchaseProduct(String userKey, String orderId) {
        try {
            Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId));
            if (mayBeOrder.isEmpty()) return new Response(ResultCode.EXCEPTION,"구매내역이 없습니다.");

            Order order = mayBeOrder.get();

            return new Response(ResultCode.OK,"정상적으로 구매내역을 조회하였습니다.",order);
        }catch (Exception e) {
            return new Response(ResultCode.FAIL,"구매내역 조회에 실패하였습니다.");
        }
    }

    @Override
    public Response getProductsAll() {
        List<Product> products = productRepository.findByAll();
        if (products.isEmpty()) return new Response(ResultCode.FAIL,"상품이 존재하지 않습니다.");
        return  new Response(ResultCode.OK,"성공적으로 상품을 조회하였습니다.",products);
    }

    @Override
    public Response getMemberProducts(String userKey) {
        List<Product> products = productRepository.findByUserKey(userKey);
        if (products.isEmpty()) return new Response(ResultCode.FAIL,"상품이 존재하지 않습니다.");
        return new Response(ResultCode.OK,"성공적으로 상품을 조회하였습니다.",products);
    }

    @Override
    public Response updateProductStatus(Long productId, ProductStatus status) {
        try {
            Optional<Product> mayBeProduct = productRepository.findByProductId(productId);
            if(mayBeProduct.isEmpty()) return new Response(ResultCode.EXCEPTION ,"상품이 존재하지 않습니다.");

            productRepository.updateStatus(productId,status);

            return new Response(ResultCode.OK,"상품 "+productId+"를 "+status+"상태로 변경하였습니다.");
        }catch (Exception e){
            return new Response(ResultCode.FAIL ,"상품 상태변경을 실패하였습니다.");
        }
    }
}
