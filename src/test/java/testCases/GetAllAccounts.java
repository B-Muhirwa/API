package testCases;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;
import static io.restassured.RestAssured.given;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetAllAccounts extends ConfigReader{
	String baseURI;
	String allAccountEndPoint; 
	String headerContentType;
	long responseTime;
	String bearerToken;

	public GetAllAccounts() {
		baseURI= getProperty("baseUri");
		allAccountEndPoint = getProperty("getAllAccountEndpoint");
		headerContentType= getProperty("header_content_Type");
		
	}
	@Test
	public void getAllAccounts() {
		Authentication aunt = new Authentication();
		/* given: all input details ->(baseURI, Headers,Authorization,Playload/Body,QueryParameters)
		 * when: submit api requests -> HttpMethod(Endpoint/Resource)
		 * then: validate response -> (status code, Headers, responseTime, Playload/Body)
		 */
	Response resp =
			given()
		     	.baseUri(baseURI)
		     	.header("Content-Type", headerContentType)
		     //	.auth().preemptive().basic("demo@codefios.com", "abc123")
		     	.auth().preemptive().oauth2(aunt.generateBearerToken())
		     	.log().all().
			when()
			      .get(allAccountEndPoint).
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
	String firstAccountId = jp.getString("records[0].account_id");
	System.out.println("First account Id:" + firstAccountId);
			if (firstAccountId != null) {
				System.out.println("First Account ID is Not Null");
			} else {
				System.out.println("Fisrt Account ID is Null");
			} 
	}

}
