package com.example.ShopView;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsCreationDTO {
    private List<ProductItem> items;

    public void addItem(ProductItem item){
        items.add(item);
    }
}
