package com.example.OrderService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.OrderService.ProtoInventoryServiceGrpc.ProtoInventoryServiceBlockingStub;
import com.example.OrderService.ProtoMsg.ProtoProductItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Slf4j
@RestController
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @GrpcClient("inventory-service")
    private ProtoInventoryServiceBlockingStub inventoryStub;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @PostMapping("/order")
    ResponseEntity<String> order(@RequestBody List<ProductItem> productItems) throws JsonProcessingException{  
        String orderCode = ObjectId.get().toString();
        ArrayList<ProtoProductItem> protoItems = new ArrayList<>();
        productItems.forEach(product -> {
            protoItems.add(ProtoProductItem.newBuilder()
                            .setCode(product.Code)
                            .setQuantity(product.Quantity)
                            .build());
        });

        log.info(Arrays.toString(protoItems.toArray()));

        ProtoMsg responseMsg = inventoryStub.order(
                            ProtoMsg.newBuilder()
                            .addAllProductItems(protoItems)
                            .build()
        ); 

        if(responseMsg.getHttpCode() != HttpStatus.OK.value()) {
            return new ResponseEntity<String>(responseMsg.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Order order = new Order(orderCode, productItems);
        orderRepository.insert(order);    

        // kafkaTemplate.send("notifications", new ObjectMapper()
        //                     .writeValueAsString(
        //                         new Notification("orderService","add new order",order.toString()))
        // );

        return new ResponseEntity<String>(Arrays.toString(responseMsg.getProductItemsList().toArray()), HttpStatus.OK);
    }
}
