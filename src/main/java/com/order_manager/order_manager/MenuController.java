package com.order_manager.order_manager;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    private MenuController(MenuRepository menuRepository, CategoryRepository categoryRepository) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @GetMapping()
    private ResponseEntity<Iterable<MenuItem>> findAll(Pageable pageable) {
        ArrayList<Sort.Order> sortOrderList = new ArrayList<>();
        sortOrderList.add(new Sort.Order(Sort.Direction.ASC,"category"));
        sortOrderList.add(new Sort.Order(Sort.Direction.DESC,"price"));
        Page<MenuItem> page = menuRepository.findAll(
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(sortOrderList))
            )
        );

        return ResponseEntity.ok(page.getContent());
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @GetMapping("/categories")
    private ResponseEntity<Iterable<Category>> findAllCategories(Pageable pageable) {
        Page<Category> categoriesList = categoryRepository.findAll(
            PageRequest.of(
                pageable.getPageNumber(), 
                pageable.getPageSize(), 
                pageable.getSortOr(Sort.by(Direction.ASC, "category_id"))
            )
        );

        return ResponseEntity.ok(categoriesList.getContent());
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @GetMapping("/categories/{category}")
    private ResponseEntity<Iterable<MenuItem>> findByCategory(@PathVariable String category, Pageable pageable) {
        Page<MenuItem> page = menuRepository.findByCategory(category,
            PageRequest.of(
                pageable.getPageNumber(), 
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Direction.DESC, "price"))
            )
        );

        return ResponseEntity.ok(page.getContent());
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

    @PutMapping("/{itemId}")
    private ResponseEntity<Void> updateMenuItem(@PathVariable int itemId, @RequestBody MenuItem updatedItem) {
        Optional<MenuItem> currentOptional = menuRepository.findById(itemId);
        
        if (currentOptional.isPresent()) {
            
            MenuItem current = currentOptional.get();
            String name = updatedItem.dish_name() == null ? current.dish_name() : updatedItem.dish_name();
            String image = updatedItem.dish_image() == null ? current.dish_image() : updatedItem.dish_image();
            Integer category = updatedItem.category() == null ? current.category() : updatedItem.category();
            Float price = updatedItem.price() == null ? current.price() : updatedItem.price();
            boolean available = updatedItem.available() == null ? current.available() : updatedItem.available();
        
            MenuItem toBeSaved = new MenuItem(
                itemId, 
                name, 
                image, 
                category, 
                price, 
                available
            );
            menuRepository.save(toBeSaved);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    private ResponseEntity<Void> createMenuItem(@RequestBody MenuItem itemToCreate, UriComponentsBuilder ucb) {
        MenuItem createdItem = menuRepository.save(itemToCreate);
        URI location = ucb.path("/menu/{id}").buildAndExpand(createdItem.dish_id()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{itemId}")
    private ResponseEntity<Void> deleteMenuItem(@PathVariable int itemId) {
        if (menuRepository.existsById(itemId)) {
            menuRepository.delete(menuRepository.findById(itemId).get());            

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
