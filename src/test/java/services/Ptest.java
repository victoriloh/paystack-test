package services;
import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import utils.TestUtils;
import utils.TestBase;

public class Ptest extends TestBase {
    @Parameters({"dataEnv"})
    public void positiveTest (String envsValue, String message, String dataEnv, int sCode) throws FileNotFoundException, IOException, ParseException {

        RestAssured.baseURI = baseUrl;

        JSONParser parser = new JSONParser();
        JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/"+dataEnv+"/Ptest.json"));
        JSONObject envs = (JSONObject) config.get(envsValue);

        String endpoint = (String) config.get("endpoint");
        String name = (String) envs.get("name");
        String complete = (String) envs.get("complete");

        TestUtils.testTitle("<b>ENDPOINT</b>");
        testInfo.get().info(baseUrl + endpoint);

        TestUtils.testTitle("<b>DESCRIPTION</b>");
        testInfo.get().info(message);

        TestUtils.testTitle("<b>REQUEST BODY</b>");
        testInfo.get().info("<b> name: </b> "+name);
        testInfo.get().info("<b> Completed: </b> " +complete);

        Response res = given().
                header("User-Agent","PostmanRuntime/7.29.0").
                header("Connection","keep-alive").
                header("Content-Type","application/json").
                header("Accept-Encoding","gzip, deflate, br").
                header("Accept","*/*").
                when().get(endpoint).then().assertThat().extract().response();

        TestUtils.testTitle("<b>RESPONSE BODY</b>");
        String response = res.asString();
        testInfo.get().info(MarkupHelper.createCodeBlock(response));

        int statusCode = res.getStatusCode();
        Assert.assertEquals(statusCode, sCode);

        JsonPath jsonRes = new JsonPath(response);
        String message1 = jsonRes.getString("name");
        Assert.assertEquals(message1, message);

    }

    @Parameters({"dataEnv"})
    @Test (groups = { "Regression","Health Check" })
    public void positiveScenerio (String dataEnv) throws FileNotFoundException, IOException, ParseException {

        positiveTest("PositiveTest","Yavin IV", dataEnv, 200);

    }

    @Parameters({"dataEnv"})
    public void apiNegativeTest (String envsValue, String message, String dataEnv, int sCode) throws FileNotFoundException, IOException, ParseException {

        RestAssured.baseURI = baseUrl;

        JSONParser parser = new JSONParser();
        JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/"+dataEnv+"/Ptest.json"));
        JSONObject envs = (JSONObject) config.get(envsValue);

        String endpoint = (String) config.get("endpoint");
        String name = (String) envs.get("name");
        String complete = (String) envs.get("complete");

        TestUtils.testTitle("<b>ENDPOINT</b>");
        testInfo.get().info(baseUrl + endpoint);

        TestUtils.testTitle("<b>DESCRIPTION</b>");
        testInfo.get().info(message);

        TestUtils.testTitle("<b>REQUEST BODY</b>");
        testInfo.get().info("<b> name: </b> "+name);
        testInfo.get().info("<b> Completed: </b> " +complete);

        Response res = given().
                header("User-Agent","PostmanRuntime/7.29.0").
                header("Connection","keep-alive").
                header("Content-Type","application/json").
                header("Accept-Encoding","gzip, deflate, br").
                header("Accept","*/*").param("name",name).param("completed",complete).
                when().post(endpoint).then().assertThat().extract().response();

        TestUtils.testTitle("<b>RESPONSE BODY</b>");
        String response = res.asString();
        testInfo.get().info(MarkupHelper.createCodeBlock(response));

        int statusCode = res.getStatusCode();
        Assert.assertEquals(statusCode, sCode);

        JsonPath jsonRes = new JsonPath(response);
        String message1 = jsonRes.getString("detail");
        Assert.assertEquals(message1, message);

    }

    @Parameters({"dataEnv"})
    @Test (groups = { "Regression","Health Check" })
    public void negativeTest (String dataEnv) throws FileNotFoundException, IOException, ParseException {

        apiNegativeTest("negativeTest","Method 'POST' not allowed.", dataEnv, 405);

    }

    }