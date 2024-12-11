package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Authentication extends ConfigReader{
	String baseURI;
	String authEndPoint; 
	String authFilePath;
	String headerContentType;
	long responseTime;
	String bearerToken;
	public Authentication() {
		baseURI= getProperty("baseUri");
		authEndPoint = getProperty("AuthEndpoint");
		headerContentType= getProperty("header_content_Type");
		authFilePath = "src\\main\\java\\data\\authPlayload.json";
	}
	public boolean validateResponseTime() {
		boolean withinRange =false;
		if(responseTime <=3000) {
			withinRange = true;
			System.out.println("Response time is within the range.");
		}else {
			System.out.println("Response time is out of range.");
		}
		return withinRange;
		
	}
	@Test
	public String generateBearerToken() {
		
		/* given: all input details ->(baseURI, Headers,Authorization,Playload/Body,QueryParameters)
		 * when: submit api requests -> HttpMethod(Endpoint/Resource)
		 * then: validate response -> (status code, Headers, responseTime, Playload/Body)
		 */
		Response resp =   
		given()
		   .baseUri(baseURI)
		   .header("Content-Type",headerContentType)
		   .body(new File(authFilePath))
		   .relaxedHTTPSValidation()
		   .log().all().
		when()
		   .post(authEndPoint).
		then()
		   .log().all()
		   .extract().response();
		
		int statusCode = resp.getStatusCode();
		System.out.println("Status code:" + statusCode);
		Assert.assertEquals(statusCode, 201, "Status codes are Not matching");
		responseTime = resp.timeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time:" + responseTime);
		Assert.assertEquals(validateResponseTime(), true);
		String responseHeaderContentType= resp.getHeader("Content-Type");
		System.out.println("Header Content Type :" +responseHeaderContentType);
		Assert.assertEquals(responseHeaderContentType, headerContentType);
		String responseBody = resp.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		 JsonPath jp = new JsonPath(responseBody);
		bearerToken = jp.getString("access_token"); 
		System.out.println("Bearer_Token:" + bearerToken);
		return bearerToken;
	
	}

}
