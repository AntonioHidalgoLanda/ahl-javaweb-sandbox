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
public class ResellerService  implements SqlEntityMediator{
    
    BasicDataSource connectorPool = null;
    
    ResellerService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public SqlMediator getSqlMediator() {
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("reseller")
                .addFindField("id")
                .addFindField("name")
                .addFindField("pageUrl")
                .addFindField("planCode")
                .addFindField("contactEmail")
                .addFindField("contactPhone")
                .addFindField("contactName");
        return sm;
    }

    @Override
    public SqlEntityMediator grantAccess(int entityId, List<Integer> listUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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

    @Override
    public SqlEntityMediator revokeAccess(int entityId) {
        SqlMediator sm = this.getSqlMediator();
        sm.revokeAccessAllUsers(Arrays.asList(entityId));
        return this;
    }
    
    //  default 'FREE', -- premium, or band-code of the service
    @RequestMapping(value = "/resellers", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int resellerID,
           @RequestParam(value="name", required=false, defaultValue="") String name,
           @RequestParam(value="pageUrl", required=false, defaultValue="") String pageUrl,
           @RequestParam(value="planCode", required=false, defaultValue="FREE") String planCode,
           @RequestParam(value="contactEmail", required=false, defaultValue="") String contactEmail,
           @RequestParam(value="contactPhone", required=false, defaultValue="") String contactPhone,
           @RequestParam(value="contactName", required=false, defaultValue="") String contactName
    ){
        SqlMediator sm = this.getSqlMediator();
        if (resellerID >= 0){
            sm.addFindParam("id", resellerID, 1);
        }
        if (!name.isEmpty()){
            sm.addFindParam("name", name, 1);
        }
        if (!pageUrl.isEmpty()){
            sm.addFindParam("pageUrl", pageUrl, 1);
        }
        if (!planCode.isEmpty()){
            sm.addFindParam("planCode", planCode, 1);
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
            int id = (Integer)obj.get("id");
            obj.put("storeList", this.findStore(id));
            obj.put("shoppingOnlineLinkList", this.findShoppingOnlineLink(id));
        });
        return result;
    }
    
    @RequestMapping(value = "/reseller", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int resellerID,
           @RequestParam(value="name", required=false, defaultValue="") String name,
           @RequestParam(value="pageUrl", required=false, defaultValue="") String pageUrl,
           @RequestParam(value="planCode", required=false, defaultValue="FREE") String planCode,
           @RequestParam(value="contactEmail", required=false, defaultValue="") String contactEmail,
           @RequestParam(value="contactPhone", required=false, defaultValue="") String contactPhone,
           @RequestParam(value="contactName", required=false, defaultValue="") String contactName
    ){
        SqlMediator sm = this.getSqlMediator();
        if (resellerID >= 0){
            sm.addId(resellerID);
        }
        if (!name.isEmpty()){
            sm.addUpsertParam("name", name);
        }
        if (!pageUrl.isEmpty()){
            sm.addUpsertParam("pageUrl", pageUrl);
        }
        if (!planCode.isEmpty()){
            sm.addUpsertParam("planCode", planCode);
        }
        if (!contactEmail.isEmpty()){
            sm.addUpsertParam("contactEmail", contactEmail);
        }
        if (!contactPhone.isEmpty()){
            sm.addUpsertParam("contactPhone", contactPhone);
        }
        sm.runUpsert();
        int newId = (resellerID>0)?resellerID:Integer.parseInt(sm.getId());
        this.grantAccess(newId);
        
        return sm.getId();
    }
    
    
    @RequestMapping(value = "/reseller", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int resellerID
    ){
        this.revokeAccess(resellerID);
        SqlMediator sm = this.getSqlMediator();
        sm.addFindParam("id", resellerID, 1)
          .runDelete();
        return ""+resellerID;
    }
    
    public List<Integer> getStore(int resellerId){
        StoreService ss = new StoreService();
        List<Map<String, Object>> listObject = ss.find(-1, resellerId, "", "", "", "", "", "", "", "", "", "");
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("id"));
        });
        return listInt;
    }
    
    public List<Integer> getShoppingOnlineLink(int resellerId){
        ShoppingOnlineLinkService ss = new ShoppingOnlineLinkService();
        List<Map<String, Object>> listObject = ss.find(-1, "", -1, resellerId);
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("id"));
        });
        return listInt;
    }
    
    @RequestMapping(value = "/reseller/stores", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findStore(
            @RequestParam(value="id", required=true) int resellerId
    ){
        return this.getStore(resellerId);
    }
    
    @RequestMapping(value = "/reseller/shoppingOnlineLinks", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findShoppingOnlineLink(
            @RequestParam(value="id", required=true) int resellerId
    ){
        return this.getShoppingOnlineLink(resellerId);
    }
    
    @RequestMapping(value = "/reseller/store", method = RequestMethod.POST)
    public @ResponseBody String upsertStore(
           @RequestParam(value="id", required=true) int resellerId,
           @RequestParam(value="storeId", required=true) int storeId
    ){
        StoreService ss = new StoreService();
        return ss.upsert(storeId, resellerId, "", "", "", "", "", "", "", "", "", "");
    }
    
    @RequestMapping(value = "/reseller/shoppingOnlineLink", method = RequestMethod.POST)
    public @ResponseBody String upsertShoppingOnlineLink(
           @RequestParam(value="id", required=true) int resellerId,
           @RequestParam(value="shoppingOnlineLinkId", required=true) int shoppingOnlineLinkId
    ){
        ShoppingOnlineLinkService ss = new ShoppingOnlineLinkService();
        return ss.upsert(shoppingOnlineLinkId, "", -1, resellerId);
    }
    
    @RequestMapping(value = "/reseller/store", method = RequestMethod.DELETE)
    public @ResponseBody String deleteStore(
           @RequestParam(value="id", required=true) int resellerId,
           @RequestParam(value="storeId", required=true) int storeId
    ){
        StoreService ss = new StoreService();
        ss.delete(storeId);
        return ""+resellerId+":"+storeId;
    }
    
    @RequestMapping(value = "/reseller/shoppingOnlineLink", method = RequestMethod.DELETE)
    public @ResponseBody String deleteShoppingOnlineLink(
           @RequestParam(value="id", required=true) int resellerId,
           @RequestParam(value="shoppingOnlineLinkId", required=true) int shoppingOnlineLinkId
    ){
        ShoppingOnlineLinkService ss = new ShoppingOnlineLinkService();
        ss.delete(shoppingOnlineLinkId);
        return ""+resellerId+":"+shoppingOnlineLinkId;
    }
}
