package com.order_manager.order_manager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

import java.util.Optional;

import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuRepository menuRepository;

    private MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping("/{itemId}")
    private ResponseEntity<MenuItem> findById(@PathVariable int itemId) {
        Optional<MenuItem> menuItem = menuRepository.findById(itemId);

        if (menuItem.isPresent()) {
            return ResponseEntity.ok(menuItem.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createMenuItem(@RequestBody MenuItem itemToCreate, UriComponentsBuilder ucb) {
        MenuItem createdItem = menuRepository.save(itemToCreate);
        URI location = ucb.path("/menu/{id}").buildAndExpand(createdItem.dish_id()).toUri();
        return ResponseEntity.created(location).build();
    }
}
