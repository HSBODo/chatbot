package site.pointman.chatbot.controller.admin;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.service.ProductService;

@RequestMapping(value = "admin/products")
@Controller
public class ProductAdminController {

    ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @ResponseBody
    @GetMapping(value = "all")
    public HttpResponse getProducts(){
        return productService.getProductsAll();
    }

    @ResponseBody
    @GetMapping(value = "")
    public HttpResponse getProductsByUserKey(@RequestParam("userKey") String userKey){
        return productService.getMemberProducts(userKey);
    }

    @ResponseBody
    @GetMapping(value = "{productId}")
    public HttpResponse getProduct(@PathVariable("productId") Long productId){
        return productService.getProduct(productId);
    }

    @ResponseBody
    @PatchMapping(value = "{productId}")
    public HttpResponse updateProductStatus(@PathVariable Long productId, @RequestParam String status){
        ProductStatus productStatus = ProductStatus.getProductStatus(status);
        return productService.updateProductStatus(productId,productStatus);
    }
}
