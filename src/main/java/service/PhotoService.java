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
public class PhotoService {
    /*
CREATE TABLE photo (
  id SERIAL PRIMARY KEY,
  localpath varchar (255) not null,
  igotitId int4 not null references igotit (id)
);*/
    
    BasicDataSource connectorPool = null;
    
    PhotoService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }

    
    @RequestMapping(value = "/photos", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int photoID,
           @RequestParam(value="localpath", required=false, defaultValue="") String localpath,
           @RequestParam(value="igotitId", required=false, defaultValue="0") int igotitId
    ){
        BasicDataSource connectorPool;
        try {
            connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
            return new ArrayList<>();
        }
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("photo")
                .addFindField("id")
                .addFindField("localpath")
                .addFindField("igotitId");
        if (photoID >= 0){
            sm.addFindParam("id", photoID, 1);
        }
        if (!localpath.isEmpty()){
            sm.addFindParam("localpath", localpath, 1);
        }
        if (igotitId > 0){
            sm.addFindParam("igotitId", igotitId, 1);
        }
        sm.runFind();
        return sm.getResultsFind();
    }
    
    @RequestMapping(value = "/photo", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int photoID,
           @RequestParam(value="localpath", required=false, defaultValue="") String localpath,
           @RequestParam(value="igotitId", required=false, defaultValue="0") int igotitId
    ){
        BasicDataSource connectorPool;
        try {
            connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
            return "";
        }
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("photo");
        if (photoID >= 0){
            sm.addId(photoID);
        }
        if (!localpath.isEmpty()){
            sm.addUpsertParam("localpath", localpath);
        }
        if (igotitId > 0){
            sm.addUpsertParam("igotitId", igotitId);
        }
        sm.runUpsert();
        return sm.getId();
    }
    
    
    @RequestMapping(value = "/photo", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int photoID
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("photo")
                .addFindParam("id", photoID, 1);
        sm.runDelete();
        return ""+photoID;
    }
}
