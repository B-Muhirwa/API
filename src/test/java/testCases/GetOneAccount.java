package testCases;

import io.restassured.response.Response;
import util.ConfigReader;
import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;

public class GetOneAccount extends ConfigReader{
	String baseURI;
	String oneAccountEndPoint; 
	String authFilePath;
	String headerContentType;
	long responseTime;

	public GetOneAccount() {
		baseURI = getProperty("baseUri");
		oneAccountEndPoint = getProperty("getOneAccountEndpoint");
		headerContentType= getProperty("header_content_Type");
	}
	@Test
	public void getOneAccount() {
		Authentication aunt = new Authentication();
		/* given: all input details ->(baseURI, Headers,Authorization,Playload/Body,QueryParameters)
		 * when: submit api requests -> HttpMethod(Endpoint/Resource)
		 * then: validate response -> (status code, Headers, responseTime, Playload/Body)
		 */
		Response resp =  
				given()
				       .baseUri(baseURI)
				       .header("Content-Type", headerContentType)
				       .header("Authorization", aunt.generateBearerToken())
				       .queryParam("account_id", 461)
				       .log().all().
				 when()
				       .get(oneAccountEndPoint).
				       
				 then()
				       .log().all()
				       .extract().response();
		
		int statusCode = resp.getStatusCode();
		System.out.println("Status code:" + statusCode);
		Assert.assertEquals(statusCode, 200, "Status codes are Not matching");
		responseTime = resp.timeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time:" + responseTime);
		String responseHeaderContentType= resp.getHeader("Content-Type");
		
		System.out.println("Header Content Type :" +responseHeaderContentType);
		Assert.assertEquals(responseHeaderContentType, headerContentType);
		String responseBody = resp.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String actualAccountName = jp.getString("account_name");
		String  actualAccountNumber = jp.getString("account_number");
		String actualBalance = jp.getString("balance");
		
		Assert.assertEquals(actualAccountName, "MD Techfios account 111", "Account Name Not Found");
		Assert.assertEquals(actualAccountNumber,"12345678", "Account Number Not Matching");
		Assert.assertEquals(actualBalance,"100.21", "Account Balance Not Matching");
		
	}

}
