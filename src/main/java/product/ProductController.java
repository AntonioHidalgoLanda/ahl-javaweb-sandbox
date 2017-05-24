/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

    private BasicDataSource connectionPool;
    
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
    
    @RequestMapping("/pocdb")
    String db(Map<String, Object> model) {
        try (Connection connection = connectionPool.getConnection()) {
          Statement stmt = connection.createStatement();
          // DO NOT COMMIT // stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
          // DO NOT COMMIT // stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
          
          ResultSet rs = stmt.executeQuery("SELECT id,name,pageurl FROM brand");

          ArrayList<String> output = new ArrayList<String>();
          while (rs.next()) {
            output.add("{id:" + rs.getInt("id")
                    + ", name:" + rs.getInt("name")
                    + ", pageurl:" + rs.getInt("pageurl")+"}");
          }

          model.put("records", output);
          return "db";
        } catch (Exception e) {
          model.put("message", e.getMessage());
          return "error "+  e.getMessage();
        }
    }
    
    @Bean
    public DataSource dataSource() throws SQLException, URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":"
                + dbUri.getPort() + dbUri.getPath();
        connectionPool = new BasicDataSource();

        if (dbUri.getUserInfo() != null) {
          connectionPool.setUsername(dbUri.getUserInfo().split(":")[0]);
          connectionPool.setPassword(dbUri.getUserInfo().split(":")[1]);
        }
        connectionPool.setDriverClassName("org.postgresql.Driver");
        connectionPool.setUrl(dbUrl);
        connectionPool.setInitialSize(1);
        return this.connectionPool;
    }

}


