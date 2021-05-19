package com.test.reports;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.gherkin.model.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class ReportsTest {

    @Test
    public void firstTest() {

        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark.html");
        extent.attachReporter(spark);
        ExtentTest test1 = extent.createTest("MyTest1");
        test1.log(Status.PASS, "This is a logging event for MyFirstTest, and it passed!");
        ExtentTest test2 = extent.createTest("MyTest2");
        test2.log(Status.FAIL, "This is a logging event for MyFirstTest, and it passed!");
        extent.flush();
    }

    @Test
    public void filterReportsTest() {
        ExtentReports extent = new ExtentReports();
        // will only contain failures
        ExtentSparkReporter sparkFail = new ExtentSparkReporter("target/SparkFail.html")
                .filter()
                .statusFilter()
                .as(new Status[]{Status.FAIL})
                .apply();
        // will contain all tests
        ExtentSparkReporter sparkAll = new ExtentSparkReporter("target/SparkAll.html");
        extent.attachReporter(sparkFail, sparkAll);

        ExtentTest test1 = extent.createTest("MyTest1");
        test1.log(Status.PASS, "This is a logging event for MyFirstTest, and it passed!");
        ExtentTest test2 = extent.createTest("MyTest2");
        test2.log(Status.FAIL, "This is a logging event for MyFirstTest, and it passed!");
        extent.flush();
    }
    @Test
    public void orderAndViewTest1() {
        ExtentReports extent = new ExtentReports();
        // will only contain failures
        ExtentSparkReporter spark = new ExtentSparkReporter("target/OrderAndView1.html")
               .viewConfigurer()
                .viewOrder().as(new ViewName[]{ViewName.DASHBOARD,ViewName.TEST})
                .apply();
        extent.attachReporter(spark);
        ExtentTest test1 = extent.createTest("MyTest1");
        test1.log(Status.PASS, "This is a logging event for MyFirstTest, and it passed!");
        ExtentTest test2 = extent.createTest("MyTest2");
        test2.log(Status.FAIL, "This is a logging event for MyFirstTest, and it passed!");
        extent.flush();
    }
    @Test
    public void orderAndViewTest2() {
        ExtentReports extent = new ExtentReports();
        // will only contain failures
        ExtentSparkReporter spark = new ExtentSparkReporter("target/OrderAndView2.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extent.attachReporter(spark);
        ExtentTest test1 = extent.createTest("MyTest1");
        test1.log(Status.PASS, "This is a logging event for MyFirstTest, and it passed!");
        ExtentTest test2 = extent.createTest("MyTest2");
        test2.log(Status.FAIL, "This is a logging event for MyFirstTest, and it passed!");
        extent.flush();
    }
    @Test
    public void createTest(){
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/CreateTest.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extent.attachReporter(spark);

        extent.createTest("MyFirstTest").pass("Pass by anjie");
        extent.flush();
    }
    @Test
    public void createNode(){
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/CreateNode.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extent.attachReporter(spark);

        ExtentTest test = extent.createTest("MyFirstTest");
        ExtentTest f1a = test.createNode("f1a");
        f1a.pass("f1a pass");
        ExtentTest f2a = f1a.createNode("F2a");
        ExtentTest f2b = f1a.createNode("F2b");
        ExtentTest f3a = f2b.createNode("f3a");
        f3a.pass("f3a pass");


        extent.flush();
    }
    // test ???????????
    @Test
    public void BDDTest(){
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/BDDTest.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extent.attachReporter(spark);

        ExtentTest feature = extent.createTest(Feature.class, "Refund item");
        ExtentTest scenario = feature.createNode(Scenario.class, "Jeff returns a faulty microwave");
        scenario.createNode(Given.class, "Jeff has bought a microwave for $100").pass("pass");
        scenario.createNode(And.class, "he has a receipt").pass("pass");
        scenario.createNode(When.class, "he returns the microwave").pass("pass");
        scenario.createNode(Then.class, "Jeff should be refunded $100").fail("fail");

        extent.flush();
    }
    // test ???????????
    @Test
    public void gherkinDialect() throws UnsupportedEncodingException, ClassNotFoundException {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/GherkinDialect.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extent.attachReporter(spark);

        extent.setGherkinDialect("de");

        ExtentTest feature = extent.createTest(new GherkinKeyword("Funktionalität"), "Refund item VM");
        ExtentTest scenario = feature.createNode(new GherkinKeyword("Szenario"), "Jeff returns a faulty microwave");
        ExtentTest given = scenario.createNode(new GherkinKeyword("Angenommen"), "Jeff has bought a microwave for $100").skip("skip");


        extent.flush();
    }
    @Test
    public void removeTest() throws UnsupportedEncodingException, ClassNotFoundException {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/RemoveTest.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extent.attachReporter(spark);

        ExtentTest test = extent.createTest("Test").fail("reason");
//        extent.removeTest(test);

        // or remove using test name
//        extent.removeTest("Test");

        extent.flush();
    }
    @Test
    public void removeNode() throws UnsupportedEncodingException, ClassNotFoundException {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/RemoveNode.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extent.attachReporter(spark);

        ExtentTest test = extent.createTest("Test");
        ExtentTest node = test.createNode("Node").fail("reason");
//        extent.removeTest(node);

        // or remove using test name
//        extent.removeTest("node");


        extent.flush();
    }
    @Test
    public void testDetails() throws UnsupportedEncodingException, ClassNotFoundException {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/Details.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extent.attachReporter(spark);

        ExtentTest test = extent.createTest("MyFirstTest");
        test.pass("Text details");
        test.log(Status.PASS, "Text details");

        extent.flush();
    }
    @Test
    public void testExceptions() throws UnsupportedEncodingException, ClassNotFoundException {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/Exceptions.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extent.attachReporter(spark);

        Throwable t = new RuntimeException("A runtime exception");
        ExtentTest test = extent.createTest("MyFirstTest");
        test.fail(t);
        test.log(Status.FAIL, t);


        extent.flush();
    }
    @Test
    public void testScreenshots() throws UnsupportedEncodingException, ClassNotFoundException {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/Screenshots.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extent.attachReporter(spark);

        ExtentTest test = extent.createTest("MyFirstTest");

        // reference image saved to disk
        test.fail(MediaEntityBuilder.createScreenCaptureFromPath("img.png").build());
        // base64
        test.fail(MediaEntityBuilder.createScreenCaptureFromBase64String("base64").build());

        extent.flush();
    }



}
