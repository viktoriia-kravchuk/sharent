package com.viktoria.sharent.repository;

import com.viktoria.sharent.pojo.Product;
import com.viktoria.sharent.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByUser(User user);
    List<Product> findTop10ByProductNameContaining(String query);
}
