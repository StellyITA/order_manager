package com.order_manager.order_manager;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderManagerApplicationTests {

	@Autowired
	TestRestTemplate testRest;

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
	void shouldCreateANewMenuItem() {
		MenuItem testItem = new MenuItem(null, "Misto di fritti", "starter", 16.79f, true);
		ResponseEntity<Void> postResponse = testRest.postForEntity("/menu", testItem, Void.class);

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
