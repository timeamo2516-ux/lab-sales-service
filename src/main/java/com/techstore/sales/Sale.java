package com.techstore.sales;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="sales")
public class Sale {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private Long productId;
 @Column(nullable=false) private String productName;
 @Column(nullable=false) private int quantity;
 @Column(nullable=false) private double unitPrice;
 @Column(nullable=false) private LocalDateTime createdAt;
 @PrePersist void prePersist(){createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
 public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
 public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
 public double getUnitPrice(){return unitPrice;} public void setUnitPrice(double v){unitPrice=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
