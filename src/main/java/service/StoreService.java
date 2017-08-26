/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import datamediator.DataSourceSingleton;
import datamediator.PostgreSQLMediator;
import datamediator.SqlEntityMediator;
import datamediator.SqlMediator;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class StoreService  implements SqlEntityMediator{
    
    BasicDataSource connectorPool = null;
    
    StoreService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public SqlMediator getSqlMediator() {
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
        return sm;
    }

    public SqlMediator getStoreProductSqlMediator() {
        SqlMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("storeProduct")
                .turnIdOff()
                .setAccessId("storeId")
                .setAccessTable("store")
                .addFindField("productId")
                .addFindField("storeId");
        return sm;
    }

    @Override
    public SqlEntityMediator grantAccess(int entityId, List<Integer> listUsers) {
        SqlMediator sm = this.getSqlMediator();
        sm.grantAccess(true, Arrays.asList(entityId), listUsers);
        return this;
    }

    @Override
    public SqlEntityMediator grantAccess(int entityId) {
        SqlMediator sm = this.getSqlMediator();
        if(!sm.hasAccessNoInitialized(entityId)){
            sm.grantAccessAllUsers(true, Arrays.asList(entityId));
            sm.grantAccess(false, Arrays.asList(entityId));
        }
        return this;
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId, List<Integer> listUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId) {
        SqlMediator sm = this.getSqlMediator();
        sm.revokeAccessAllUsers(Arrays.asList(entityId));
        return this;
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
        SqlMediator sm = this.getSqlMediator();
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
        SqlMediator sm = this.getSqlMediator();
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
        int newId = (storeID>0)?storeID:Integer.parseInt(sm.getId());
        this.grantAccess(newId);
        if (resellerId>0){
            sm.revokeAccessAllUsers(Arrays.asList(newId));
            ResellerService rs = new ResellerService();
            SqlMediator rssm = rs.getSqlMediator();
            List<Integer> listUsers = rssm.getUserAccess(true, resellerId);
            this.grantAccess(newId, listUsers);
            listUsers = rssm.getUserAccess(false, resellerId);
            this.grantAccess(newId, listUsers);
        }
        return sm.getId();
    }
    
    @RequestMapping(value = "/store", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int storeId
    ){
        this.deleteProduct(storeId, -1);
        this.revokeAccess(storeId);
        SqlMediator sm = this.getSqlMediator();
        sm.addFindParam("id", storeId, 1)
                .runDelete();
        return ""+storeId;
    }
    
    public List<Integer> getProducts(int storeId){
        SqlMediator sm = this.getStoreProductSqlMediator();
        sm.addFindParam("storeId", storeId, 1)
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
        SqlMediator sm = this.getStoreProductSqlMediator();
        sm.addUpsertParam("productId", productId)
          .addUpsertParam("storeId", storeId)
          .runUpsert();
        // Do not need to revoke anything
        return sm.getId();
    }
    
    @RequestMapping(value = "/store/product", method = RequestMethod.DELETE)
    public @ResponseBody String deleteProduct(
           @RequestParam(value="id", required=true) int storeId,
           @RequestParam(value="productId", required=false, defaultValue="-1") int productId
    ){
        // Do not need to revoke anything
        ProductService ps = new ProductService();
        if (productId > 0){
            ps.delete(productId);
        }
        else{
            List<Integer> search = this.findProducts(storeId);
            search.stream().forEach((item) -> {
                ps.delete(item);
            }); 
        }
        return ""+storeId+":"+productId;
    }
}
