/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
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
    List<String> db(Map<String, Object> model) {
        try (Connection connection = connectionPool.getConnection()) {
          Statement stmt = connection.createStatement();
          // DO NOT COMMIT // stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
          // DO NOT COMMIT // stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
          
          ResultSet rs = stmt.executeQuery("SELECT id,name,pageurl FROM brand");

          ArrayList<String> output = new ArrayList<String>();
          while (rs.next()) {
            output.add("{id:" + rs.getInt("id")
                    + ", name:" + rs.getString("name")
                    + ", pageurl:" + rs.getString("pageurl")+"}");
          }

          model.put("records", output);
          return output;
        } catch (Exception e) {
          model.put("message", e.getMessage());
          return null;
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
    
    /* REST Methods THE SHOULD GO in another class */
    // we cannot overload the function in traditional way, and we need check the
    // parameters
    // note ISO_OFFSET_DATE_TIME 	Date Time with Offset 	2011-12-03T10:15:30+01:00'
    @RequestMapping(value = "/igotit", method = RequestMethod.POST) 
    public @ResponseBody Map<String, Object> postIgotit(
            @RequestParam(value="id", required=false, defaultValue="") int igotitID,
            @RequestParam(value="publishdate", required=false) @DateTimeFormat(pattern="ISO_OFFSET_DATE_TIME") Date publishdate,
            @RequestParam(value="enduserid", required=false) int enduserid,
            @RequestParam(value="visibility", required=false, defaultValue="0") int visibility,
            @RequestParam(value="usercomment", required=false, defaultValue="") String usercomment,
            @RequestParam(value="coordinates", required=false, defaultValue="") String coordinates,
            @RequestParam(value="rating", required=false, defaultValue="0") String rating,
            @RequestParam(value="photos", required=false) List<Long> photos
        ) {
        // if id == "" ==> enduser != ""
        // create
        // if enduser == "" ==> id != ""
        // update
        return null;
    }
    
    @RequestMapping(value = "/enduser", method = RequestMethod.POST) 
    public @ResponseBody Map<String, Object> postEnduser(
           @RequestParam(value="id", required=false, defaultValue="-1") int enduserID,
           @RequestParam(value="federationId", required=false, defaultValue="") String federationID,
           @RequestParam(value="profileName", required=false, defaultValue="") String profileName,
           @RequestParam(value="recoveryEmail", required=false, defaultValue="") String recoveryEmail,
           @RequestParam(value="avatarUrl", required=false, defaultValue="") String avatarUrl,
           Map<String, Object> model
             ) {
        // if id == "" ==> (federationID !="" && profileName!="" && recoveryEmail!="")
        // create
        if (enduserID == -1){
            if (!federationID.isEmpty() && !profileName.isEmpty() && !recoveryEmail.isEmpty()){
                
            }
            // Esle error TODO
        }
        // else
        // update
        else {
            
        }
        return null;
    }
    
    
    @RequestMapping(value = "/enduser", method = RequestMethod.GET) 
    public @ResponseBody Map<String, Object> getEnduser(
           @RequestParam(value="id", required=true, defaultValue="") int enduserID,
           Map<String, Object> model
             ) {
        try (Connection connection = connectionPool.getConnection()) {
          String qrySelect = "SELECT id,federationId,profileName,recoveryEmail,avatarUrl "
                        + "FROM enduser "
                        + "WHERE id = ?";
          PreparedStatement stmt = connection.prepareStatement(qrySelect);
          stmt.setInt(1, enduserID);
          ResultSet rs = stmt.executeQuery();
          
          while (rs.next()) {
            model.put("enduserID",rs.getInt("id"));
            model.put("federationId",rs.getString("federationId"));
            model.put("profileName",rs.getString("profileName"));
            model.put("recoveryEmail",rs.getString("recoveryEmail"));
            model.put("avatarUrl",rs.getString("avatarUrl"));
            
            break;
          }

          return model;
        } catch (Exception e) {
          model.put("message", e.getMessage());
          return null;
        }
    }
    
}


