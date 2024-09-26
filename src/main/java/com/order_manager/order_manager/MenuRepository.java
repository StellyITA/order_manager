package com.order_manager.order_manager;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuRepository extends CrudRepository<MenuItem, Integer>, PagingAndSortingRepository<MenuItem, Integer> {
    
    Page<MenuItem> findByCategory(String category, Pageable pageable);
}
