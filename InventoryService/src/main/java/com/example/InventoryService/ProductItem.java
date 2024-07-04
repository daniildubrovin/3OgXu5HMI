package com.example.InventoryService;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productitems")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {
    @Id
    String Code;
    int Quantity;
}
