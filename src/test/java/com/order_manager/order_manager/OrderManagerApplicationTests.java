package com.order_manager.order_manager;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderManagerApplicationTests {

	@Autowired
	TestRestTemplate testRest;

	// GET

	@Test
	void shouldReturnItemsOfRequestedCategory() {
		ResponseEntity<String> response = testRest.getForEntity("/menu/categories/1", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext body = JsonPath.parse(response.getBody());
		int size = body.read("$.length()");
		double price = body.read("$[0].price");

		assertThat(size).isEqualTo(3);
		assertThat(price).isEqualTo(18.8);
	}

	@Test
	void shouldReturnRequestedNumberOfMenuItemsInDefaultOrder() {
		ResponseEntity<String> response = testRest.getForEntity("/menu?page=0&size=2", String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		DocumentContext body = JsonPath.parse(response.getBody());
		int size = body.read("$.length()");
		Double price = body.read("$[1].price");

		assertThat(size).isEqualTo(2);
		assertThat(price).isEqualTo(16.79);
	}

	@Test
	void shouldReturnSortedItemsWhenRequested() {
		ResponseEntity<String> response = testRest.getForEntity("/menu?page=0&size=1&sort=price,asc", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext body = JsonPath.parse(response.getBody());
		int size = body.read("$.length()");
		Double price = body.read("$[0].price");

		assertThat(size).isEqualTo(1);
		assertThat(price).isEqualTo(15);
	}

	@Test
	void shouldReturnRequestedNumberOfMenuItems() {
		ResponseEntity<String> response = testRest.getForEntity("/menu?page=0&size=1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext body = JsonPath.parse(response.getBody());
		int size = body.read("$.length()");

		assertThat(size).isEqualTo(1);
	}

	@Test
	void shouldReturnAllMenuItemsWhenRequested() {
		ResponseEntity<String> response = testRest.getForEntity("/menu", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext body = JsonPath.parse(response.getBody());
		int size = body.read("$.length()");
		JSONArray ids = body.read("$..dish_id");
		JSONArray names = body.read("$..dish_name");
		JSONArray categories = body.read("$..category");
		JSONArray prices = body.read("$..price");
		JSONArray availability = body.read("$..available");

		assertThat(size).isEqualTo(6);
		assertThat(ids).containsExactlyInAnyOrder(1,2,3,4,5,6);
		assertThat(names).containsExactlyInAnyOrder("Antipasto di terra","Cozze alla marinara","Crudo e bufala","Carbonara","Noci, gorgonzola e radicchio","Pescatora");
		assertThat(categories).containsExactly(1,1,1,2,2,2);
		assertThat(prices).containsExactlyInAnyOrder(18.8,15.0,16.79,17.8,19.8,16.8);
		assertThat(availability).containsExactlyInAnyOrder(true,false,true,true,true,true);
	}

	@Test
	void shouldReturnMenuItemWhenDataIsSaved() {
		ResponseEntity<String> response = testRest.getForEntity("/menu/1", String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext responseBody = JsonPath.parse(response.getBody());

		Number id = responseBody.read("$.dish_id");
		assertThat(id).isNotNull();
		assertThat(id).isEqualTo(1);

		String name = responseBody.read(("$.dish_name"));
		assertThat(name).isNotNull();
		assertThat(name).isEqualTo("Antipasto di terra");

		String img = responseBody.read("$.dish_image");

		assertThat(img).isNotNull();
	}

	@Test
	void shouldNotReturnAnUnknownId() {
		ResponseEntity<String> response = testRest.getForEntity("/menu/100", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	// POST

	@Test
	void shouldNotCreateAnItemIfUnauthorizedRole() {
		MenuItem testItem = new MenuItem(null, "null", null, 1, 0f, false);
		ResponseEntity<Void> response = testRest.withBasicAuth("username", "12345678").postForEntity("/menu", testItem, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	void shouldNotCreateAnItemIfWrongCredentials() {
		MenuItem testItem = new MenuItem(null, "null", null, 1, 0f, false);
		ResponseEntity<Void> response = testRest.withBasicAuth("admin", "wrongpassword").postForEntity("/menu", testItem, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

		response = testRest.withBasicAuth("wrongname", "password").postForEntity("/menu", testItem, Void.class);
	
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}	

	@Test
	@DirtiesContext
	void shouldCreateANewMenuItem() {
		MenuItem testItem = new MenuItem(null, "Misto di fritti", null, 1, 16.79f, true);
		ResponseEntity<Void> postResponse = testRest.withBasicAuth("admin", "password").postForEntity("/menu", testItem, Void.class);

		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI testItemLocation = postResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = testRest.getForEntity(testItemLocation, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext responseBody = JsonPath.parse(getResponse.getBody());
		Number id = responseBody.read("$.dish_id");
		String name = responseBody.read("$.dish_name");
		Number category = responseBody.read("$.category");
		Number price = responseBody.read("$.price");
		boolean available = responseBody.read("$.available");
		
		assertThat(id).isNotNull();
		assertThat(name).isEqualTo("Misto di fritti");
		assertThat(category).isEqualTo(1);
		assertThat(price).isEqualTo(16.79);
		assertThat(available).isEqualTo(true);
	}

	// PUT

	@Test
	void shouldNotUpdateMenuItemIfUnauthorizedRole () {
		MenuItem testItem = new MenuItem(null, "null", null, 1, 0f, false);
		HttpEntity<MenuItem> request = new HttpEntity<>(testItem);
		ResponseEntity<Void> response = testRest
			.withBasicAuth("username", "12345678")
			.exchange("/menu/1", HttpMethod.PUT, request, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	@DirtiesContext
	void shouldUpdateMenuItem() {
		MenuItem testItem = new MenuItem(null, "Antipasto di terra", null, 1, 18.79f, false);
		HttpEntity<MenuItem> request = new HttpEntity<>(testItem);
		ResponseEntity<Void> response = testRest
			.withBasicAuth("admin", "password")
			.exchange("/menu/1", HttpMethod.PUT, request, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> getResponse = testRest.getForEntity("/menu/1", String.class);
		DocumentContext body = JsonPath.parse(getResponse.getBody());
		boolean available = body.read("$.available");
		String img = body.read("$.dish_image");

		assertThat(available).isEqualTo(false);
		assertThat(img).isNotNull();
	}

	// DELETE

	@Test
	void shouldNotDeleteMenuItemIfUnauthorizedRole() {
		ResponseEntity<Void> response = testRest
			.withBasicAuth("username", "12345678")
			.exchange("/menu/1", HttpMethod.DELETE, null, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

		ResponseEntity<String> getResponse = testRest.getForEntity("/menu/1", String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldNotDeleteUnexistingMenuItem() {
		ResponseEntity<Void> response = testRest
			.withBasicAuth("admin", "password")
			.exchange("/menu/999", HttpMethod.DELETE, null, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldDeleteMenuItem() {
		ResponseEntity<Void> response = testRest
			.withBasicAuth("admin", "password")
			.exchange("/menu/1", HttpMethod.DELETE, null, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

}
