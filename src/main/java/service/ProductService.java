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
           @RequestParam(value="brandLink", required=false, defaultValue="") String brandLink,
           @RequestParam(value="extended", required=false, defaultValue="true") boolean bextended
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
        List<Map<String, Object>> result = sm.getResultsFind();
        result.stream().forEach((obj) -> {
            int id = (Integer)obj.get("id");
            if(bextended){
                obj.put("shoppingOnlineLinkList", this.findShoppingOnlineLink(id));
                obj.put("storeList", this.findStore(id));
                obj.put("igotitList", this.findIgotit(id));
            }
        });
        return result;
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
    
    public List<Integer> getShoppingOnlineLink(int productId){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("shoppingOnlineLink")
                .addFindField("id")
                .addFindField("productId")
                .addFindParam("productId", productId, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("id"));
        });
        return listInt;
    }
    
    public List<Integer> getStore(int productId){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("storeProduct")
                .turnIdOff()
                .setAccessId("storeId")
                .setAccessTable("store")
                .addFindField("storeId")
                .addFindField("productId")
                .addFindParam("productId", productId, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("storeId"));
        });
        return listInt;
    }
    
    public List<Integer> getIgotit(int productId){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotitProduct")
                .turnIdOff()
                .setAccessId("igotitId")
                .setAccessTable("igotit")
                .addFindField("igotitId")
                .addFindField("productId")
                .addFindParam("productId", productId, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("igotitId"));
        });
        return listInt;
    }
    
    @RequestMapping(value = "/product/shoppingOnlineLinks", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findShoppingOnlineLink(
            @RequestParam(value="id", required=true) int productId
    ){
        return this.getShoppingOnlineLink(productId);
    }
    
    @RequestMapping(value = "/product/stores", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findStore(
            @RequestParam(value="id", required=true) int productId
    ){
        return this.getStore(productId);
    }
    
    @RequestMapping(value = "/product/igotits", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findIgotit(
            @RequestParam(value="id", required=true) int productId
    ){
        return this.getIgotit(productId);
    }
    
    @RequestMapping(value = "/product/shoppingOnlineLink", method = RequestMethod.POST)
    public @ResponseBody String upsertShoppingOnlineLink(
           @RequestParam(value="id", required=true) int productId,
           @RequestParam(value="shoppingOnlineLinkId", required=true) int shoppingOnlineLinkId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("shoppingOnlineLink");
        sm.addId(shoppingOnlineLinkId)
             .addUpsertParam("productId", productId);
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/product/store", method = RequestMethod.POST)
    public @ResponseBody String upsertStore(
           @RequestParam(value="id", required=true) int productId,
           @RequestParam(value="storeId", required=true) int storeId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("storeProduct")
                .turnIdOff()
                .setAccessId("storeId")
                .setAccessTable("store");
        sm.addUpsertParam("storeId", storeId)
          .addUpsertParam("productId", productId);
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/product/igotit", method = RequestMethod.POST)
    public @ResponseBody String upsertIgotit(
           @RequestParam(value="id", required=true) int productId,
           @RequestParam(value="igotitId", required=true) int igotitId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotitProduct")
                .turnIdOff()
                .setAccessTable("igotit")
                .setAccessId("igotitId");
        sm.addUpsertParam("igotitId", igotitId)
          .addUpsertParam("productId", productId);
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/product/shoppingOnlineLink", method = RequestMethod.DELETE)
    public @ResponseBody String deleteShoppingOnlineLink(
           @RequestParam(value="id", required=true) int productId,
           @RequestParam(value="shoppingOnlineLinkId", required=true) int shoppingOnlineLinkId
    ){
        IgotItService ps = new IgotItService();
        ps.delete(shoppingOnlineLinkId);
        return ""+productId+":"+shoppingOnlineLinkId;
    }
    
    @RequestMapping(value = "/product/store", method = RequestMethod.DELETE)
    public @ResponseBody String deleteStore(
           @RequestParam(value="id", required=true) int productId,
           @RequestParam(value="storeId", required=true) int storeId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("storeProduct")
                .turnIdOff()
                .setAccessId("storeId")
                .setAccessTable("store")
                .addFindParam("storeId", storeId, 1)
                .addFindParam("productId", productId, 1);
        sm.runDelete();
        return ""+productId+":"+storeId;
    }
    
    @RequestMapping(value = "/product/igotit", method = RequestMethod.DELETE)
    public @ResponseBody String deleteIgotit(
           @RequestParam(value="id", required=true) int productId,
           @RequestParam(value="igotitId", required=true) int igotitId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotitProduct")
                .turnIdOff()
                .setAccessId("igotitId")
                .setAccessTable("igotit")
                .addFindParam("igotitId", igotitId, 1)
                .addFindParam("productId", productId, 1);
        sm.runDelete();
        return ""+productId+":"+igotitId;
    }
    
}
