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
import product.CountryHelper;

/**
 *
 * @author antonio
 */
@RestController
public class StoreService {
    
    BasicDataSource connectorPool = null;
    
    StoreService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }
    
    @RequestMapping(value = "/stores", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int storeID,
           @RequestParam(value="resellerId", required=false, defaultValue="0") int resellerId,
           @RequestParam(value="numberStreet", required=false, defaultValue="") String numberStreet,
           @RequestParam(value="address1", required=false, defaultValue="") String address1,
           @RequestParam(value="address2", required=false, defaultValue="") String address2,
           @RequestParam(value="city", required=false, defaultValue="") String city,
           @RequestParam(value="stateProvince", required=false, defaultValue="") String stateProvince,
           @RequestParam(value="country", required=false, defaultValue="") String country,
           @RequestParam(value="postCode", required=false, defaultValue="") String postCode,
           @RequestParam(value="contactEmail", required=false, defaultValue="") String contactEmail,
           @RequestParam(value="contactPhone", required=false, defaultValue="") String contactPhone,
           @RequestParam(value="contactName", required=false, defaultValue="") String contactName
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("store")
                .addFindField("id")
                .addFindField("resellerId")
                .addFindField("numberStreet")
                .addFindField("address1")
                .addFindField("address2")
                .addFindField("city")
                .addFindField("stateProvince")
                .addFindField("country")
                .addFindField("postCode")
                .addFindField("contactEmail")
                .addFindField("contactPhone")
                .addFindField("contactName");
        if (storeID >= 0){
            sm.addFindParam("id", storeID, 1);
        }
        if (resellerId > 0){
            sm.addFindParam("resellerId", resellerId, 1);
        }
        if (!numberStreet.isEmpty()){
            sm.addFindParam("numberStreet", numberStreet, 1);
        }
        if (!address1.isEmpty()){
            sm.addFindParam("address1", address1, 1);
        }
        if (!address2.isEmpty()){
            sm.addFindParam("address2", address2, 1);
        }
        if (!city.isEmpty()){
            sm.addFindParam("city", city, 1);
        }
        if (!stateProvince.isEmpty()){
            sm.addFindParam("stateProvince", stateProvince, 1);
        }
        if (!country.isEmpty()){
            sm.addFindParam("country", CountryHelper.getIsoCode(country), 1);
        }
        if (!postCode.isEmpty()){
            sm.addFindParam("postCode", postCode, 1);
        }
        if (!contactEmail.isEmpty()){
            sm.addFindParam("contactEmail", contactEmail, 1);
        }
        if (!contactPhone.isEmpty()){
            sm.addFindParam("contactPhone", contactPhone, 1);
        }
        if (!contactName.isEmpty()){
            sm.addFindParam("contactName", contactName, 1);
        }
        sm.runFind();
        List<Map<String, Object>> result = sm.getResultsFind();
        result.stream().forEach((obj) -> {
            obj.put("productList", this.findProducts((Integer)obj.get("id")));
        });
        return result;
    }
    
    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int storeID,
           @RequestParam(value="resellerId", required=false, defaultValue="0") int resellerId,
           @RequestParam(value="numberStreet", required=false, defaultValue="") String numberStreet,
           @RequestParam(value="address1", required=false, defaultValue="") String address1,
           @RequestParam(value="address2", required=false, defaultValue="") String address2,
           @RequestParam(value="city", required=false, defaultValue="") String city,
           @RequestParam(value="stateProvince", required=false, defaultValue="") String stateProvince,
           @RequestParam(value="country", required=false, defaultValue="") String country,
           @RequestParam(value="postCode", required=false, defaultValue="") String postCode,
           @RequestParam(value="contactEmail", required=false, defaultValue="") String contactEmail,
           @RequestParam(value="contactPhone", required=false, defaultValue="") String contactPhone,
           @RequestParam(value="contactName", required=false, defaultValue="") String contactName
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("store");
        if (storeID >= 0){
            sm.addId(storeID);
        }
        if (resellerId > 0){
            sm.addUpsertParam("resellerId", resellerId);
        }
        if (!numberStreet.isEmpty()){
            sm.addUpsertParam("numberStreet", numberStreet);
        }
        if (!address1.isEmpty()){
            sm.addUpsertParam("address1", address1);
        }
        if (!address2.isEmpty()){
            sm.addUpsertParam("address2", address2);
        }
        if (!city.isEmpty()){
            sm.addUpsertParam("city", city);
        }
        if (!stateProvince.isEmpty()){
            sm.addUpsertParam("stateProvince", stateProvince);
        }
        if (!country.isEmpty()){
            sm.addUpsertParam("country", CountryHelper.getIsoCode(country));
        }
        if (!postCode.isEmpty()){
            sm.addUpsertParam("postCode", postCode);
        }
        if (!contactEmail.isEmpty()){
            sm.addUpsertParam("contactEmail", contactEmail);
        }
        if (!contactPhone.isEmpty()){
            sm.addUpsertParam("contactPhone", contactPhone);
        }
        if (!contactName.isEmpty()){
            sm.addUpsertParam("contactName", contactName);
        }
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/store", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int storeId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("store")
                .addFindParam("id", storeId, 1);
        sm.runDelete();
        return ""+storeId;
    }
    
    public List<Integer> getProducts(int storeId){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("storeProduct")
                .setAccessId("storeId")
                .setAccessTable("store")
                .addFindField("productId")
                .addFindField("storeId")
                .addFindParam("storeId", storeId, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("productId"));
        });
        return listInt;
    }
    
    @RequestMapping(value = "/store/products", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findProducts(
            @RequestParam(value="id", required=true) int storeId
    ){
        return this.getProducts(storeId);
    }
    
    @RequestMapping(value = "/store/product", method = RequestMethod.POST)
    public @ResponseBody String upsertProduct(
           @RequestParam(value="id", required=true) int storeId,
           @RequestParam(value="productId", required=true) int productId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("storeProduct")
                .setAccessId("storeId")
                .setAccessTable("store");
        sm.addUpsertParam("productId", productId)
          .addUpsertParam("storeId", storeId);
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/store/product", method = RequestMethod.DELETE)
    public @ResponseBody String deleteProduct(
           @RequestParam(value="id", required=true) int storeId,
           @RequestParam(value="productId", required=true) int productId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("storeProduct")
                .setAccessId("storeId")
                .setAccessTable("store")
                .addFindParam("storeId", storeId, 1)
                .addFindParam("productId", productId, 1);
        sm.runDelete();
        return ""+storeId+":"+productId;
    }
}
