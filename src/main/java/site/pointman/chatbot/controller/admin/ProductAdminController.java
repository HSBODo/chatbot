package site.pointman.chatbot.controller.admin;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.product.dto.ProductCondition;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.domain.product.service.ProductService;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "admin/products")
@RestController
public class ProductAdminController {

    ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping(value = "")
    public ResponseEntity getProducts(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "userKey",required = false) String userKey,
            @RequestParam(value = "productCategory",required = false) String productCategory,
            @RequestParam(value = "searchWord",required = false) String searchWord,
            @RequestParam(value = "firstProductStatus",required = false) String firstProductStatus,
            @RequestParam(value = "secondProductStatus",required = false) String secondProductStatus){
        HttpHeaders headers = getHeaders();
        try {
            Category category = null;
            ProductStatus firstStatus = null;
            ProductStatus secondStatus = null;

            if (StringUtils.hasText(productCategory)) {
                category = Category.getCategory(productCategory);
            }

            if (StringUtils.hasText(firstProductStatus)) {
                firstStatus = ProductStatus.getProductStatus(firstProductStatus);
            }

            if (StringUtils.hasText(secondProductStatus)) {
                secondStatus = ProductStatus.getProductStatus(secondProductStatus);
            }

            ProductCondition productCondition = ProductCondition.builder()
                    .userKey(userKey)
                    .productCategory(category)
                    .searchWord(searchWord)
                    .firstProductStatus(firstStatus)
                    .secondProductStatus(secondStatus)
                    .build();

            Page<ProductDto> productDtos = productService.getProductDtos(productCondition, page);

            return new ResponseEntity(new Response<>(ResultCode.OK,"성공적으로 상품을 조회하였습니다,",productDtos.getContent()),headers, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity(new Response<>(ResultCode.EXCEPTION,e.getMessage()),headers, HttpStatus.OK);
        }

    }

    @GetMapping(value = "{productId}")
    public ResponseEntity getProduct(@PathVariable("productId") Long productId){
        HttpHeaders headers = getHeaders();
        try {
            ProductDto productDto = productService.getProductDto(productId);

            return new ResponseEntity(new Response<>(ResultCode.OK,"성공적으로 상품을 조회하였습니다.",productDto),headers, HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(new Response<>(ResultCode.EXCEPTION,"상품조회를 실패하였습니다"),headers, HttpStatus.OK);
        }

    }


    @PatchMapping(value = "{productId}")
    public ResponseEntity updateProductStatus(@PathVariable Long productId, @RequestParam String status){
        HttpHeaders headers = getHeaders();
        try {
            ProductStatus productStatus = ProductStatus.getProductStatus(status);

            productService.updateProductStatus(productId, productStatus);

            return new ResponseEntity(new Response<>(ResultCode.OK,"성공적으로 상품의 상태를 변경하였습니다. 상품 = "+ productId,productStatus),headers, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity(new Response<>(ResultCode.EXCEPTION,"상품 상태변경을 실패하였습니다",e.getMessage()),headers, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "{productId}")
    public ResponseEntity deleteProduct(@PathVariable Long productId){
        HttpHeaders headers = getHeaders();
        try {

            productService.deleteProduct(productId);

            return new ResponseEntity(new Response<>(ResultCode.OK,"성공적으로 상품을 삭제하였습니다.",productId),headers, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity(new Response<>(ResultCode.EXCEPTION,"상품 삭제를 실패하였습니다",e.getMessage()),headers, HttpStatus.OK);
        }
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
}
