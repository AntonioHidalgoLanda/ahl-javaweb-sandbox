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
public class ShoppingOnlineLinkService {
    /*
CREATE TABLE shoppingOnlineLink (
  id SERIAL PRIMARY KEY,
  url varchar (255),
  productId int4 not null references product(id),
  resellerId int4 not null references reseller(id)
);*/
    
    BasicDataSource connectorPool = null;
    
    ShoppingOnlineLinkService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }
    
    @RequestMapping(value = "/shoppingOnlineLinks", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int shoppingOnlineLinkID,
           @RequestParam(value="federationId", required=false, defaultValue="") String federationId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("shoppingOnlineLink")
                .addFindField("id")
                .addFindField("federationId");
        if (shoppingOnlineLinkID >= 0){
            sm.addFindParam("id", shoppingOnlineLinkID, 1);
        }
        if (!federationId.isEmpty()){
            sm.addFindParam("federationId", federationId, 1);
        }
        sm.runFind();
        return sm.getResultsFind();
    }
    
    @RequestMapping(value = "/shoppingOnlineLink", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int shoppingOnlineLinkID,
           @RequestParam(value="federationId", required=false, defaultValue="") String federationId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("shoppingOnlineLink");
        if (shoppingOnlineLinkID >= 0){
            sm.addId(shoppingOnlineLinkID);
        }
        if (!federationId.isEmpty()){
            sm.addUpsertParam("federationId", federationId);
        }
        sm.runUpsert();
        return sm.getId();
    }
    
    
    @RequestMapping(value = "/shoppingOnlineLink", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int shoppingOnlineLinkID
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("shoppingOnlineLink")
                .addFindParam("id", shoppingOnlineLinkID, 1);
        sm.runDelete();
        return ""+shoppingOnlineLinkID;
    }
}
