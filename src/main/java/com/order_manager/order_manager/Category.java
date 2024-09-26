package com.order_manager.order_manager;

import org.springframework.data.annotation.Id;

public record Category(@Id Integer category_id, String category_name) {

}
