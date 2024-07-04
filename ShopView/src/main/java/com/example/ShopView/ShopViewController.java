package com.example.ShopView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import graphql.kickstart.spring.webclient.boot.GraphQLWebClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
public class ShopViewController {

    @ModelAttribute
    public void addValuesToModel(Model model) {

        WebClient client = WebClient.builder().baseUrl("http://localhost:8087/graphql").build();

       String document = """
        query {
            allProducts {
                Code
                Model 
            }
        }
        """;


        HttpGraphQlClient graphql = HttpGraphQlClient.builder().url("http://localhost:8087/graphql").build();
        List<Product> arr = graphql.document(document).retrieve("allProducts").toEntityList(Product.class).block();
        log.info(Arrays.toString(arr.toArray()));

       
        List<Product> products = Arrays.asList(
            new Product("1", "bmw", "xa", "RUSSIA", "100$"),
            new Product("2", "bmw2", "xa2", "RUSSIA2", "200$"),
            new Product("3", "bmw3", "xa3", "RUSSIA3", "300$")
        );

        model.addAttribute("products", products);

        ArrayList<ProductItem> items = new ArrayList<>();
        products.forEach(p -> {items.add(new ProductItem());});

        model.addAttribute("form", new ProductsCreationDTO(items));
    }

    @PostMapping("/order")
    public String order(Model model, @ModelAttribute("form") ProductsCreationDTO form) {
        log.info(Arrays.toString(form.getItems().toArray()));

        return "redirect:/";
    }
    

    @GetMapping("/")
    public String order(){
        return "index";
    }
}
