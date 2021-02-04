package com.viktoria.sharent.controller;

import com.viktoria.sharent.pojo.Product;
import com.viktoria.sharent.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor

public class SearchApiController {
    private final ProductService productService;
    @GetMapping("/search/{query}")
    public List<Product> searchProduct(@RequestBody @PathVariable String query){
        return productService.searchProduct(query);
    }

}
