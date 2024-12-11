package testCases;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateAccount extends Authentication{
	String baseURI;
	String createAccountEndPoint; 
	String createAccountFilePath;
	String oneAccountEndPoint; 
	String headerContentType;
	String allAccountEndPoint; 
	String firstAccountId;

	
	public CreateAccount() {
		baseURI= getProperty("baseUri");
		createAccountEndPoint= getProperty("createAccountEndPoint");
		headerContentType = getProperty("header_content_Type");
		createAccountFilePath = "src\\main\\java\\data\\createAccountPlayload.json";
		allAccountEndPoint = getProperty("getAllAccountEndpoint");
		oneAccountEndPoint = getProperty("getOneAccountEndpoint");
	}
	@Test(priority=1)
	public void createAccount() {
		/* given: all input details ->(baseURI, Headers,Authorization,Playload/Body,QueryParameters)
		 * when: submit api requests -> HttpMethod(Endpoint/Resource)
		 * then: validate response -> (status code, Headers, responseTime, Playload/Body)
		 */
		Response resp=
				given()
				       .baseUri(baseURI)
				       .header("Content-Type", headerContentType)
				       .header("Authorization", generateBearerToken())
				       .body(new File(createAccountFilePath)).
				 when()
				       .post(createAccountEndPoint).
				       
				  then()
				       .log().all()
				       .extract().response();
		 
		int statusCode = resp.getStatusCode();
		System.out.println("Status code:" + statusCode);
		Assert.assertEquals(statusCode, 201, "Status codes are Not matching");
	
		String responseHeaderContentType= resp.getHeader("Content-Type");
		Assert.assertEquals(responseHeaderContentType, headerContentType);
		String responseBody = resp.getBody().asString();

		JsonPath jp = new JsonPath(responseBody);
		String actualCreatedAccountMessage =jp.get("message");
		Assert.assertEquals(actualCreatedAccountMessage,"Account created successfully.", "Account was Not Created");
		
	} 
	@Test(priority=2)
	public void getAllAccounts() {
		
	Response resp =
			given()
		     	.baseUri(baseURI)
		     	.header("Content-Type", headerContentType)
		     	.auth().preemptive().oauth2(generateBearerToken())
		     	.log().all().
			when()
			      .get(allAccountEndPoint).
			then()
			      .log().all()
			      .extract().response();
	String responseBody = resp.getBody().asString();
	System.out.println("Response Body:" + responseBody);
	JsonPath jp = new JsonPath(responseBody);
	firstAccountId= jp.getString("records[0].account_id");
	System.out.println("Fisrt Account Id:" + firstAccountId);
	}
	@Test(priority=3)
	public void getOneAccount() {
		
		Response resp =  
				given()
				       .baseUri(baseURI)
				       .header("Content-Type", headerContentType)
				       .header("Authorization", generateBearerToken())
				       .queryParam("account_id", firstAccountId)
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
		System.out.println("Account Name:" +actualAccountName);
		String  actualAccountNumber = jp.getString("account_number");
		String actualBalance = jp.getString("balance");
		System.out.println("Account Number:"+ actualAccountNumber);
		String actualContactPerson =jp.getString("contact_person");
			
		JsonPath jp1 = new JsonPath(new File(createAccountFilePath));
		String expectedAccountName = jp1.getString("account_name");
		String  expectedAccountNumber = jp1.getString("account_number");
		String expectedBalance = jp1.getString("balance");
		String expectedContactPerson =jp1.getString("contact_person");
		
		Assert.assertEquals(actualAccountName, expectedAccountName, "Account Name Not Found");
		Assert.assertEquals(actualAccountNumber,expectedAccountNumber, "Account Number Not Matching");
		Assert.assertEquals(actualBalance,expectedBalance, "Account Balance Not Matching");
		Assert.assertEquals(actualContactPerson,expectedContactPerson, "Account Balance Not Matching");
	}


}
