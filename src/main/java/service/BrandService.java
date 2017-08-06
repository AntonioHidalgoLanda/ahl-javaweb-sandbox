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
public class BrandService {
    
    BasicDataSource connectorPool = null;
    
    BrandService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }
    
    /** Find requests, the filters are ands, to get the full spectrum of a
     * select, the ors are obtained joining to querie
     * @param brandID
     * @param name
     * @param pageurl
     * @param bextended if true (default) will also return the products which 
     * is an expensive operation in a generic search
     * @return s*/
    @RequestMapping(value = "/brands", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int brandID,
           @RequestParam(value="name", required=false, defaultValue="") String name,
           @RequestParam(value="pageurl", required=false, defaultValue="") String pageurl,
           @RequestParam(value="extended", required=false, defaultValue="true") boolean bextended
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
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
        List<Map<String, Object>> result = sm.getResultsFind();
        if(bextended){
            result.stream().forEach((obj) -> {
                obj.put("productList", this.findProducts((Integer)obj.get("id")));
            });
        }
        return result;
    }
    
    
    /** Find requests, the filters are ands, to get the full spectrum of a
     * select, the ors are obtained joining to querie
     * @param brandID
     * @param name
     * @param pageurl
     * @return s*/
    @RequestMapping(value = "/brand", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int brandID,
           @RequestParam(value="name", required=false, defaultValue="") String name,
           @RequestParam(value="pageurl", required=false, defaultValue="") String pageurl
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
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
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int brandID
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("brand")
                .addFindParam("id", brandID, 1);
        sm.runDelete();
        return ""+brandID;
    }
    
    // find products of a brand
    public List<Integer> getProducts(int brandid){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("product")
                .addFindField("id")
                .addFindField("brandid")
                .addFindParam("brandid", brandid, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("id"));
        });
        return listInt;
    }
    
    // find product of a brand API
    @RequestMapping(value = "/brand/products", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findProducts(
            @RequestParam(value="id", required=true) int brandID
    ){
        return this.getProducts(brandID);
    }
    
    // enter a new record in catalog
    @RequestMapping(value = "/brand/product", method = RequestMethod.POST)
    public @ResponseBody String upsertProduct(
           @RequestParam(value="id", required=true) int brandid,
           @RequestParam(value="productId", required=true) int productId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("product");
        sm.addId(productId)
             .addUpsertParam("brandid", brandid);
        sm.runUpsert();
        return sm.getId();
    }
    
    // delete a record from catalog
    @RequestMapping(value = "/brand/product", method = RequestMethod.DELETE)
    public @ResponseBody String deleteProduct(
           @RequestParam(value="id", required=true) int brandid,
           @RequestParam(value="productId", required=true) int productId
    ){
        ProductService ps = new ProductService();
        ps.delete(productId);
        return ""+brandid+":"+productId;
    }
    
}
