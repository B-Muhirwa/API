package testCases;
import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
public class UpdateAccount extends Authentication {
	String baseURI;
	String oneAccountEndPoint;
	String headerContentType;
	String firstAccountId;
	String updateAccountEndpoint;
	JsonPath jpath;
	Map<String, String> updateAccountPlayloadMap;

	public UpdateAccount() {
		baseURI = getProperty("baseUri");
		headerContentType = getProperty("header_content_Type");
		updateAccountEndpoint = getProperty("updateAccountEndpoint");
		updateAccountPlayloadMap = new HashMap<String, String>();
		oneAccountEndPoint =getProperty("getOneAccountEndpoint");
	}

	public Map<String, String> updateAccountBodyMap() {

		updateAccountPlayloadMap.put("account_id", "634");
		updateAccountPlayloadMap.put("account_name", "TechFios Saving Account");
		updateAccountPlayloadMap.put("account_number", "123456789");
		updateAccountPlayloadMap.put("description", "TechFios Business");
		updateAccountPlayloadMap.put("balance", "777.50");
		updateAccountPlayloadMap.put("contact_person", "Director");

		return updateAccountPlayloadMap;

	}

	@Test(priority = 1)
	public void updateAccount() {
		Response resp = 
				given()
				       .baseUri(baseURI)
				       .header("Content-Type", headerContentType)
			           .header("Authorization", generateBearerToken())
			           .body(updateAccountBodyMap()).
			     when()
				       .put(updateAccountEndpoint).
				 then()
				       .log().all()
				       .extract().response();

		int statusCode = resp.getStatusCode();
		System.out.println("Status code:" + statusCode);
		Assert.assertEquals(statusCode, 200, "Status codes are Not matching");

		String responseBody = resp.getBody().asString();
		jpath = new JsonPath(responseBody);
		System.out.println("Response Body: " + responseBody);

	}

	@Test(priority = 2)
	public void getOneAccount() {
		Authentication aunt = new Authentication();

		Response resp = 
				given()
				      .baseUri(baseURI)
				      .header("Content-Type", headerContentType)
				      .header("Authorization", aunt.generateBearerToken())
				      .queryParam("account_id", updateAccountBodyMap().get("account_id"))
				      .log().all().
				when()
				      .get(oneAccountEndPoint).

				then()
				      .log().all().extract().response();

		int statusCode = resp.getStatusCode();
		System.out.println("Status code:" + statusCode);
		Assert.assertEquals(statusCode, 200, "Status codes are Not matching");
		responseTime = resp.timeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time:" + responseTime);
		String responseHeaderContentType = resp.getHeader("Content-Type");

		System.out.println("Header Content Type :" + responseHeaderContentType);
		Assert.assertEquals(responseHeaderContentType, headerContentType);
		String responseBody = resp.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		jpath = new JsonPath(responseBody);
		String actualAccountName = jpath.getString("account_name");
		String actualAccountNumber = jpath.getString("account_number");
		String actualBalance = jpath.getString("balance");
		String actualContactPerson = jpath.getString("contact_person");

		String expectedAccountName = updateAccountPlayloadMap.get("account_name");
		String expectedAccountNumber = updateAccountPlayloadMap.get("account_number");
		String expectedBalance = updateAccountPlayloadMap.get("balance");
		String expectedContactPerson = updateAccountPlayloadMap.get("contact_person");

		Assert.assertEquals(actualAccountName, expectedAccountName, "Account Name Not Found");
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber, "Account Number Not Matching");
		Assert.assertEquals(actualBalance, expectedBalance, "Account Balance Not Matching");
		Assert.assertEquals(actualContactPerson, expectedContactPerson, "Account Balance Not Matching");

	}

}
