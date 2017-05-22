/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * @author antonio
 */
@RestController
public class ProductController {
    
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
    @RequestMapping("/sku")
    public @ResponseBody Product getProductFromSku(
            @RequestParam(value="sku", required=true, defaultValue="Unknown") String sku,
            @RequestParam(value="serialNumber", required=false, defaultValue="") String serialNumber,
            @RequestParam(value="description", required=false, defaultValue="") String description
            ) {
        return new Product(sku, serialNumber,description, 0);
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public @ResponseBody Product postProduct(
            @RequestParam(value="sku", required=true, defaultValue="Unknown") String sku,
            @RequestParam(value="serialNumber", required=false, defaultValue="") String serialNumber,
            @RequestParam(value="description", required=false, defaultValue="") String description
        ) {
        return new Product(sku, serialNumber,description, 0);
    }
}


