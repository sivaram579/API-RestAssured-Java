package com.reqres.utils;

import org.testng.annotations.DataProvider;

public class TestDataProvider {
    
    @DataProvider(name = "postTestData")
    public static Object[][] postTestData() {
        return new Object[][] {
            {"test1", 123, "Test case 1"},
            {"test2", 456, "Test case 2"},
            {"test3", 789, "Test case 3"}
        };
    }
    
    @DataProvider(name = "queryParams")
    public static Object[][] queryParams() {
        return new Object[][] {
            {"name", "value", 200},
            {"param1", "test", 200},
            {"key", "123", 200}
        };
    }
    
    @DataProvider(name = "headers")
    public static Object[][] headers() {
        return new Object[][] {
            {"custom-header1", "value1"},
            {"custom-header2", "value2"},
            {"custom-header3", "value3"}
        };
    }
} 