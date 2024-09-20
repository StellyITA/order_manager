package com.order_manager.order_manager;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class MenuItemJsonTests {

    @Autowired
    private JacksonTester<MenuItem> json;

    @Test
    void jsonSerializationTest() throws IOException {
        MenuItem testItem = new MenuItem(1, "Antipasto di terra", "starter", 18.79f, true);
        
        assertThat(json.write(testItem)).isStrictlyEqualToJson("expected.json");
        assertThat(json.write(testItem)).hasJsonPathNumberValue("$.dish_id");
        assertThat(json.write(testItem)).extractingJsonPathNumberValue("$.dish_id").isEqualTo(1);
        assertThat(json.write(testItem)).hasJsonPathStringValue("$.dish_name");
        assertThat(json.write(testItem)).extractingJsonPathStringValue("$.dish_name").isEqualTo("Antipasto di terra");
        assertThat(json.write(testItem)).hasJsonPathStringValue("$.category");
        assertThat(json.write(testItem)).extractingJsonPathStringValue("$.category").isEqualTo("starter");
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
                    "category": "starter",
                    "price": 18.79,
                    "available": true
                }
                """;

        assertThat(json.parse(expected)).isEqualTo(new MenuItem(1,"Antipasto di terra","starter",18.79f,true));
        assertThat(json.parseObject(expected).dish_id()).isEqualTo(1);
        assertThat(json.parseObject(expected).dish_name()).isEqualTo("Antipasto di terra");
        assertThat(json.parseObject(expected).category()).isEqualTo("starter");
        assertThat(json.parseObject(expected).price()).isEqualTo(18.79f);
        assertThat(json.parseObject(expected).available()).isEqualTo(true);
    }
}
