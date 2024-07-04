package com.example.InventoryService;

import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<ProductItem, String> {

}