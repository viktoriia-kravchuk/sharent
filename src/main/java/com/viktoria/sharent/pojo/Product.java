package com.viktoria.sharent.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name="id")
    private int id;
    @Column(name="product_name",length = 50)
    @NotEmpty(message = "*Please provide product name")
    private String productName;
    @Column (name="description", length = 1000)
    @NotEmpty(message = "*Please provide product description")
    private String productDescription;
    @Column (name="availability")
    private Boolean availability;
    @Column (name="rent_price")
    @Positive(message ="Price should be positive")
    private int productPrice;
    @Column (name="created_at")
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "user_id")
    User user;




}
