package ua.com.zmike.product.service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.zmike.product.service.util.TestConstants.API_PATH;
import static ua.com.zmike.product.service.util.TestConstants.EMPTY_JSON_CONTENT;
import static ua.com.zmike.product.service.util.TestConstants.QUERY_KEY;
import static ua.com.zmike.product.service.util.TestConstants.VALID_PATTERN;

@SpringBootTest
@ActiveProfiles("it-test")
@AutoConfigureMockMvc
class ProductControllerSysTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/db/clean-db.sql", "/db/fill-db.sql"})
    void getProducts_shouldReturnFilteredData_whenSuitableEntityExistInDb_testRegex1() throws Exception {
        var nameFilter = "^[^D].*";
        var expectedResponse = "[" +
                "{\"id\":29,\"name\":\"D D\",\"description\":\"\"}," +
                "{\"id\":18,\"name\":\"DDD\",\"description\":\"\"}," +
                "{\"id\":17,\"name\":\"DD\",\"description\":\"\"}," +
                "{\"id\":20,\"name\":\"DDDDD\",\"description\":\"\"}," +
                "{\"id\":33,\"name\":\"Dd\",\"description\":\"\"}," +
                "{\"id\":16,\"name\":\"D\",\"description\":\"\"}," +
                "{\"id\":19,\"name\":\"DDDD\",\"description\":\"\"}]";

        var response = mockMvc
                .perform(get(API_PATH).param(QUERY_KEY, nameFilter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);
    }

    @Test
    @Rollback
    @Sql({"/db/clean-db.sql", "/db/fill-db.sql"})
    void getProducts_shouldReturnFilteredData_whenSuitableEntityExistInDb_testRegex2() throws Exception {
        var nameFilter = ".{1,4}";
        var expectedResponse = "[" +
                "{\"id\":10,\"name\":\"BBBBB\",\"description\":\"\"}," +
                "{\"id\":15,\"name\":\"CCCCC\",\"description\":\"\"}," +
                "{\"id\":38,\"name\":\"abcd1\",\"description\":\"\"}," +
                "{\"id\":20,\"name\":\"DDDDD\",\"description\":\"\"}," +
                "{\"id\":5,\"name\":\"AAAAA\",\"description\":\"\"}]";

        var response = mockMvc
                .perform(get(API_PATH).param(QUERY_KEY, nameFilter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);
    }

    @Test
    @Rollback
    @Sql({"/db/clean-db.sql", "/db/fill-db.sql"})
    void getProducts_shouldReturnFilteredData_whenSuitableEntityExistInDb_testRegex3() throws Exception {
        var nameFilter = "(A|B|C|D).*";
        var expectedResponse = "[" +
                "{\"id\":36,\"name\":\"cc\",\"description\":\"\"}," +
                "{\"id\":34,\"name\":\"aa\",\"description\":\"\"}," +
                "{\"id\":39,\"name\":\"bcd2\",\"description\":\"\"}," +
                "{\"id\":37,\"name\":\"dd\",\"description\":\"\"}," +
                "{\"id\":38,\"name\":\"abcd1\",\"description\":\"\"}," +
                "{\"id\":35,\"name\":\"bb\",\"description\":\"\"}," +
                "{\"id\":40,\"name\":\"cd3\",\"description\":\"\"}]";

        var response = mockMvc
                .perform(get(API_PATH).param(QUERY_KEY, nameFilter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);
    }

    @Test
    @Sql("/db/clean-db.sql")
    void getProducts_shouldReturnEmpty_dbIsEmpty() throws Exception {
        var response = mockMvc
                .perform(get(API_PATH).param(QUERY_KEY, VALID_PATTERN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString()).isEqualTo(EMPTY_JSON_CONTENT);
    }
}
