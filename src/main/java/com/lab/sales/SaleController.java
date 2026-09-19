package com.lab.sales;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins="*")
public class SaleController {
  private final SaleRepository repository;
  public SaleController(SaleRepository repository){this.repository=repository;}
  @GetMapping public List<Sale> all(){return repository.findAll();}
  @GetMapping("/{id}") public ResponseEntity<Sale> one(@PathVariable Long id){
    return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }
  @PostMapping public ResponseEntity<Sale> create(@Valid @RequestBody Sale sale){
    return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(sale));
  }
}
