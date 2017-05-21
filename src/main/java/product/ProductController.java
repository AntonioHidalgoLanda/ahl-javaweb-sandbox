/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

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

}


