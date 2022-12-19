package ua.com.zmike.product.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.zmike.product.service.service.ProductService;
import ua.com.zmike.product.service.util.TestEntityFactory;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.zmike.product.service.util.TestConstants.API_PATH;
import static ua.com.zmike.product.service.util.TestConstants.EMPTY_JSON_CONTENT;
import static ua.com.zmike.product.service.util.TestConstants.QUERY_KEY;
import static ua.com.zmike.product.service.util.TestConstants.TEST_PATTERN;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService mockProductService;

    @Test
    void getProducts_shouldReturnList_whenServiceReturnsList() throws Exception {
        var productDto = TestEntityFactory.getProductDto(1L);
        var products = Set.of(productDto);

        var expectedResponse = mapper.writeValueAsString(products);

        when(mockProductService
                .getAllFilteredProducts(TEST_PATTERN))
                .thenReturn(products);

        var response = mockMvc
                .perform(get(API_PATH).param(QUERY_KEY, TEST_PATTERN))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);
        verify(mockProductService, only()).getAllFilteredProducts(TEST_PATTERN);
    }

    @Test
    void getProducts_shouldReturnEmpty_whenEmptyServiceResponse() throws Exception {
        when(mockProductService
                .getAllFilteredProducts(TEST_PATTERN))
                .thenReturn(Collections.emptySet());

        var response = mockMvc
                .perform(get(API_PATH).param(QUERY_KEY, TEST_PATTERN))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(EMPTY_JSON_CONTENT);
        verify(mockProductService, only()).getAllFilteredProducts(TEST_PATTERN);
    }

    @Test
    void getProducts_shouldReturnBadRequest_whenNoPattern() throws Exception {
        mockMvc.perform(get(API_PATH))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(mockProductService);
    }

    @Test
    void getProducts_shouldInternalServerError_whenServiceThrows() throws Exception {
        when(mockProductService
                .getAllFilteredProducts(TEST_PATTERN))
                .thenThrow(RuntimeException.class);

        mockMvc.perform(get(API_PATH).param(QUERY_KEY, TEST_PATTERN))
                .andExpect(status().isInternalServerError());

        verify(mockProductService, only()).getAllFilteredProducts(TEST_PATTERN);
    }
}
