/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import datamediator.PostgreSQLMediator;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
          
          ResultSet rs = stmt.executeQuery("SELECT id , name , pageurl FROM brand");

          ArrayList<String> output = new ArrayList<>();
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
            @RequestParam(value="photos", required=false) List<Integer> photos
        ) {
        // if id == "" ==> enduser != ""
        // create
        // if enduser == "" ==> id != ""
        // update
        return null;
    }
    
    // Note that avatar needs to be managed.
    @RequestMapping(value = "/enduserOld", method = RequestMethod.POST) 
    public @ResponseBody String postEnduser(
           @RequestParam(value="id", required=false, defaultValue="-1") int enduserID,
           @RequestParam(value="federationId", required=false, defaultValue="") String federationID,
           @RequestParam(value="profileName", required=false, defaultValue="") String profileName,
           @RequestParam(value="recoveryEmail", required=false, defaultValue="") String recoveryEmail,
           @RequestParam(value="avatarUrl", required=false, defaultValue="") String avatarUrl,
           Map<String, Object> model
             ) {
        int nRowUpdated = 0;
        // insert
        if (enduserID == -1){
            if (!federationID.isEmpty() && !profileName.isEmpty() && !recoveryEmail.isEmpty()){
                String updateString =
                    "INSERT INTO enduser " +
                    "(federationId, profileName, recoveryEmail, avatarUrl)" +
                    " VALUES (?, ?, ?, ?)" +
                    "RETURNING id";

                try (Connection connection = connectionPool.getConnection()){
                    PreparedStatement updateSql = connection.prepareStatement(updateString);

                    updateSql.setString(1, federationID);
                    updateSql.setString(2, profileName);
                    updateSql.setString(3, recoveryEmail);
                    updateSql.setString(4, avatarUrl);

                    ResultSet rs = updateSql.executeQuery();
                    rs.next();
                    nRowUpdated = rs.getInt(1);

                } catch (Exception e) {
                    return e.getMessage();
                }
            }
            // Esle error TODO
        }
        // update
        else {
            // TODO manage when fields do not change
            // Use a map, Map<int,Object>  order,value
            // Refactor in an SQL Wrapper (SELECT1,FIND,INSERT/UPDATE,DELETE,Merge)
            String updateString =
                "UPDATE enduser " +
                "SET federationId = ? , "+
                " profileName = ? , "+
                " recoveryEmail = ? , "+
                " avatarUrl = ? "+
                "WHERE id = ?";

            try (Connection connection = connectionPool.getConnection()){
                PreparedStatement updateSql = connection.prepareStatement(updateString);

                updateSql.setString(1, federationID);
                updateSql.setString(2, profileName);
                updateSql.setString(3, recoveryEmail);
                updateSql.setString(4, avatarUrl);
                updateSql.setInt(5, enduserID);
                
                nRowUpdated = updateSql.executeUpdate();
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return ""+nRowUpdated;
    }
    
    
    @RequestMapping(value = "/enduserOld", method = RequestMethod.GET) 
    public @ResponseBody Map<String, Object> getEnduser(
           @RequestParam(value="id", required=true) int enduserID
             ) {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = connectionPool.getConnection()) {
          String qrySelect = "SELECT id,federationId,profileName,recoveryEmail,avatarUrl "
                        + "FROM enduser "
                        + "WHERE id = ?";
          PreparedStatement stmt = connection.prepareStatement(qrySelect);
          stmt.setInt(1, enduserID);
          ResultSet rs = stmt.executeQuery();
          
          while (rs.next()) {
            result.put("enduserID",rs.getInt("id"));
            result.put("federationId",rs.getString("federationId"));
            result.put("profileName",rs.getString("profileName"));
            result.put("recoveryEmail",rs.getString("recoveryEmail"));
            result.put("avatarUrl",rs.getString("avatarUrl"));
            
            break;
          }

          return result;
        } catch (Exception e) {
          result.put("message", e.getMessage());
          result.put("exception", e);
          return result;
        }
    }
    
    /** Find requests, the filters are ands, to get the full spectrum of a
     * select, the ors are obtained joining to querie
     * @param brandID
     * @param name
     * @param pageurl
     * @return s*/
    @RequestMapping(value = "/brands", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> getFindBrands(
            @RequestParam(value="id", required=false, defaultValue="-1") int brandID,
           @RequestParam(value="name", required=false, defaultValue="") String name,
           @RequestParam(value="pageurl", required=false, defaultValue="") String pageurl
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectionPool);
        sm.setTable("brand");
        sm.addFindField("id");
        sm.addFindField("name");
        sm.addFindField("pageurl");
        if (brandID >= 0){
            sm.addId(brandID);
        }
        if (!name.isEmpty()){
            sm.addFindParam("name", name, 1);
        }
        if (!pageurl.isEmpty()){
            sm.addFindParam("pageurl", pageurl, 1);
        }
        sm.runFind();
        List<Map<String, Object>> result = sm.getResultsFind();
        HashMap m = new HashMap<>();
        m.put("Query", sm.getLastQuery());
        result.add(m);
        return result;
        //return sm.getResultsFind();
    }
    
    
    /** Find requests, the filters are ands, to get the full spectrum of a
     * select, the ors are obtained joining to querie
     * @param enduserID
     * @param federationID
     * @param profileName
     * @param recoveryEmail
     * @param avatarUrl
     * @return s*/
    @RequestMapping(value = "/endusers", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> getFindEndusers(
            @RequestParam(value="id", required=false, defaultValue="-1") int enduserID,
           @RequestParam(value="federationId", required=false, defaultValue="") String federationID,
           @RequestParam(value="profileName", required=false, defaultValue="") String profileName,
           @RequestParam(value="recoveryEmail", required=false, defaultValue="") String recoveryEmail,
           @RequestParam(value="avatarUrl", required=false, defaultValue="") String avatarUrl
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectionPool);
        sm.setTable("enduser");
        sm.addFindField("id");
        sm.addFindField("federationId");
        sm.addFindField("profileName");
        sm.addFindField("recoveryEmail");
        sm.addFindField("avatarUrl");
        if (enduserID >= 0){
            sm.addId(enduserID);
        }
        if (!federationID.isEmpty()){
            sm.addFindParam("federationId", federationID, 1);
        }
        if (!profileName.isEmpty()){
            sm.addFindParam("profileName", profileName, 1);
        }
        if (!recoveryEmail.isEmpty()){
            sm.addFindParam("recoveryEmail", recoveryEmail, 1);
        }
        if (!avatarUrl.isEmpty()){
            sm.addFindParam("avatarUrl", avatarUrl, 1);
        }
        sm.runFind();
        return sm.getResultsFind();
    }
    
    /** Find requests, the filters are ands, to get the full spectrum of a
     * select, the ors are obtained joining to querie
     * @param enduserID
     * @param federationID
     * @param profileName
     * @param recoveryEmail
     * @param avatarUrl
     * @return s*/
    @RequestMapping(value = "/enduser", method = RequestMethod.POST)
    public @ResponseBody String getUpsertEndusers(
            @RequestParam(value="id", required=false, defaultValue="-1") int enduserID,
           @RequestParam(value="federationId", required=false, defaultValue="") String federationID,
           @RequestParam(value="profileName", required=false, defaultValue="") String profileName,
           @RequestParam(value="recoveryEmail", required=false, defaultValue="") String recoveryEmail,
           @RequestParam(value="avatarUrl", required=false, defaultValue="") String avatarUrl
    ){
        
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectionPool);
        sm.setTable("enduser");
        if (enduserID >= 0){
            sm.addId(enduserID);
        }
        if (!federationID.isEmpty()){
            sm.addUpsertParam("federationId", federationID);
        }
        if (!profileName.isEmpty()){
            sm.addUpsertParam("profileName", profileName);
        }
        if (!recoveryEmail.isEmpty()){
            sm.addUpsertParam("recoveryEmail", recoveryEmail);
        }
        if (!avatarUrl.isEmpty()){
            sm.addUpsertParam("avatarUrl", avatarUrl);
        }
        sm.runUpsert();
        return sm.getId();
    }
}


