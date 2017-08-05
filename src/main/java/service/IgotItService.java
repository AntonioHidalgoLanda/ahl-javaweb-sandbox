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
import java.util.Date;
import org.apache.commons.dbcp2.BasicDataSource;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import product.DatetimeHelper;

/**
 *
 * @author antonio
 */
@RestController
public class IgotItService {
    
    @RequestMapping(value = "/igotits", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int igotitID,
           @RequestParam(value="publishdate", required=false, defaultValue=DatetimeHelper.NO_DATE_STRING) @DateTimeFormat(pattern="ISO_OFFSET_DATE_TIME") Date publishdate,
           @RequestParam(value="enduserid", required=false, defaultValue="0") int enduserid,
           @RequestParam(value="visibility", required=false, defaultValue="0") int visibility,
           @RequestParam(value="usercomment", required=false, defaultValue="") String usercomment,
           @RequestParam(value="coordinates", required=false, defaultValue="") String coordinates,
           @RequestParam(value="rating", required=false, defaultValue="10") int rating
    ){
        BasicDataSource connectorPool;
        try {
            connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
            return new ArrayList<>();
        }
        PostgreSQLMediator sm = new PostgreSQLMediator(connectorPool);
        sm.setTable("igotit")
                .addFindField("id")
                .addFindField("publishdate")
                .addFindField("enduserid")
                .addFindField("visibility")
                .addFindField("usercomment")
                .addFindField("coordinates")
                .addFindField("rating");
        if (igotitID >= 0){
            sm.addFindParam("id", igotitID, 1);
        }
        if (!DatetimeHelper.isNoDate(publishdate)){
            sm.addFindParam("publishdate", publishdate, 1);
        }
        if (enduserid > 0){
            sm.addFindParam("enduserid", enduserid, 1);
        }
        if (visibility > 0){
            sm.addFindParam("visibility", visibility, 1);
        }
        if (!usercomment.isEmpty()){
            sm.addFindParam("usercomment", usercomment, 1);
        }
        if (!coordinates.isEmpty()){
            sm.addFindParam("coordinates", coordinates, 1);
        }
        if (rating > 0){
            sm.addFindParam("rating", rating, 1);
        }
        sm.runFind();
        return sm.getResultsFind();
    }
    
    
    // Date e.g. 2011-12-03T10:15:30+01:00'
    // we strore UCD
    @RequestMapping(value = "/igotit", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int igotitID,
            @RequestParam(value="publishdate", required=false, defaultValue=DatetimeHelper.NO_DATE_STRING) @DateTimeFormat(pattern="ISO_OFFSET_DATE_TIME") Date publishdate,
            @RequestParam(value="enduserid", required=false, defaultValue="0") int enduserid,
            @RequestParam(value="visibility", required=false, defaultValue="0") int visibility,
            @RequestParam(value="usercomment", required=false, defaultValue="") String usercomment,
            @RequestParam(value="coordinates", required=false, defaultValue="") String coordinates,
            @RequestParam(value="rating", required=false, defaultValue="0") int rating
    ){
        BasicDataSource connectorPool;
        try {
            connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
            return "";
        }
        PostgreSQLMediator sm = new PostgreSQLMediator(connectorPool);
        sm.setTable("igotit");
        if (igotitID >= 0){
            sm.addId(igotitID);
        }
        if (!DatetimeHelper.isNoDate(publishdate)){
            sm.addUpsertParam("publishdate",publishdate );
        }
        if (enduserid > 0){
            sm.addUpsertParam("enduserid", enduserid);
        }
        if (visibility > 0){
            sm.addUpsertParam("visibility", visibility);
        }
        if (!usercomment.isEmpty()){
            sm.addUpsertParam("usercomment", usercomment);
        }
        if (!coordinates.isEmpty()){
            sm.addUpsertParam("coordinates", coordinates);
        }
        if (rating > 0){
            sm.addUpsertParam("rating", rating);
        }
        sm.runUpsert();
        return sm.getId();
    }
    
    
    @RequestMapping(value = "/igotit", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int igotitID
    ){
        BasicDataSource connectorPool;
        try {
            connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
            return "";
        }
        PostgreSQLMediator sm = new PostgreSQLMediator(connectorPool);
        sm.setTable("igotit")
                .addFindParam("id", igotitID, 1);
        sm.runDelete();
        return ""+igotitID;
    }
}
