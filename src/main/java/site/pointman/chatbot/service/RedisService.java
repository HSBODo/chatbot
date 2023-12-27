package site.pointman.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.dto.product.SpecialProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public SpecialProduct getRedisSpecialProductValue(String key) throws JsonProcessingException {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String value = stringValueOperations.get(key);
        if (StringUtils.isNullOrEmpty(value)) return null;
        SpecialProduct specialProduct = objectMapper.readValue(value, SpecialProduct.class);
        return specialProduct;
    }

    public void setRedisSpecialProductValue(String key, SpecialProduct specialProduct) throws JsonProcessingException {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String value = objectMapper.writeValueAsString(specialProduct);

        stringValueOperations.set(key,value,10, TimeUnit.MINUTES);
    }

    public List<SpecialProduct> isSameSpecialProduct(int page, int firstProduct, int lastProduct, List<Element> filterElements) throws JsonProcessingException {
        boolean isSame = false;
        List<SpecialProduct> specialProducts = new ArrayList<>();
        for (int i = firstProduct; i<lastProduct; i++){
            String text = filterElements.get(i).select("span.ellipsis-with-reply-cnt").text();
            SpecialProduct redisStringValue = getRedisSpecialProductValue(page + "-" + i);

            if (Objects.isNull(redisStringValue) || !text.equals(redisStringValue.getTitle())) {
                isSame = false;
                break;
            }
            isSame = true;
            specialProducts.add(redisStringValue);
        }

        if (isSame) return specialProducts;
        return new ArrayList<>();
    }
}
