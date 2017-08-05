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
public class TagService {
    /*
CREATE TABLE tag (
  name varchar (255) not null,
  igotitId int4 not null references igotit (id)
);*/
    
    BasicDataSource connectorPool = null;
    
    TagService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }
    
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="") String name,
           @RequestParam(value="igotitId", required=false, defaultValue="0") int igotitId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("tag")
                .addFindField("name")
                .addFindField("igotitId");
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
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("tag")
                .addId(name)
                .addUpsertParam("igotitId", igotitId);
        sm.runUpsert();
        return sm.getId();
    }
    
    
    @RequestMapping(value = "/tag", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="name", required=true) String name,
           @RequestParam(value="igotitId", required=true) int igotitId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("name")
                .addFindParam("name", name, 1)
                .addFindParam("igotitId", igotitId, 1);
        sm.runDelete();
        return ""+name+":"+igotitId;
    }
    
}
