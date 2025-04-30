package com.reqres.config;

import lombok.Getter;

@Getter
public class Config {
    public static final String BASE_URL = "http://httpbin.org";
    public static final String GET_ENDPOINT = "/get";
    public static final String POST_ENDPOINT = "/post";
    public static final String PUT_ENDPOINT = "/put";
    public static final String DELETE_ENDPOINT = "/delete";
    public static final String BASIC_AUTH_ENDPOINT = "/basic-auth/{user}/{passwd}";
    public static final String BEARER_AUTH_ENDPOINT = "/bearer";
    
    // Test Data Paths
    public static final String TEST_DATA_PATH = "src/test/resources/testdata/";
    
    // Report Path
    public static final String REPORT_PATH = "reports/extent-reports/";
    
    // Authentication
    public static final String BASIC_AUTH_USER = "user";
    public static final String BASIC_AUTH_PASS = "passwd";
    public static final String BEARER_TOKEN = "test-token";
} 