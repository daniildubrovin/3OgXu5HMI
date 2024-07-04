package com.example.GraphQLService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class GraphQLService {

    @QueryMapping
    public List<Product> allProducts(){
        log.info("graphql: allProducts");

        Mono<Product[]> result = WebClient.create("http://products:8080/products")
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Product[].class);

        return List.of(result.block());      
    }

    @QueryMapping
    public String addProduct(@Argument Product product){
        log.info("graphql: addProduct: " + product);   
        return WebClient.create("http://products:8080/add")
                .post()
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class).block().toString(); 
    }

    @QueryMapping
    public String deleteProduct(@Argument String code){
        log.info("graphql: deleteProduct: " + code);   
        Mono<String> mono = WebClient.create("http://products:8080/delete")
                    .post()
                    .bodyValue(code)
                    .retrieve()
                    .bodyToMono(String.class);

        String res;
        try {
            res = mono.block();
        } catch(RuntimeException e) {
            res = e.getMessage();
        }

        return res;
    }

    @QueryMapping
    public String newOrder(@Argument ArrayList<ProductItem> productItems){
        log.info("graphql: newOrder: " + Arrays.toString(productItems.toArray()));
        Mono<String> result = WebClient.create("http://order:8084/order")
                    .post()
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(productItems)
                    .retrieve()
                    .bodyToMono(String.class);

        return result.block();
    }
}
