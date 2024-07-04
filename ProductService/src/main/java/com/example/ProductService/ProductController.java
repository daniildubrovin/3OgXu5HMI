package com.example.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public Iterable<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @PostMapping(value = "/add")
    public Product addProduct(@RequestBody Product product){
        log.info("add: " + product.toString());
        return productRepository.save(product);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteProduct(@RequestBody String code){
        log.info("delete: " + code);
        if(!productRepository.existsById(code)) return new ResponseEntity<>(code + " not founded", HttpStatus.INTERNAL_SERVER_ERROR);
        productRepository.deleteById(code);
        return new ResponseEntity<>(code + " deleted", HttpStatus.OK);
    }
}