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
    
    BasicDataSource connectorPool = null;
    
    IgotItService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }
    
    @RequestMapping(value = "/igotits", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int igotitId,
           @RequestParam(value="publishdate", required=false, defaultValue=DatetimeHelper.NO_DATE_STRING) @DateTimeFormat(pattern="ISO_OFFSET_DATE_TIME") Date publishdate,
           @RequestParam(value="enduserid", required=false, defaultValue="0") int enduserid,
           @RequestParam(value="visibility", required=false, defaultValue="0") int visibility,
           @RequestParam(value="usercomment", required=false, defaultValue="") String usercomment,
           @RequestParam(value="coordinates", required=false, defaultValue="") String coordinates,
           @RequestParam(value="rating", required=false, defaultValue="10") int rating
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotit")
                .addFindField("id")
                .addFindField("publishdate")
                .addFindField("enduserid")
                .addFindField("visibility")
                .addFindField("usercomment")
                .addFindField("coordinates")
                .addFindField("rating");
        if (igotitId >= 0){
            sm.addFindParam("id", igotitId, 1);
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
            @RequestParam(value="id", required=false, defaultValue="-1") int igotitId,
            @RequestParam(value="publishdate", required=false, defaultValue=DatetimeHelper.NO_DATE_STRING) @DateTimeFormat(pattern="ISO_OFFSET_DATE_TIME") Date publishdate,
            @RequestParam(value="enduserid", required=false, defaultValue="0") int enduserid,
            @RequestParam(value="visibility", required=false, defaultValue="0") int visibility,
            @RequestParam(value="usercomment", required=false, defaultValue="") String usercomment,
            @RequestParam(value="coordinates", required=false, defaultValue="") String coordinates,
            @RequestParam(value="rating", required=false, defaultValue="0") int rating
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotit");
        if (igotitId >= 0){
            sm.addId(igotitId);
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
            @RequestParam(value="id", required=true) int igotitId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotit")
                .addFindParam("id", igotitId, 1);
        sm.runDelete();
        return ""+igotitId;
    }
    
    public List<Integer> getPhotos(int igotitId){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("photo")
                .addFindField("id")
                .addFindField("igotitId")
                .addFindParam("igotitId", igotitId, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("id"));
        });
        return listInt;
    }
    
    public List<Integer> getProducts(int igotitId){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotitProduct")
                .addFindField("igotitId")
                .addFindField("productId")
                .addFindParam("igotitId", igotitId, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("productId"));
        });
        return listInt;
    }
    
    public List<String> getTags(int igotitId){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("tag")
                .addFindField("name")
                .addFindField("igotitId")
                .addFindParam("igotitId", igotitId, 1)
                .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<String> listStr = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listStr.add(obj.get("name").toString());
        });
        return listStr;
    }
    
    @RequestMapping(value = "/igotit/photos", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findPhotos(
            @RequestParam(value="id", required=true) int igotitId
    ){
        return this.getPhotos(igotitId);
    }
    
    @RequestMapping(value = "/igotit/products", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findProducts(
            @RequestParam(value="id", required=true) int igotitId
    ){
        return this.getProducts(igotitId);
    }
    
    @RequestMapping(value = "/igotit/tags", method = RequestMethod.GET)
    public @ResponseBody List<String> findTags(
            @RequestParam(value="id", required=true) int igotitId
    ){
        return this.getTags(igotitId);
    }
    
    @RequestMapping(value = "/igotit/photo", method = RequestMethod.POST)
    public @ResponseBody String upsertPhoto(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="photoId", required=true) int photoId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("photo");
        sm.addId(photoId)
             .addUpsertParam("igotitId", igotitId);
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/igotit/product", method = RequestMethod.POST)
    public @ResponseBody String upsertProduct(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="productId", required=true) int productId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotitProduct");
        sm.addUpsertParam("productId", productId)
          .addUpsertParam("igotitId", igotitId);
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/igotit/tag", method = RequestMethod.POST)
    public @ResponseBody String upsertTag(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="tag", required=true) String tag
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("tag");
        sm.addUpsertParam("name",tag)
             .addUpsertParam("igotitId", igotitId);
        sm.runUpsert();
        return sm.getId();
    }
    
    @RequestMapping(value = "/igotit/photo", method = RequestMethod.DELETE)
    public @ResponseBody String deletePhoto(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="photoId", required=true) int photoId
    ){
        PhotoService ps = new PhotoService();
        ps.delete(photoId);
        return ""+igotitId+":"+photoId;
    }
    
    @RequestMapping(value = "/igotit/product", method = RequestMethod.DELETE)
    public @ResponseBody String deleteProduct(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="productId", required=true) int productId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotitProduct")
                .addFindParam("igotitId", igotitId, 1)
                .addFindParam("productId", productId, 1);
        sm.runDelete();
        return ""+igotitId+":"+productId;
    }
    
    @RequestMapping(value = "/igotit/tag", method = RequestMethod.DELETE)
    public @ResponseBody String deleteTag(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="tag", required=true) String tag
    ){
        TagService ts = new TagService();
        ts.delete(tag,igotitId);
        return ""+igotitId+":"+tag;
    }
}
