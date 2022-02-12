package utils;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils extends TestBase {
	
	public static void testTitle(String phrase) {
		String word = "<b>"+phrase+"</b>";
        Markup w = MarkupHelper.createLabel(word, ExtentColor.BLUE);
        testInfo.get().info(w);
	}
	
	
	@Parameters({"dataEnv"})
	public static JsonObject generateJson(String dataEnv, String filename, String testCase) throws IOException {
        
		String filePath = "src/test/resources/"+dataEnv+"/"+filename;
		
        JsonObject object = new Gson().fromJson(new String(Files.readAllBytes(Paths.get(filePath))),JsonObject.class);
        JsonObject object2 = (JsonObject) object.get(testCase);
        
        return object2;
        
	}


	@Parameters({"dataEnv"})
	public static JsonArray generateJsonArray(String dataEnv, String filename , String testCase) throws IOException {

		String filePath = "src/test/resources/" + dataEnv + "/" + filename;

		JsonObject object = new Gson().fromJson(new String(Files.readAllBytes(Paths.get(filePath))), JsonObject.class);
		JsonArray object2 = (JsonArray) object.get(testCase);

		return object2;
	}





}
