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
public class BrandService {
    
    /** Find requests, the filters are ands, to get the full spectrum of a
     * select, the ors are obtained joining to querie
     * @param brandID
     * @param name
     * @param pageurl
     * @return s*/
    @RequestMapping(value = "/brands", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int brandID,
           @RequestParam(value="name", required=false, defaultValue="") String name,
           @RequestParam(value="pageurl", required=false, defaultValue="") String pageurl
    ){
        BasicDataSource connectorPool;
        try {
            connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
            return new ArrayList<>();
        }
        PostgreSQLMediator sm = new PostgreSQLMediator(connectorPool);
        sm.setTable("brand")
                .addFindField("id")
                .addFindField("name")
                .addFindField("pageurl");
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
        return sm.getResultsFind();
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
        BasicDataSource connectorPool;
        try {
            connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
            return "";
        }
        PostgreSQLMediator sm = new PostgreSQLMediator(connectorPool);
        sm.setTable("brand");
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
        return sm.getId();
    }
    
    /** Find requests, the filters are ands, to get the full spectrum of a
     * select, the ors are obtained joining to querie
     * @param brandID
     * @return s*/
    @RequestMapping(value = "/brand", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int brandID
    ){
        BasicDataSource connectorPool;
        try {
            connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
            return "";
        }
        PostgreSQLMediator sm = new PostgreSQLMediator(connectorPool);
        sm.setTable("brand")
                .addFindParam("id", brandID, 1);
        sm.runDelete();
        return ""+brandID;
    }
}
