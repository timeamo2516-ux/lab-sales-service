package com.lab.sales;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
@Entity
@Table(name="sales")
public class Sale {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @NotNull private Long productId;
  @NotBlank private String productName;
  @Min(1) private int quantity;
  @Positive private double unitPrice;
  private LocalDateTime createdAt;
  @PrePersist void onCreate(){createdAt=LocalDateTime.now();}
  public Sale(){}
  public Long getId(){return id;} public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
  public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
  public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
  public double getUnitPrice(){return unitPrice;} public void setUnitPrice(double v){unitPrice=v;}
  public LocalDateTime getCreatedAt(){return createdAt;}
}
