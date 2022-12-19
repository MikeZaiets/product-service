package ua.com.zmike.product.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ConfigurationProperties("db.query")
@Data
public class DbQueryProperty {

    private static final int MIN_ALLOWED_PAGE_SIZE = 1;
    private static final int MAX_ALLOWED_PAGE_SIZE = 100000;

    @Min(MIN_ALLOWED_PAGE_SIZE)
    @Max(MAX_ALLOWED_PAGE_SIZE)
    private int pageSize;
}
