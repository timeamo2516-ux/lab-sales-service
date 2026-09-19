package com.techstore.sales;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class InventoryClient {
 private final RestClient client;
 public InventoryClient(@Value("${inventory.url}") String url){client=RestClient.builder().baseUrl(url).build();}
 public ProductResponse get(Long id){
  try{return client.get().uri("/api/products/{id}",id).retrieve().body(ProductResponse.class);}
  catch(HttpClientErrorException.NotFound e){return null;}
 }
 public ProductResponse decrease(Long id,int quantity){
  try{return client.patch().uri("/api/products/{id}/stock/decrease",id).contentType(MediaType.APPLICATION_JSON).body(new StockBody(quantity)).retrieve().body(ProductResponse.class);}
  catch(HttpClientErrorException.Conflict e){throw new IllegalStateException("Stock insuficiente");}
  catch(HttpClientErrorException.NotFound e){return null;}
 }
 record StockBody(int quantity){}
}
