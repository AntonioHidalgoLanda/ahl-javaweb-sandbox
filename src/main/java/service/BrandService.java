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

/**
 *
 * @author antonio
 */
@RestController
public class BrandService implements SqlEntityMediator{
    
    BasicDataSource connectorPool = null;
    
    BrandService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public SqlMediator getSqlMediator() {
        
        SqlMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("brand")
                .addFindField("id")
                .addFindField("name")
                .addFindField("pageurl");
        return sm;
    }

    @Override
    public SqlEntityMediator grantAccess(int entityId, List<Integer> listUsers) {
        /*
            
            // overriding current
            sm.revokeAccessAllUsers(Arrays.asList(newId));
            sm.grantAccessAllUsers(false, Arrays.asList(newId));
        */
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // ignoring grant if there are values
    @Override
    public SqlEntityMediator grantAccess(int entityId) {
        SqlMediator sm = this.getSqlMediator();
        if(!sm.hasAccessNoInitialized(entityId)){
            sm.grantAccessAllUsers(false, Arrays.asList(entityId));
        }
        return this;
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId, List<Integer> listUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // revokes all access
    @Override
    public SqlEntityMediator revokeAccess(int entityId) {
        SqlMediator sm = this.getSqlMediator();
        sm.revokeAccessAllUsers(Arrays.asList(entityId));
        return this;
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
        SqlMediator sm = this.getSqlMediator();
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
        System.err.println("BRAND FIND");
        System.err.println(((PostgreSQLMediator)sm).getLastQuery());
        System.err.println("name: '"+name+"'");
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
        SqlMediator sm = this.getSqlMediator();
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
        int newId = (brandID>0)?brandID:Integer.parseInt(sm.getId());
        this.grantAccess(newId);
        
        return ""+newId;
    }
    
    /** Find requests, the filters are ands, to get the full spectrum of a
     * select, the ors are obtained joining to querie
     * @param brandID
     * @return s*/
    @RequestMapping(value = "/brand", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int brandID
    ){
        this.deleteProduct(brandID, -1);
        this.revokeAccess(brandID);
        SqlMediator sm = this.getSqlMediator();
        sm.addFindParam("id", brandID, 1);
        sm.runDelete();
        return ""+brandID;
    }
    
    // find products of a brand
    public List<Integer> getProducts(int brandid){
        ProductService ps = new ProductService();
        List<Map<String, Object>> listObject = ps.find(-1, "", brandid, -1, "", "", false);
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
        ProductService ps = new ProductService();
        return ps.upsert(productId, "", brandid, -1, "", "");
    }
    
    // delete a record from catalog
    @RequestMapping(value = "/brand/product", method = RequestMethod.DELETE)
    public @ResponseBody String deleteProduct(
           @RequestParam(value="id", required=true) int brandid,
           @RequestParam(value="productId", required=false, defaultValue="-1") int productId
    ){
        ProductService ps = new ProductService();
        if (productId > 0){
            ps.delete(productId);
        }
        else{
            List<Integer> search = this.findProducts(brandid);
            search.stream().forEach((item) -> {
                ps.delete(item);
            }); 
        }
        return ""+brandid+":"+productId;
    }
    
}
