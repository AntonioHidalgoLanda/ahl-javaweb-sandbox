/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import datamediator.DataSourceSingleton;
import datamediator.PostgreSQLMediator;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author antonio
 */
@RestController
public class ProductService {
    /*
CREATE TABLE product (
  id SERIAL PRIMARY KEY,
  sku varchar (255) not null,
  brandid  int4 not null references brand (id),
  brandedIgotitId int4 not null references igotit(id), -- reference post from the brand (has priority)
  name varchar (255) not null,
  brandLink varchar (255) -- see the product in brand page
);

CREATE TABLE storeProduct (
  productId int4 not null references product(id),
  storeId int4 not null references store(id)
);*/
    
    BasicDataSource connectorPool = null;
    
    ProductService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }
    
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int productID,
           @RequestParam(value="sku", required=false, defaultValue="") String sku,
           @RequestParam(value="brandid", required=false, defaultValue="0") int brandid,
           @RequestParam(value="brandedIgotitId", required=false, defaultValue="0") int brandedIgotitId,
           @RequestParam(value="name", required=false, defaultValue="") String name,
           @RequestParam(value="brandLink", required=false, defaultValue="") String brandLink
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("product")
                .addFindField("id")
                .addFindField("sku")
                .addFindField("brandid")
                .addFindField("brandedIgotitId")
                .addFindField("name")
                .addFindField("brandLink");
        if (productID >= 0){
            sm.addFindParam("id", productID, 1);
        }
        if (!sku.isEmpty()){
            sm.addFindParam("sku", sku, 1);
        }
        if (brandid > 0){
            sm.addFindParam("brandid", brandid, 1);
        }
        if (brandedIgotitId > 0){
            sm.addFindParam("federationId", brandedIgotitId, 1);
        }
        if (!name.isEmpty()){
            sm.addFindParam("name", name, 1);
        }
        if (!brandLink.isEmpty()){
            sm.addFindParam("brandLink", brandLink, 1);
        }
        sm.runFind();
        return sm.getResultsFind();
    }
    
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int productID,
           @RequestParam(value="sku", required=false, defaultValue="") String sku,
           @RequestParam(value="brandid", required=false, defaultValue="0") int brandid,
           @RequestParam(value="brandedIgotitId", required=false, defaultValue="0") int brandedIgotitId,
           @RequestParam(value="name", required=false, defaultValue="") String name,
           @RequestParam(value="brandLink", required=false, defaultValue="") String brandLink
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("product");
        if (productID >= 0){
            sm.addId(productID);
        }
        if (!sku.isEmpty()){
            sm.addUpsertParam("sku", sku);
        }
        if (brandid > 0){
            sm.addUpsertParam("brandid", brandid);
        }
        if (brandedIgotitId > 0){
            sm.addUpsertParam("federationId", brandedIgotitId);
        }
        if (!name.isEmpty()){
            sm.addUpsertParam("name", name);
        }
        if (!brandLink.isEmpty()){
            sm.addUpsertParam("brandLink", brandLink);
        }
        sm.runUpsert();
        return sm.getId();
    }
    
    
    @RequestMapping(value = "/product", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int productID
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("product")
                .addFindParam("id", productID, 1);
        sm.runDelete();
        return ""+productID;
    }
}
