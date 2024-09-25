package com.order_manager.order_manager;

import org.springframework.data.annotation.Id;

public record MenuItem(@Id Integer dish_id, String dish_name, String dish_image, String category, Float price, Boolean available) {

}
