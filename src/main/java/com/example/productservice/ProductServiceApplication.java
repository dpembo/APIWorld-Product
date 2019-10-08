package com.example.productservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.productservice.ProductItem;

@RestController
@SpringBootApplication
public class ProductServiceApplication {

    public ProductServiceApplication()
    {
        populateProductList();
    }

    //private ProductItem productItem = null;
    private static HashMap<String, ProductItem> productList = new HashMap<String,ProductItem>();
    public HashMap<String, ProductItem> getProductItems()
    {
        return productList;
    }

    private static void populateProductList()
    {
        productList.clear();
        productList.put("1",new ProductItem(1,"foo","Foo Description"));
        productList.put("2",new ProductItem(2,"bar","Bar Description"));
        productList.put("3",new ProductItem(3,"wgt","Widget Description"));
        productList.put("4",new ProductItem(4,"dgb","Digestive Biscuits"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
        //ProductServiceApplication self = new ProductServiceApplication();
        populateProductList();
    }

    @RequestMapping("/")
    public String home() {
        return "Hello from product service.  Try /product resource";
    }

    @GetMapping("/product")
    public List<ProductItem> product(){

        //Populate values if empty
        if(getProductItems()==null||getProductItems().isEmpty())populateProductList();
        return new ArrayList<ProductItem>(getProductItems().values());
    }



  
    @GetMapping("/product/{id}")
    public List<ProductItem> product(@PathVariable(value="id") String id){

        //Populate values if empty
        if(getProductItems()==null||getProductItems().isEmpty())populateProductList();

        //Get the item with the passed ID
        List<ProductItem> singleItemList = new ArrayList<ProductItem>();
        singleItemList.add(getProductItems().get(id));
        return singleItemList;
    }

    /**
     * Insert a product into the list
     * @param newProduct
     * @return
     */
    @PostMapping("/product")
    public boolean insertProduct(@RequestBody ProductItem newProduct){
        //System.out.println(newProduct);
        //Get a new ID
        int id = getProductItems().size() + 1;
        newProduct.setId(Integer.valueOf(id));
        getProductItems().put(Integer.toString(id), newProduct);
        return true;
    }


    
}

