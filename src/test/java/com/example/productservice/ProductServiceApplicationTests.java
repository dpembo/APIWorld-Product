package com.example.productservice;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.URI;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import junit.framework.Assert;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceApplicationTests {
 
    @Test
    public void testGetEmployeeListSuccess() throws URISyntaxException
    {
        RestTemplate restTemplate = new RestTemplate();
         
        final String baseUrl = "http://localhost:" + 8090 + "/product?id=1";
        URI uri = new URI(baseUrl);
     
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
         
        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("productName"));
    }

}