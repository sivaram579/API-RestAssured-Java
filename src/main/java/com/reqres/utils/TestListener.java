package com.reqres.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.reqres.config.Config;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    @Override
    public void onStart(ITestContext context) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportName = "Test-Report-" + timeStamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(Config.REPORT_PATH + reportName);
        extent = new ExtentReports();
        extent.attachReporter(spark);
        
        // Add system info
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Environment", "Test");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test passed");
        addTestDetails(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test failed");
        test.get().log(Status.FAIL, result.getThrowable());
        addTestDetails(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test skipped");
        test.get().log(Status.SKIP, result.getThrowable());
        addTestDetails(result);
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
    
    private void addTestDetails(ITestResult result) {
        // Add test duration
        long duration = result.getEndMillis() - result.getStartMillis();
        test.get().info("Test Duration: " + duration + " ms");
        
        // Add test groups
        for (String group : result.getMethod().getGroups()) {
            test.get().assignCategory(group);
        }
        
        // Add test parameters if any
        if (result.getParameters().length > 0) {
            for (Object parameter : result.getParameters()) {
                test.get().info("Parameter: " + parameter.toString());
            }
        }
    }
} 