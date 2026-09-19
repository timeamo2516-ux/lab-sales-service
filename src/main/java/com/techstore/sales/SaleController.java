package com.techstore.sales;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/sales") @CrossOrigin(origins="*")
public class SaleController {
    private final SaleRepository repository; private final InventoryClient inventory;
    public SaleController(SaleRepository repository,InventoryClient inventory){this.repository=repository;this.inventory=inventory;}
    @GetMapping public List<Sale> all(){return repository.findAll();}
    @GetMapping("/{id}") public ResponseEntity<Sale> one(@PathVariable Long id){
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public ResponseEntity<?> create(@Valid @RequestBody SaleRequest req){
        ProductResponse product=inventory.get(req.productId());
        if(product==null)return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        if(product.stock()<req.quantity())return ResponseEntity.status(HttpStatus.CONFLICT).body("Stock insuficiente. Stock actual: "+product.stock());
        ProductResponse updated;
        try{updated=inventory.decrease(req.productId(),req.quantity());}
        catch(IllegalStateException e){return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());}
        if(updated==null)return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        Sale sale=new Sale(); sale.setProductId(updated.id()); sale.setProductName(updated.name()); sale.setQuantity(req.quantity()); sale.setUnitPrice(updated.price());
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(sale));
    }
}
