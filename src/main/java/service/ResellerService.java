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
    /*
CREATE TABLE reseller (
  id SERIAL PRIMARY KEY,
  name varchar (255) not null,
  pageUrl varchar (255),
  planCode varchar (10) not null default 'FREE', -- premium, or band-code of the service
  contactEmail varchar (255),
  contactPhone varchar (50),
  contactName varchar (255)
);*/
    
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
}
