package com.example.InventoryService;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.InventoryService.ProtoInventoryServiceGrpc.ProtoInventoryServiceImplBase;
import com.example.InventoryService.ProtoMsg.ProtoProductItem;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class InventoryGrpcService extends ProtoInventoryServiceImplBase {
    @Autowired
    InventoryRepository inventoryRepository;

    @Override
    public void order(ProtoMsg request, StreamObserver<ProtoMsg> responseObserver){
        log.info(request.toString());
        ArrayList<ProductItem> productItems = new ArrayList<>();
        request.getProductItemsList().forEach(p -> { productItems.add(new ProductItem(p.getCode(), p.getQuantity())); });

        String errorInfo = "";
        int httpCode = 200;
        ArrayList<ProtoProductItem> protoItems = new ArrayList<>();

        for(ProductItem item: productItems){
            Optional<ProductItem> foundItem = inventoryRepository.findById(item.Code);
            if(foundItem.isEmpty()){
                httpCode = 500;
                errorInfo += item.toString() + ": not found;  ";
            }
            else if(foundItem.get().Quantity < item.Quantity) {
                httpCode = 500;
                errorInfo = foundItem.get().toString() + ": bad quantity: " + foundItem.get().Quantity + " < " + item.Quantity + ";  ";
            }
        }

        if(httpCode != 200) responseObserver.onNext(ProtoMsg.newBuilder().setHttpCode(httpCode).setInfoError(errorInfo).build());
        else {
            for(ProductItem item: productItems) {
                ProductItem updatingProduct = updateInventoryDB(inventoryRepository.findById(item.Code).get(), item); 
                protoItems.add(
                            ProtoProductItem.newBuilder()
                            .setCode(updatingProduct.Code)
                            .setQuantity(updatingProduct.Quantity)
                            .build());    
            }

            responseObserver.onNext(ProtoMsg.newBuilder().setHttpCode(200).addAllProductItems(protoItems).build());
        }
   
        
        responseObserver.onCompleted();
    }

    private ProductItem updateInventoryDB(ProductItem dbProduct, ProductItem restProduct){
        dbProduct.Quantity -= restProduct.Quantity;
        if(dbProduct.Quantity == 0) {
            inventoryRepository.delete(dbProduct);
            return dbProduct;
        }
        return inventoryRepository.save(dbProduct);
    }
}