package ua.com.zmike.product.service.util;

import org.apache.logging.log4j.util.Strings;

public class TestConstants {
    public static final int ACTUAL_COUNT_OF_ITEM_IN_TEST_DB = 40;

    public static final int QUERY_PAGE_SIZE = 100;
    public static final int FIRST_PAGE_NUMBER = 0;

    public static final String API_PATH = "/shop/product";
    public static final String INCORRECT_API_PATH = "/shop/test";

    public static final String QUERY_KEY = "nameFilter";
    public static final String INCORRECT_QUERY_KEY = "filter";

    public static final String TEST_PATTERN = "test";

    public static final String VALID_PATTERN = "^[^A].*$";
    public static final String BROKEN_PATTERN = "^^^^^";
    public static final String GLOBAL_PATTERN = ".*";
    public static final String EMPTY_PATTERN = Strings.EMPTY;

    public static final String EMPTY_JSON_CONTENT = "[]";
    public static final String NAME_TO_BE_RECEIVED_WITH_VALID_PATTERN = "Aaaa";

}
