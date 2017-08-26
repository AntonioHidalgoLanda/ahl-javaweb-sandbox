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
public class ShoppingOnlineLinkService  implements SqlEntityMediator{
    
    BasicDataSource connectorPool = null;
    
    ShoppingOnlineLinkService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public SqlMediator getSqlMediator() {
        SqlMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("shoppingOnlineLink")
                .setAccessTable("reseller")
                .setAccessId("resellerId")
                .addFindField("id")
                .addFindField("url")
                .addFindField("productId")
                .addFindField("resellerId");
        return sm;
    }

    @Override
    public SqlEntityMediator grantAccess(int entityId, List<Integer> listUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SqlEntityMediator grantAccess(int entityId) {
        // do nothing, access is managed in Resellert Service
        return this;
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId, List<Integer> listUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId) {
        // do nothing; access is managed in Reseller Service
        return this;
    }
    
    @RequestMapping(value = "/shoppingOnlineLinks", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int shoppingOnlineLinkID,
           @RequestParam(value="url", required=false, defaultValue="") String url,
           @RequestParam(value="productId", required=false, defaultValue="-1") int productId,
           @RequestParam(value="resellerId", required=false, defaultValue="-1") int resellerId
    ){
        SqlMediator sm = this.getSqlMediator();
        if (shoppingOnlineLinkID >= 0){
            sm.addFindParam("id", shoppingOnlineLinkID, 1);
        }
        if (!url.isEmpty()){
            sm.addFindParam("url", url, 1);
        }
        if (productId > 0){
            sm.addFindParam("productId", productId, 1);
        }
        if (resellerId > 0){
            sm.addFindParam("resellerId", resellerId, 1);
        }
        sm.runFind();
        return sm.getResultsFind();
    }
    
    @RequestMapping(value = "/shoppingOnlineLink", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int shoppingOnlineLinkID,
           @RequestParam(value="url", required=false, defaultValue="") String url,
           @RequestParam(value="productId", required=false, defaultValue="-1") int productId,
           @RequestParam(value="resellerId", required=false, defaultValue="-1") int resellerId
    ){
        SqlMediator sm = this.getSqlMediator();
        if (shoppingOnlineLinkID >= 0){
            sm.addId(shoppingOnlineLinkID);
        }
        if (!url.isEmpty()){
            sm.addUpsertParam("url", url);
        }
        if (productId > 0){
            sm.addUpsertParam("productId", productId);
        }
        if (resellerId > 0){
            sm.addUpsertParam("resellerId", resellerId);
        }
        sm.runUpsert();
        this.grantAccess(shoppingOnlineLinkID);
        return sm.getId();
    }
    
    
    @RequestMapping(value = "/shoppingOnlineLink", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int shoppingOnlineLinkID
    ){
        
        this.revokeAccess(shoppingOnlineLinkID);
        SqlMediator sm = this.getSqlMediator();
        sm.addFindParam("id", shoppingOnlineLinkID, 1)
            .runDelete();
        return ""+shoppingOnlineLinkID;
    }
}
