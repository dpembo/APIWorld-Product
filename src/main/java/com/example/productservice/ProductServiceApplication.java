package com.example.productservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.productservice.ProductItem;

@RestController
@SpringBootApplication
public class ProductServiceApplication {

    private ProductItem productItem = null;
    private static List<ProductItem> productList = new ArrayList<ProductItem>();
    public List<ProductItem> getProductItems()
    {
        return productList;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
        //ProductServiceApplication self = new ProductServiceApplication();
        productList.add(new ProductItem(1,"foo","Foo Description"));
        productList.add(new ProductItem(2,"bar","Bar Description"));
    }

    @RequestMapping("/")
    public String home() {
        return "Hello from product service";
    }

    @RequestMapping("/product")
    public ProductItem product(@RequestParam(value="id", defaultValue="1") String id){
        try {
          return getProductItems().get(Integer.parseInt(id)-1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    
}

