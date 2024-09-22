package com.order_manager.order_manager;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import net.minidev.json.JSONArray;

import java.net.URI;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderManagerApplicationTests {

	@Autowired
	TestRestTemplate testRest;

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
		assertThat(price).isEqualTo(15.79);
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

		assertThat(size).isEqualTo(3);
		assertThat(ids).containsExactlyInAnyOrder(1,2,3);
		assertThat(names).containsExactlyInAnyOrder("Antipasto di terra","Cozze alla marinara","Crudo e bufala");
		assertThat(categories).containsExactlyInAnyOrder("starter","starter","starter");
		assertThat(prices).containsExactlyInAnyOrder(18.79,15.79,16.79);
		assertThat(availability).containsExactlyInAnyOrder(true,true,true);
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
	}

	@Test
	void shouldNotReturnAnUnknownId() {
		ResponseEntity<String> response = testRest.getForEntity("/menu/100", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	void shouldNotCreateAnItemIfUnauthorizedRole() {
		MenuItem testItem = new MenuItem(null, "null", "null", 0, false);
		ResponseEntity<Void> response = testRest.withBasicAuth("username", "12345678").postForEntity("/menu", testItem, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	void shouldNotCreateAnItemIfWrongCredentials() {
		MenuItem testItem = new MenuItem(null, "null", "null", 0, false);
		ResponseEntity<Void> response = testRest.withBasicAuth("admin", "wrongpassword").postForEntity("/menu", testItem, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

		response = testRest.withBasicAuth("wrongname", "password").postForEntity("/menu", testItem, Void.class);
	
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	@DirtiesContext
	void shouldCreateANewMenuItem() {
		MenuItem testItem = new MenuItem(null, "Misto di fritti", "starter", 16.79f, true);
		ResponseEntity<Void> postResponse = testRest.withBasicAuth("admin", "password").postForEntity("/menu", testItem, Void.class);

		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI testItemLocation = postResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = testRest.getForEntity(testItemLocation, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext responseBody = JsonPath.parse(getResponse.getBody());
		Number id = responseBody.read("$.dish_id");
		String name = responseBody.read("$.dish_name");
		String category = responseBody.read("$.category");
		Number price = responseBody.read("$.price");
		boolean available = responseBody.read("$.available");
		
		assertThat(id).isNotNull();
		assertThat(name).isEqualTo("Misto di fritti");
		assertThat(category).isEqualTo("starter");
		assertThat(price).isEqualTo(16.79);
		assertThat(available).isEqualTo(true);
	}

}
