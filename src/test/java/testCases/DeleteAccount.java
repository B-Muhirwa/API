package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class DeleteAccount extends Authentication{
	String baseURI;
	String oneAccountEndPoint;
	String headerContentType;
	String deleteAccountEndpoint;
	String deleteAccountId;
	
	public DeleteAccount() {
		baseURI = getProperty("baseUri");
		headerContentType = getProperty("header_content_Type");
		deleteAccountEndpoint = getProperty("deleteAccountEndpoint");
		deleteAccountId="809";
		oneAccountEndPoint=getProperty("getOneAccountEndpoint");
	}
	@Test(priority=1)
	public void deleteOneAccount() {
		/* given: all input details ->(baseURI, Headers,Authorization,Playload/Body,QueryParameters)
		 * when: submit api requests -> HttpMethod(Endpoint/Resource)
		 * then: validate response -> (status code, Headers, responseTime, Playload/Body)
		 */
		Response resp =
			given()
			       .baseUri(baseURI)
			       .header("Content-Type",headerContentType)
			       .header("Authorization",generateBearerToken())
			       .queryParam("account_id", deleteAccountId)
			       .log().all().
			       
			 when()
			       .delete(deleteAccountEndpoint).
			 then()
			        .log().all()
			        .extract().response();
		
		int statusCode = resp.getStatusCode();
		System.out.println("Status code:" + statusCode);
		Assert.assertEquals(statusCode, 200, "Status codes are Not matching");
		
		String responseBody = resp.getBody().asString();
		System.out.println("Response Body :" + responseBody);
		String responseHeaderContentType= resp.getHeader("Content-Type");
		Assert.assertEquals(responseHeaderContentType, headerContentType);
		JsonPath jpath = new JsonPath(responseBody);
		
		String deleteAccountMsg = jpath.getString("message");
		System.out.println("Confirmation Message :" +deleteAccountMsg);
		Assert.assertEquals(deleteAccountMsg, "Account deleted successfully.");
	}
	@Test(priority=2)
	public void getOneAccount() {
		
		Response resp =  
				given()
				       .baseUri(baseURI)
				       .header("Content-Type", headerContentType)
				       .header("Authorization", generateBearerToken())
				       .queryParam("account_id", deleteAccountId)
				       .log().all().
				 when()
				       .get(oneAccountEndPoint).
				       
				 then()
				       .log().all()
				       .extract().response();
		
		int statusCode = resp.getStatusCode();
		System.out.println("Status code:" + statusCode);
		Assert.assertEquals(statusCode, 404, "Status codes are Not matching");
		String responseBody = resp.getBody().asString();
		JsonPath jpath = new JsonPath(responseBody);
		String confirmationMsg =jpath.getString("message");
		Assert.assertEquals(confirmationMsg, "No Record Found");
	
	
		
	}
}
