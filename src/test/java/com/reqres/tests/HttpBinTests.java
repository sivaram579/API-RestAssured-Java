package com.reqres.tests;

import com.aventstack.extentreports.Status;
import com.reqres.base.BaseTest;
import com.reqres.config.Config;
import com.reqres.utils.ApiUtils;
import com.reqres.utils.TestDataProvider;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class HttpBinTests extends BaseTest {

    @Test(description = "Test GET request", groups = {"smoke", "regression"})
    public void testGetRequest() {
        Response response = ApiUtils.get(Config.GET_ENDPOINT);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        getTest().log(Status.PASS, "Status code is 200");
        
        String url = response.jsonPath().getString("url");
        Assert.assertTrue(url.contains(Config.GET_ENDPOINT));
        getTest().log(Status.PASS, "Response URL contains expected endpoint");
    }

    @Test(description = "Test POST request with different data", 
          dataProvider = "postTestData", 
          dataProviderClass = TestDataProvider.class,
          groups = "regression")
    public void testPostRequestWithData(String name, int value, String description) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("value", value);
        
        Response response = ApiUtils.post(Config.POST_ENDPOINT, requestBody);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        getTest().log(Status.PASS, "Status code is 200");
        
        String json = response.jsonPath().getString("json");
        Assert.assertTrue(json.contains(name) && json.contains(String.valueOf(value)));
        getTest().log(Status.PASS, "Response contains expected data");
    }

    @Test(description = "Test GET request with query parameters",
          dataProvider = "queryParams",
          dataProviderClass = TestDataProvider.class,
          groups = "regression")
    public void testGetRequestWithQueryParams(String paramName, String paramValue, int expectedStatus) {
        Response response = given()
                .spec(requestSpec)
                .queryParam(paramName, paramValue)
                .when()
                .get(Config.GET_ENDPOINT);
        
        Assert.assertEquals(response.getStatusCode(), expectedStatus);
        getTest().log(Status.PASS, "Status code is " + expectedStatus);
        
        String args = response.jsonPath().getString("args." + paramName);
        Assert.assertEquals(args, paramValue);
        getTest().log(Status.PASS, "Query parameter verified in response");
    }

    @Test(description = "Test request with custom headers",
          dataProvider = "headers",
          dataProviderClass = TestDataProvider.class,
          groups = "regression")
    public void testRequestWithHeaders(String headerName, String headerValue) {
        Response response = given()
                .spec(requestSpec)
                .header(headerName, headerValue)
                .when()
                .get(Config.GET_ENDPOINT);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        getTest().log(Status.PASS, "Status code is 200");
        
        // Get the header value from the response JSON
        // The header name is capitalized with hyphens in the response
        String[] parts = headerName.split("-");
        StringBuilder capitalizedHeader = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) capitalizedHeader.append("-");
            capitalizedHeader.append(parts[i].substring(0, 1).toUpperCase())
                          .append(parts[i].substring(1));
        }
        String receivedHeader = response.jsonPath().getString("headers." + capitalizedHeader.toString());
        Assert.assertEquals(receivedHeader, headerValue);
        getTest().log(Status.PASS, "Custom header verified in response");
    }

    @Test(description = "Test request with gzip compression", groups = "regression")
    public void testGzipCompression() {
        Response response = given()
                .spec(requestSpec)
                .header("Accept-Encoding", "gzip")
                .when()
                .get(Config.GET_ENDPOINT);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        getTest().log(Status.PASS, "Status code is 200");
        
        String encoding = response.getHeader("Content-Encoding");
        if (encoding != null) {
            Assert.assertEquals(encoding, "gzip");
            getTest().log(Status.PASS, "Response is gzip compressed");
        } else {
            getTest().log(Status.PASS, "Response was not compressed due to small size");
        }
    }

    @Test(description = "Test request timeout", timeOut = 5000, groups = {"smoke", "regression"})
    public void testRequestTimeout() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get(Config.GET_ENDPOINT);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 5000);
        getTest().log(Status.PASS, "Response time is within acceptable limit: " + response.getTime() + "ms");
    }

    @Test(description = "Test PUT request")
    public void testPutRequest() {
        String requestBody = "{\"name\":\"updated\",\"value\":456}";
        
        Response response = ApiUtils.put(Config.PUT_ENDPOINT, requestBody);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        getTest().log(Status.PASS, "Status code is 200");
        
        String json = response.jsonPath().getString("json");
        Assert.assertTrue(json.contains("updated") && json.contains("456"));
        getTest().log(Status.PASS, "Response contains updated data");
    }

    @Test(description = "Test DELETE request")
    public void testDeleteRequest() {
        Response response = ApiUtils.delete(Config.DELETE_ENDPOINT);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        getTest().log(Status.PASS, "Status code is 200");
        
        String url = response.jsonPath().getString("url");
        Assert.assertTrue(url.contains(Config.DELETE_ENDPOINT));
        getTest().log(Status.PASS, "Response URL contains expected endpoint");
    }

    @Test(description = "Test Basic Authentication", groups = {"smoke", "regression"})
    public void testBasicAuth() {
        String endpoint = Config.BASIC_AUTH_ENDPOINT
                .replace("{user}", Config.BASIC_AUTH_USER)
                .replace("{passwd}", Config.BASIC_AUTH_PASS);
        
        Response response = given()
                .auth()
                .basic(Config.BASIC_AUTH_USER, Config.BASIC_AUTH_PASS)
                .when()
                .get(endpoint);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        getTest().log(Status.PASS, "Status code is 200");
        
        boolean authenticated = response.jsonPath().getBoolean("authenticated");
        Assert.assertTrue(authenticated);
        getTest().log(Status.PASS, "Authentication successful");
    }

    @Test(description = "Test Bearer Token Authentication", groups = {"smoke", "regression"})
    public void testBearerAuth() {
        Response response = given()
                .header("Authorization", "Bearer " + Config.BEARER_TOKEN)
                .when()
                .get(Config.BEARER_AUTH_ENDPOINT);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        getTest().log(Status.PASS, "Status code is 200");
        
        String token = response.jsonPath().getString("token");
        Assert.assertEquals(token, Config.BEARER_TOKEN);
        getTest().log(Status.PASS, "Token matches expected value");
    }
} 