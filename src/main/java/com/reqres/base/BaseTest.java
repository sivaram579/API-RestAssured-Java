package com.reqres.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.reqres.config.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    protected static ExtentReports extent;
    protected static RequestSpecification requestSpec;

    @BeforeSuite(alwaysRun = true)
    public void setup() {
        // Create reports directory if it doesn't exist
        File reportsDir = new File(Config.REPORT_PATH);
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        // Initialize ExtentReports
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportName = "Test-Report-" + timeStamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(Config.REPORT_PATH + reportName);
        extent = new ExtentReports();
        extent.attachReporter(spark);
        
        // Add system info
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Environment", "Test");

        // Setup RestAssured
        RestAssured.baseURI = Config.BASE_URL;
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestResult result) {
        String testName = result.getMethod().getDescription();
        if (testName == null || testName.isEmpty()) {
            testName = result.getMethod().getMethodName();
        }
        // Create a more descriptive test name that includes the class name and test description
        String fullTestName = String.format("%s - %s", 
            result.getTestClass().getRealClass().getSimpleName(),
            testName);
        ExtentTest test = extent.createTest(fullTestName);
        extentTest.set(test);
    }

    protected ExtentTest getTest() {
        return extentTest.get();
    }

    @AfterMethod(alwaysRun = true)
    public void getResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            getTest().fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            getTest().pass("Test passed");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        if (extent != null) {
            extent.flush();
        }
    }
} 