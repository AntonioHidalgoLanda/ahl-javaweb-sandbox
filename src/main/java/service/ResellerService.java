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
public class ResellerService {
    
    BasicDataSource connectorPool = null;
    
    ResellerService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
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
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("reseller")
                .addFindField("id")
                .addFindField("name")
                .addFindField("pageUrl")
                .addFindField("planCode")
                .addFindField("contactEmail")
                .addFindField("contactPhone")
                .addFindField("contactName");
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
        return sm.getResultsFind();
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
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("reseller");
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
        return sm.getId();
    }
    
    
    @RequestMapping(value = "/reseller", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int resellerID
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("reseller")
                .addFindParam("id", resellerID, 1);
        sm.runDelete();
        return ""+resellerID;
    }
    
    public List<Integer> getStore(int resellerId){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("store")
                .addFindField("id")
                .addFindField("resellerId")
                .addFindParam("resellerId", resellerId, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("id"));
        });
        return listInt;
    }
    
    public List<Integer> getShoppingOnlineLink(int resellerId){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("shoppingOnlineLink")
                .addFindField("id")
                .addFindField("resellerId")
                .addFindParam("resellerId", resellerId, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
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
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("store");
        sm.addId(storeId)
             .addUpsertParam("resellerId", resellerId);
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/reseller/shoppingOnlineLink", method = RequestMethod.POST)
    public @ResponseBody String upsertShoppingOnlineLink(
           @RequestParam(value="id", required=true) int resellerId,
           @RequestParam(value="shoppingOnlineLinkId", required=true) int shoppingOnlineLinkId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("shoppingOnlineLink");
        sm.addId(shoppingOnlineLinkId)
             .addUpsertParam("resellerId", resellerId);
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/reseller/store", method = RequestMethod.DELETE)
    public @ResponseBody String deleteStore(
           @RequestParam(value="id", required=true) int resellerId,
           @RequestParam(value="igotitId", required=true) int storeId
    ){
        IgotItService ps = new IgotItService();
        ps.delete(storeId);
        return ""+resellerId+":"+storeId;
    }
    
    @RequestMapping(value = "/reseller/shoppingOnlineLink", method = RequestMethod.DELETE)
    public @ResponseBody String deleteShoppingOnlineLink(
           @RequestParam(value="id", required=true) int resellerId,
           @RequestParam(value="shoppingOnlineLinkId", required=true) int shoppingOnlineLinkId
    ){
        IgotItService ps = new IgotItService();
        ps.delete(shoppingOnlineLinkId);
        return ""+resellerId+":"+shoppingOnlineLinkId;
    }
}
