package org.adaschool.api.controller.product;

import org.adaschool.api.exception.ProductNotFoundException;
import org.adaschool.api.repository.product.Product;
import org.adaschool.api.service.product.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/products/")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(@Autowired ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        URI createdProductUri = URI.create("");
        productsService.save(product);
        return ResponseEntity.created(createdProductUri).contentType(MediaType.APPLICATION_JSON).body(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productsService.all());
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") String id){
        Optional<Product> p = productsService.findById(id);
        if (p.isEmpty()) throw new ProductNotFoundException(id);
        return ResponseEntity.ok(p.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") String id, @RequestBody Product updatedProduct) {
        Optional<Product> existingProduct = productsService.findById(id);
        if (existingProduct.isEmpty()) throw new ProductNotFoundException(id);
        Product productToUpdate = existingProduct.get();
        productToUpdate.setName(updatedProduct.getName());
        productToUpdate.setDescription(updatedProduct.getDescription());
        productToUpdate.setCategory(updatedProduct.getCategory());
        productToUpdate.setPrice(updatedProduct.getPrice());

        return ResponseEntity.ok(productsService.save(productToUpdate));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        Optional<Product> p = productsService.findById(id);
        if (p.isEmpty()) throw new ProductNotFoundException(id);
        this.productsService.deleteById(id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build();
    }
}
