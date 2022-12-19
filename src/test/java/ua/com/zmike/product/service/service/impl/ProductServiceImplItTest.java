package ua.com.zmike.product.service.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.com.zmike.product.service.dto.ProductDto;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.zmike.product.service.util.TestConstants.ACTUAL_COUNT_OF_ITEM_IN_TEST_DB;
import static ua.com.zmike.product.service.util.TestConstants.BROKEN_PATTERN;
import static ua.com.zmike.product.service.util.TestConstants.GLOBAL_PATTERN;

@SpringBootTest
@ActiveProfiles("it-test")
class ProductServiceImplItTest {

    @Autowired
    private ProductServiceImpl productService;

    @Test
    @Sql({"/db/clean-db.sql", "/db/fill-db.sql"})
    void getAllFilteredProducts_shouldReturnAll_whenNotFilterAnyItem() {
        var result = productService.getAllFilteredProducts(BROKEN_PATTERN);

        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(ACTUAL_COUNT_OF_ITEM_IN_TEST_DB);

        result.forEach(Assertions::assertNotNull);
        result.stream().map(ProductDto::getName).forEach(Assertions::assertNotNull);
        result.stream().map(ProductDto::getDescription).forEach(Assertions::assertNotNull);
    }

    @ParameterizedTest
    @Sql({"/db/clean-db.sql", "/db/fill-db.sql"})
    @CsvSource({"^.*[A-D].*$,7", "^[^A].*,10", "^(A|B|C|a|b|c).*,8", "\\S*,4", ".{2},26", "A+,35", ".*\\D,3"})
    void getAllFilteredProducts_shouldReturnList_whenSuitableItemsExistInDb(ArgumentsAccessor argumentsAccessor) {
        var testedNameFilter = argumentsAccessor.getString(0);
        var expectedItemCount = argumentsAccessor.getInteger(1);

        var result = productService.getAllFilteredProducts(testedNameFilter);

        assertThat(result)
                .isNotEmpty()
                .hasSize(expectedItemCount);

        result.forEach(Assertions::assertNotNull);
        result.stream().map(ProductDto::getName).forEach(Assertions::assertNotNull);
        result.stream().map(ProductDto::getDescription).forEach(Assertions::assertNotNull);
    }

    @Test
    @Sql({"/db/clean-db.sql"})
    void getAllFilteredProducts_shouldReturnEmptyList_whenDbReturnNoItems() {
        var result = productService.getAllFilteredProducts(GLOBAL_PATTERN);

        assertThat(result)
                .isNotNull()
                .isEqualTo(Collections.emptySet());
    }
}
