package com.order_manager.order_manager;

import org.springframework.data.annotation.Id;

public record MenuItem(@Id Integer dish_id, String dish_name, String category, float price, boolean available) {

}
