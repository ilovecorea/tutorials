package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public List<Product> listAll() {
    return productRepository.findAll();
  }

  public void save(Product product) {
    productRepository.save(product);
  }

  public Product get(Long id) {
    return productRepository.findById(id).get();
  }

  public void delete(Long id) {
    productRepository.deleteById(id);
  }

}
