package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;

public class TestBase {

	public static ExtentReports reports;
	public static String guestTokenBody;
	public static ExtentHtmlReporter htmlReporter;
	private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
	public static ThreadLocal<ExtentTest> testInfo = new ThreadLocal<ExtentTest>();

	public static String baseUrl = System.getProperty("instance-url", "https://swapi.dev");
	public static String recipients;


	@Parameters({ "groupReport", "dataEnv" })
	@BeforeSuite
	public void setUp(String groupReport, String dataEnv) throws IOException, ParseException {
		htmlReporter = new ExtentHtmlReporter(new File(System.getProperty("user.dir") + groupReport));
		reports = new ExtentReports();
		reports.attachReporter(htmlReporter);
		//guestTokenBody = getToken(dataEnv, "guestTokenBody");
		reports.setSystemInfo("Test Environment", baseUrl);
		reports.attachReporter(htmlReporter);
	}

	@BeforeClass
	public synchronized void beforeClass() {
		ExtentTest parent = reports.createTest(getClass().getName());
		parentTest.set(parent);

	}

	@BeforeMethod
	public synchronized void beforeMethod(Method method) {
		ExtentTest child = ((ExtentTest) parentTest.get()).createNode(method.getName());
		testInfo.set(child);
	}

	@AfterMethod
	public synchronized void afterMethod(ITestResult result) {

		for (String group : result.getMethod().getGroups())
			testInfo.get().assignCategory(group);

		if (result.getStatus() == ITestResult.FAILURE)
			testInfo.get().fail(result.getThrowable());
		else if (result.getStatus() == ITestResult.SKIP)
			testInfo.get().skip(result.getThrowable());
		else
			testInfo.get().pass("Test passed");

		reports.flush();
	}

	@Parameters({ "toMails", "groupReport" })
	@AfterSuite(description = "clean up report after test suite")
	public void cleanup(String toMails, String groupReport) {
		reports.flush();
	}

    }