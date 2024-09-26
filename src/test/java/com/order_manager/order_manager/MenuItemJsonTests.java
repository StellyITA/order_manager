package com.order_manager.order_manager;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.junit.jupiter.api.BeforeEach;
import org.assertj.core.util.Arrays;

@JsonTest
public class MenuItemJsonTests {

    @Autowired
    private JacksonTester<MenuItem> json;

    @Autowired
    private JacksonTester<MenuItem[]> jsonList;

    private MenuItem[] itemsList;

    @BeforeEach
    void setup() {
        itemsList = Arrays.array(
            new MenuItem(1,"Antipasto di terra", null, 1,18.79f,true),
            new MenuItem(2,"Cozze alla marinara", null, 1,15.79f,true),
            new MenuItem(3,"Crudo e bufala", null, 1,16.79f,true)
        );
    }

    @Test
    void jsonListSerializationTest() throws IOException {
        assertThat(jsonList.write(itemsList)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void jsonListDeserializationTest() throws IOException {
        String expected = """
                [
                    {
                        "dish_id": 1,
                        "dish_name": "Antipasto di terra",
                        "dish_image": null,
                        "category": 1,
                        "price": 18.79,
                        "available": true
                    },
                    {
                        "dish_id": 2,
                        "dish_name": "Cozze alla marinara",
                        "dish_image": null,
                        "category": 1,
                        "price": 15.79,
                        "available": true
                    },
                    {
                        "dish_id": 3,
                        "dish_name": "Crudo e bufala",
                        "dish_image": null,
                        "category": 1,
                        "price": 16.79,
                        "available": true
                    }
                ]
                """;

        assertThat(jsonList.parse(expected)).isEqualTo(itemsList);
    }

    @Test
    void jsonSerializationTest() throws IOException {
        MenuItem testItem = new MenuItem(1, "Antipasto di terra", null, 1, 18.79f, true);
        
        assertThat(json.write(testItem)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(testItem)).hasJsonPathNumberValue("$.dish_id");
        assertThat(json.write(testItem)).extractingJsonPathNumberValue("$.dish_id").isEqualTo(1);
        assertThat(json.write(testItem)).hasJsonPathStringValue("$.dish_name");
        assertThat(json.write(testItem)).extractingJsonPathStringValue("$.dish_name").isEqualTo("Antipasto di terra");
        assertThat(json.write(testItem)).hasJsonPathNumberValue("$.category");
        assertThat(json.write(testItem)).extractingJsonPathNumberValue("$.category").isEqualTo(1);
        assertThat(json.write(testItem)).hasJsonPathNumberValue("$.price");
        assertThat(json.write(testItem)).extractingJsonPathNumberValue("$.price").isEqualTo(18.79);
        assertThat(json.write(testItem)).hasJsonPathBooleanValue("$.available");
        assertThat(json.write(testItem)).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }

    @Test
    void jsonDeserializationTest() throws IOException {
        String expected = """
                {
                    "dish_id": 1,
                    "dish_name": "Antipasto di terra",
                    "dish_image": null,
                    "category": 1,
                    "price": 18.79,
                    "available": true
                }
                """;

                assertThat(json.parse(expected)).isEqualTo(new MenuItem(1,"Antipasto di terra",null,1,18.79f,true));
        assertThat(json.parseObject(expected).dish_id()).isEqualTo(1);
        assertThat(json.parseObject(expected).dish_name()).isEqualTo("Antipasto di terra");
        assertThat(json.parseObject(expected).category()).isEqualTo(1);
        assertThat(json.parseObject(expected).price()).isEqualTo(18.79f);
        assertThat(json.parseObject(expected).available()).isEqualTo(true);
    }
}
