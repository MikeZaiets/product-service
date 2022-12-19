package ua.com.zmike.product.service.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;
import ua.com.zmike.product.service.dto.ProductDto;
import ua.com.zmike.product.service.model.Product;

@UtilityClass
public class TestEntityFactory {

    public static ProductDto getProductDto(Long... id) {
        var number = ObjectUtils.isEmpty(id) ? null : id[0];
        return ProductDto.builder()
                .id(number)
                .name("Name" + number)
                .description("Description" + number)
                .build();
    }

    public static Product getProduct(Long... id) {
        var product = new Product();
        var number = ObjectUtils.isEmpty(id) ? null : id[0];
        product.setId(number);
        product.setName("Name" + number);
        product.setDescription("Description" + number);
        return product;
    }
}
