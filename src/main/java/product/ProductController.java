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
        sm.setTable("brand")
                .addFindField("id")
                .addFindField("name")
                .addFindField("pageurl");
        if (brandID >= 0){
            sm.addFindParam("id", brandID, 1);
        }
        if (!name.isEmpty()){
            sm.addFindParam("name", name, 1);
        }
        if (!pageurl.isEmpty()){
            sm.addFindParam("pageurl", pageurl, 1);
        }
        sm.runFind();
        return sm.getResultsFind();
    }
    
    
    /** Find requests, the filters are ands, to get the full spectrum of a
     * select, the ors are obtained joining to querie
     * @param brandID
     * @param name
     * @param pageurl
     * @return s*/
    @RequestMapping(value = "/brand", method = RequestMethod.POST)
    public @ResponseBody String getUpsertBrand(
            @RequestParam(value="id", required=false, defaultValue="-1") int brandID,
           @RequestParam(value="name", required=false, defaultValue="") String name,
           @RequestParam(value="pageurl", required=false, defaultValue="") String pageurl
    ){
        
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectionPool);
        sm.setTable("brand");
        if (brandID >= 0){
            sm.addId(brandID);
        }
        if (!name.isEmpty()){
            sm.addUpsertParam("name", name);
        }
        if (!pageurl.isEmpty()){
            sm.addUpsertParam("pageurl", pageurl);
        }
        sm.runUpsert();
        return sm.getId();
    }
    
    /** Find requests, the filters are ands, to get the full spectrum of a
     * select, the ors are obtained joining to querie
     * @param brandID
     * @return s*/
    @RequestMapping(value = "/brand", method = RequestMethod.DELETE)
    public @ResponseBody String getDeleteBrand(
            @RequestParam(value="id", required=true) int brandID
    ){
        
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectionPool);
        sm.setTable("brand")
                .addFindParam("id", brandID, 1);
        sm.runDelete();
        return ""+brandID;
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
            sm.addFindParam("id",enduserID, 1);
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


