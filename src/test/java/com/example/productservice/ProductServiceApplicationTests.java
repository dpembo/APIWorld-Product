package com.example.productservice;

import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.net.URISyntaxException;
import java.util.List;

import com.example.productservice.ProductServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceApplicationTests {
 

    @Test
    public void testGetSingleProduct() throws URISyntaxException
    {
        ProductServiceApplication productServiceApplication = new ProductServiceApplication();
        List<ProductItem> prodList = productServiceApplication.product("1");
        ProductItem prod = prodList.get(0);

        Assert.assertEquals("foo", prod.getProductName());
    }

  
    @Test
    public void testProductList() throws URISyntaxException
    {
        ProductServiceApplication productServiceApplication = new ProductServiceApplication();
        List<ProductItem> prodList = productServiceApplication.product();
        

        Assert.assertEquals(true, prodList.size()>0);
    }

    @Test
    public void testProductCount() throws URISyntaxException
    {
        ProductServiceApplication productServiceApplication = new ProductServiceApplication();
        List<ProductItem> prodList = productServiceApplication.product();
        int count =productServiceApplication.productCount();
        Assert.assertEquals(count,prodList.size());   
    }

    @Test
    public void addProduct() throws URISyntaxException
    {
        //Get produt List
        ProductServiceApplication productServiceApplication = new ProductServiceApplication();
        List<ProductItem> prodList = productServiceApplication.product();

        ProductItem pi = new ProductItem(99,"NAME","DESC");
        boolean res = productServiceApplication.insertProduct(pi);
 
        Assert.assertEquals(true, res);
    }    


}