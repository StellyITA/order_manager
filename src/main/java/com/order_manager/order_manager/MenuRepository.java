package com.order_manager.order_manager;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MenuRepository extends CrudRepository<MenuItem, Integer>, PagingAndSortingRepository<MenuItem, Integer> {

}
