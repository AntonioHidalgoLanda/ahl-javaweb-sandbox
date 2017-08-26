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
public class TagService  implements SqlEntityMediator{
    
    BasicDataSource connectorPool = null;
    
    TagService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public SqlMediator getSqlMediator() {
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("tag")
                .turnIdOff()
                .setAccessId("igotitId")
                .setAccessTable("igotit")
                .addFindField("name")
                .addFindField("igotitId");
        return sm;
    }

    @Override
    public SqlEntityMediator grantAccess(int entityId, List<Integer> listUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SqlEntityMediator grantAccess(int entityId) {
        // Tags use the id of the parent (igotit) hence we do not need to create
        // new access rights
        return this;
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId, List<Integer> listUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId) {
        // Tags use the id of the parent (igotit) hence we do not need to create
        // new access rights
        return this;
    }
    
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="") String name,
           @RequestParam(value="igotitId", required=false, defaultValue="0") int igotitId
    ){
        SqlMediator sm = this.getSqlMediator();
        
        if (!name.isEmpty()){
            sm.addFindParam("name", name, 1);
        }
        if (igotitId > 0){
            sm.addFindParam("igotitId", igotitId, 1);
        }
        sm.runFind();
        return sm.getResultsFind();
    }
    
    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="name", required=true) String name,
           @RequestParam(value="igotitId", required=true) int igotitId
    ){
        SqlMediator sm = this.getSqlMediator();
        sm.addUpsertParam("name",name)
          .addUpsertParam("igotitId", igotitId)
          .runUpsert();
        this.grantAccess(igotitId);
        return sm.getId();
    }
    
    
    @RequestMapping(value = "/tag", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="name", required=true) String name,
           @RequestParam(value="igotitId", required=true) int igotitId
    ){
        this.revokeAccess(igotitId);
        SqlMediator sm = this.getSqlMediator();
        sm.addFindParam("name", name, 1)
          .addFindParam("igotitId", igotitId, 1)
          .runDelete();
        return ""+name+":"+igotitId;
    }
    
}
