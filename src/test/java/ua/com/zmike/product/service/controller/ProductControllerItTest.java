package ua.com.zmike.product.service.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.zmike.product.service.config.DbQueryProperty;
import ua.com.zmike.product.service.repository.ProductRepository;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.zmike.product.service.util.TestConstants.API_PATH;
import static ua.com.zmike.product.service.util.TestConstants.EMPTY_JSON_CONTENT;
import static ua.com.zmike.product.service.util.TestConstants.EMPTY_PATTERN;
import static ua.com.zmike.product.service.util.TestConstants.GLOBAL_PATTERN;
import static ua.com.zmike.product.service.util.TestConstants.INCORRECT_QUERY_KEY;
import static ua.com.zmike.product.service.util.TestConstants.NAME_TO_BE_RECEIVED_WITH_VALID_PATTERN;
import static ua.com.zmike.product.service.util.TestConstants.QUERY_KEY;
import static ua.com.zmike.product.service.util.TestConstants.VALID_PATTERN;
import static ua.com.zmike.product.service.util.TestEntityFactory.getProduct;

@SpringBootTest
@ActiveProfiles("it-test")
@AutoConfigureMockMvc
class ProductControllerItTest {

    private static PageRequest expectedRequest;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DbQueryProperty dbQueryProperty;

    @SpyBean
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        expectedRequest = PageRequest.of(0, dbQueryProperty.getPageSize());
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(repository);
    }

    @Test
    void getProducts_shouldReturnFilteredData_whenValidRegex() throws Exception {
        var expectedResponse = getProduct(1L);
        expectedResponse.setName(NAME_TO_BE_RECEIVED_WITH_VALID_PATTERN);

        when(repository
                .findAll(expectedRequest))
                .thenReturn(new PageImpl<>(List.of(expectedResponse), expectedRequest, 1));

        mockMvc.perform(get(API_PATH).param(QUERY_KEY, VALID_PATTERN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(expectedResponse.getName())))
                .andExpect(jsonPath("$[0].description", is(expectedResponse.getDescription())));

        verify(repository, only()).findAll(expectedRequest);
    }

    @Test
    void getProducts_shouldReturnEmpty_whenFilteredAllData() throws Exception {
        when(repository
                .findAll(expectedRequest))
                .thenReturn(new PageImpl<>(List.of(), expectedRequest, 0));

        mockMvc.perform(get(API_PATH).param(QUERY_KEY, GLOBAL_PATTERN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(EMPTY_JSON_CONTENT));

        verify(repository, only()).findAll(expectedRequest);
    }

    @Test
    void getProducts_shouldReturnBadRequest_whenRegexIsBlank() throws Exception {
        mockMvc.perform(get(API_PATH).param(QUERY_KEY, EMPTY_PATTERN))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Request content")))
                .andExpect(jsonPath("$.message", is("getProducts.nameFilter: must not be blank")));

        verifyNoInteractions(repository);
    }

    @Test
    void getProducts_shouldReturnBadRequest_whenAbsentRequestParam() throws Exception {
        mockMvc.perform(get(API_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Request content")))
                .andExpect(jsonPath("$.message", is(
                        "Required request parameter 'nameFilter' for method parameter type String is not present")));

        verifyNoInteractions(repository);
    }

    @Test
    void getProducts_shouldReturnBadRequest_whenIncorrectQueryKey() throws Exception {
        mockMvc.perform(get(API_PATH).param(INCORRECT_QUERY_KEY, VALID_PATTERN))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Request content")))
                .andExpect(jsonPath("$.message", is(
                        "Required request parameter 'nameFilter' for method parameter type String is not present")));

        verifyNoInteractions(repository);
    }

    @Test
    void getProducts_shouldReturnInternalServerError_whenUnexpectedExceptionThrown() throws Exception {
        when(repository
                .findAll(expectedRequest))
                .thenThrow(RuntimeException.class);

        mockMvc.perform(get(API_PATH).param(QUERY_KEY, VALID_PATTERN))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Unexpected error")));

        verify(repository, only()).findAll(expectedRequest);
    }
}

