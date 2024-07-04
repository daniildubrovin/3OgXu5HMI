package com.example.ShopView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    String Code;
    String Model;
    String Description;
    String Country;
    String Price;
}