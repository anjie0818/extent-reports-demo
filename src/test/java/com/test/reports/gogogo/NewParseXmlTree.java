package com.test.reports.gogogo;


import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.swing.text.View;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 遍历xml所有节点（包括子节点下还有子节点多层嵌套）
 */
public class NewParseXmlTree {

    public static void main(final String[] args) throws Exception {
        final NewParseXmlTree test = new NewParseXmlTree();
        test.testGetRoot();
    }

    /**
     * 获取文件的xml对象，然后获取对应的根节点root
     */
    public void testGetRoot() throws Exception {
        //读nodes
        final SAXReader sax = new SAXReader();// 创建一个SAXReader对象
        final File xmlFile = new File("/Users/anjie/dev/Intellij Idea/extent-reports-demo/20210516-test.jtl.xml");// 根据指定的路径创建file对象
        final Document document = sax.read(xmlFile);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
        final Element root = document.getRootElement();// 获取根节点
        final List<Element> listElement = root.elements();// 所有一级子节点的list
        // 解析xml
        ExtentReports extent = new ExtentReports();
        extent.setReportUsesManualConfiguration(true);
        ExtentSparkReporter spark = new ExtentSparkReporter("/Users/anjie/dev/Intellij Idea/extent-reports-demo/20210516-test.jtl.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG,
                        ViewName.CATEGORY
                })
                .apply();
        extent.attachReporter(spark);
        /**
         *         ExtentTest test1 = extent.createTest("MyTest1");
         *
         */
        for (final Element node : listElement) {// 遍历所有一级子节点,作为每一个测试case
            ExtentTest testCase = extent.createTest(node.attributeValue("lb"));
            String result = node.attributeValue("s");
            if (result.equals("true")){
                testCase.pass("执行通过");
            }else if (result.equals("false")){
                testCase.fail("执行失败");
            }
            setTestCaseTimerStamp(testCase,node);
            //递归node节点下所有节点，如果是httpsample作为testCase的子节点
            sample2TestCase(testCase,node);
        }
        extent.flush();
    }
    private static void setTestCaseTimerStamp(ExtentTest testCase,Element node){
        //获取sample执行时间
        String timeStamp = node.attributeValue("ts");//ts="1621679238696"
        Date startDate = new Date(Long.parseLong(String.valueOf(timeStamp)));
        testCase.getModel().setStartTime(startDate);
        String timeDiff = node.attributeValue("t");
        long entTimeStamp = Long.parseLong(timeStamp)+Long.parseLong(timeDiff);
        Date endDate = new Date(entTimeStamp);
        testCase.getModel().setEndTime(endDate);
    }
    private static String toHeaderFormat(String headerStr){
        return headerStr.replaceAll("\\n","<br>");
    }
    private static String toJsonFormat(String json) {
        String result = "";
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String tmp = gson.toJson(jsonObject);
            result = "<textarea readonly=\"\" class=\"code-block\">"+tmp+"</textarea>";
        }catch (Exception e){
            result = json;
        }

        return result;
    }
    private void sample2TestCase(ExtentTest testCase, Element node) {

        if (node.getName().equals("httpSample")){
            /**
             * 遍历httpsample子节点获取相关参数
             */
            String requestUrl = "";
            String httpMethod = "";
            String queryString = "";
            String responseData = "";
            String requestHeader = "";
            String responseHeader = "";
            for (final Element e1 : node.elements()) {// 遍历所有httpsamile的子节点
                if (e1.getName().equals("java.net.URL")){
                    requestUrl = e1.getTextTrim();
                };
                if (e1.getName().equals("method")){
                    httpMethod = e1.getTextTrim();
                };
                if (e1.getName().equals("requestHeader")){
                    requestHeader = e1.getText();
                };
                if (e1.getName().equals("responseHeader")){
                    responseHeader = e1.getText();
                };
                if (e1.getName().equals("queryString")){
                    queryString = e1.getTextTrim();
                };
                if (e1.getName().equals("responseData")){
                    responseData = e1.getTextTrim();
                };
            }
            ExtentTest subCase = testCase.createNode(node.attributeValue("lb")+" "+"<span class='badge badge-primary'>"+httpMethod+"</span>");
            String result = node.attributeValue("s");
            if (result.equals("true")){
                subCase.pass("执行通过");
            }else if (result.equals("false")){
                subCase.fail("执行失败");
            }
            //设置时间
            setTestCaseTimerStamp(subCase,node);
            subCase.info("<strong>requestUrl</strong>:<br>"+requestUrl);
            subCase.info("<strong>requestHeader</strong>:<br>"+toHeaderFormat(requestHeader));
            subCase.info("<strong>queryString</strong>:<br>"+toJsonFormat(queryString));
            subCase.info("<strong>responseHeader</strong>:<br>"+toHeaderFormat(responseHeader));
            subCase.info("<strong>responseData</strong>:<br>"+toJsonFormat(responseData));

        }

        // 递归遍历当前节点所有的子节点
        final List<Element> listElement = node.elements();// 所有一级子节点的list
        for (final Element e2 : listElement) {// 遍历所有一级子节点
            sample2TestCase(testCase,e2);// 递归
        }
    }

}