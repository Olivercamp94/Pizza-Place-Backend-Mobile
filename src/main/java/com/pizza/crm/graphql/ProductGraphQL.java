package com.pizza.crm.graphql;

import com.pizza.crm.model.Product;
import com.pizza.crm.service.ProductService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductGraphQL {
    private final ProductService productService;

    public ProductGraphQL(ProductService productService){
        this.productService = productService;
    }

    @QueryMapping
    public List<Product> listProducts(){
        return productService.findAll();
    }

    @QueryMapping
    public Product findProduct(@Argument Long id){
        return productService.findById(id).orElse(null);
    }

    @MutationMapping
    public Product addProduct(@Argument String name, @Argument String description, @Argument Double price){
        
        Product product = new Product();
        
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        return productService.save(product);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id){
        productService.delete(id);
        return true;
    }
}
