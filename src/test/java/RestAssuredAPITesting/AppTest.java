package RestAssuredAPITesting;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertNotNull;

import java.util.HashMap;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AppTest {
	
	public static HashMap map = new HashMap();

	@BeforeClass
	public void postData() {
		map.put("name","Nissan");
		map.put("mode", "Rogue XL");
		map.put("manufacturer","Nissan IC.");
		map.put("cost_in_credits","35000");
		map.put("length","unknow");
		map.put("max_atmosphering_speed","unknow");
		map.put("crew","unknow");
		map.put("passengers","5");
		map.put("cargo_capacity","unknow");
		map.put("consumables", "");
		map.put("vehicle_class", "SUV");
		map.put("pilots", "");
		map.put("films", "");
		map.put("created", "2020-20-20");
		map.put("edited", "2020-20-20");
		map.put("url", "www.example.com");
		RestAssured.baseURI="https://swapi.co/api/vehicles";
		RestAssured.basePath="/1";
	}
	@Test(priority = 1)
	public void testStatusCode() {
		given()
		.when()
			.get("https://swapi.co/api/vehicles")
		.then()
			.statusCode(200);
			
	}
	
	@Test(priority = 2)
	public void testSingleContent() {
		given()
		.when()
			.get("https://swapi.co/api/vehicles/20/")
		.then()
			.statusCode(200)
			.body("vehicle_class",equalTo("repulsorcraft"));
	}
	
	@Test(priority = 3)
	public void testMultipleContent() {
		given()
		.when()
			.get("https://swapi.co/api/vehicles/8/")
		.then()
			.statusCode(200)
			.body("films",hasItems("https://swapi.co/api/films/2/",
								   "https://swapi.co/api/films/3/",
								   "https://swapi.co/api/films/1/"));
	}
	
	@Test(priority = 4)
	public void testHeaderDetails() {
		given()
		.when()
			.get("https://swapi.co/api/vehicles/4/")
		.then()
		.statusCode(200)
			.statusLine("HTTP/1.1 200 OK")
			.assertThat().body("manufacturer", equalTo("Corellia Mining Corporation"))
			.header("Content-Type","application/json")
			.header("Server", "cloudflare");
	}
	
	@Test(priority = 5)
	public void testPost() {
		given()
			.contentType("application/json")
		.body(map)
		.when()
			.post()
		.then()
			.statusCode(301);
	}
	
	@Test(priority = 6)
	public void testDeleteVehicleRecord() {
		RestAssured.baseURI="https://swapi.co/api/vehicles/";
		RestAssured.basePath="14";
		Response response = 
		given()
		.when()
			.delete()
		.then()
			.statusCode(301)
			.extract().response();
		assertNotNull(response.asString());
	}
	@Test(priority = 7)
	public void testParametersAndHeader() {
		given()
			.param("key", "val")
			.header("Content-Encoding","br")
		.when()
			.get("https://swapi.co/api/vehicles/20/?key=val")
		.then()
			.statusCode(200)
			.log().all();
	}
	
	@Test(priority = 8)
	public void testPageNotFound() {
		given()
		
		.when()
			.get("https://swapi.co/api/vehicles/21/")
		.then()
			.assertThat().statusCode(404);
	}
}
