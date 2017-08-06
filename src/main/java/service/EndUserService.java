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
public class EndUserService {
    
    BasicDataSource connectorPool = null;
    
    EndUserService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }
   
    @RequestMapping(value = "/endusers", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int enduserID,
           @RequestParam(value="federationId", required=false, defaultValue="") String federationId,
           @RequestParam(value="profileName", required=false, defaultValue="") String profileName,
           @RequestParam(value="recoveryEmail", required=false, defaultValue="") String recoveryEmail,
           @RequestParam(value="avatarUrl", required=false, defaultValue="") String avatarUrl
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("enduser")
                .addFindField("id")
                .addFindField("federationId")
                .addFindField("profileName")
                .addFindField("recoveryEmail")
                .addFindField("avatarUrl");
        if (enduserID >= 0){
            sm.addFindParam("id", enduserID, 1);
        }
        if (!federationId.isEmpty()){
            sm.addFindParam("federationId", federationId, 1);
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
        List<Map<String, Object>> result = sm.getResultsFind();
        result.stream().forEach((obj) -> {
            obj.put("igotitList", this.findIgotits((Integer)obj.get("id")));
        });
        return result;
    }
    
    @RequestMapping(value = "/enduser", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int enduserID,
           @RequestParam(value="federationId", required=false, defaultValue="") String federationId,
           @RequestParam(value="profileName", required=false, defaultValue="") String profileName,
           @RequestParam(value="recoveryEmail", required=false, defaultValue="") String recoveryEmail,
           @RequestParam(value="avatarUrl", required=false, defaultValue="") String avatarUrl
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("enduser");
        if (enduserID >= 0){
            sm.addId(enduserID);
        }
        if (!federationId.isEmpty()){
            sm.addUpsertParam("federationId", federationId);
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
    
    @RequestMapping(value = "/enduser", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int enduserID
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("enduser")
                .addFindParam("id", enduserID, 1);
        sm.runDelete();
        return ""+enduserID;
    }
    
    public List<Integer> getIgotits(int enduserid){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotit")
                .addFindField("id")
                .addFindField("enduserid")
                .addFindParam("enduserid", enduserid, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("id"));
        });
        return listInt;
    }
    
    @RequestMapping(value = "/enduser/igotits", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findIgotits(
            @RequestParam(value="id", required=true) int enduserid
    ){
        return this.getIgotits(enduserid);
    }
    
    @RequestMapping(value = "/enduser/igotit", method = RequestMethod.POST)
    public @ResponseBody String upsertIgotit(
           @RequestParam(value="id", required=true) int enduserid,
           @RequestParam(value="igotitId", required=true) int igotitId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotit");
        sm.addId(igotitId)
             .addUpsertParam("enduserid", enduserid);
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/enduser/igotit", method = RequestMethod.DELETE)
    public @ResponseBody String deleteIgotit(
           @RequestParam(value="id", required=true) int enduserid,
           @RequestParam(value="igotitId", required=true) int igotitId
    ){
        IgotItService ps = new IgotItService();
        ps.delete(igotitId);
        return ""+enduserid+":"+igotitId;
    }
}