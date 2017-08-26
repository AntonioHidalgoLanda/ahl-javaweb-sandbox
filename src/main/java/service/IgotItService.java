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
import java.util.ArrayList;
import java.util.Arrays;
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
public class IgotItService  implements SqlEntityMediator{
    
    BasicDataSource connectorPool = null;
    
    IgotItService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public SqlMediator getSqlMediator() {
        SqlMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotit")
                .addFindField("id")
                .addFindField("publishdate")
                .addFindField("enduserid")
                .addFindField("visibility")
                .addFindField("usercomment")
                .addFindField("coordinates")
                .addFindField("accessLevel")
                .addFindField("rating");
        return sm;
    }

    public SqlMediator getProductSqlMediator() {
        SqlMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("igotitProduct")
                .turnIdOff()
                .setAccessId("igotitId")
                .setAccessTable("igotit")
                .addFindField("igotitId")
                .addFindField("productId");
        return sm;
    }

    @Override
    public SqlEntityMediator grantAccess(int entityId, List<Integer> listUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SqlEntityMediator grantAccess(int entityId) {
        SqlMediator sm = this.getSqlMediator();
        if(!sm.hasAccessNoInitialized(entityId)){
            sm.grantAccess(false, Arrays.asList(entityId));
        }
        return this;
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId, List<Integer> listUsers) {
        SqlMediator sm = this.getSqlMediator();
        sm.revokeAccessAllUsers(Arrays.asList(entityId));
        return this;
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Map<String, Object>> find(int igotitId){
        return this.find(igotitId, -1, -1, "", "", -1, -1, true);
    }
    
    public List<Map<String, Object>> find(int igotitId, boolean bextended){
        return this.find(igotitId, -1, -1, "", "", -1, -1, bextended);
    }
    
    public List<Map<String, Object>> findEndusers(int enduserid, boolean bextended){
        return this.find(-1, enduserid, -1, "", "", -1, -1, bextended);
    }
    
    @RequestMapping(value = "/igotits", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int igotitId,
           @RequestParam(value="enduserid", required=false, defaultValue="-1") int enduserid,
           @RequestParam(value="visibility", required=false, defaultValue="-1") int visibility,
           @RequestParam(value="usercomment", required=false, defaultValue="") String usercomment,
           @RequestParam(value="coordinates", required=false, defaultValue="") String coordinates,
           @RequestParam(value="rating", required=false, defaultValue="-1") int rating,
           @RequestParam(value="accessLevel", required=false, defaultValue="-1") int accessLevel,
           @RequestParam(value="extended", required=false, defaultValue="true") boolean bextended
    ){
        SqlMediator sm = this.getSqlMediator();
        if (igotitId >= 0){
            sm.addFindParam("id", igotitId, 1);
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
        if (accessLevel > 0){
            sm.addFindParam("accessLevel", accessLevel, 1);
        }
        sm.runFind();
        List<Map<String, Object>> result = sm.getResultsFind();
        if(bextended){
        result.stream().forEach((obj) -> {
                int id = (Integer)obj.get("id");
                obj.put("photoList", this.findPhotos(id));
                obj.put("productList", this.findProducts(id));
                obj.put("tagList", this.findTags(id));
            });
        }
        return result;
    }
    
    
    // Date e.g. 2011-12-03T10:15:30+01:00'
    // we strore UCD
    @RequestMapping(value = "/igotit", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int igotitId,
            @RequestParam(value="enduserid", required=false, defaultValue="0") int enduserid,
            @RequestParam(value="visibility", required=false, defaultValue="0") int visibility,
            @RequestParam(value="usercomment", required=false, defaultValue="") String usercomment,
            @RequestParam(value="coordinates", required=false, defaultValue="") String coordinates,
            @RequestParam(value="rating", required=false, defaultValue="0") int rating,
           @RequestParam(value="accessLevel", required=false, defaultValue="-1") int accessLevel
    ){
        SqlMediator sm = this.getSqlMediator();
        if (igotitId >= 0){
            sm.addId(igotitId);
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
        if (rating > 0){
            sm.addUpsertParam("accessLevel", accessLevel);
        }
        sm.runUpsert();
        int newId = (igotitId > 0)? igotitId : Integer.parseInt(sm.getId());
        this.grantAccess(newId);
        return ""+newId;
    }
    
    
    @RequestMapping(value = "/igotit", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int igotitId
    ){
        this.deletePhoto(igotitId, -1);
        this.deleteProduct(igotitId, -1);
        this.deleteTag(igotitId, "");
        
        this.revokeAccess(igotitId);
        SqlMediator sm = this.getSqlMediator();
        sm.addFindParam("id", igotitId, 1)
            .runDelete();
        return ""+igotitId;
    }
    
    public List<Integer> getPhotos(int igotitId){
        PhotoService ps = new PhotoService();
        List<Map<String, Object>> listObject = ps.find(-1, "", igotitId);
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("id"));
        });
        return listInt;
    }
    
    public List<Integer> getProducts(int igotitId){
        SqlMediator sm = this.getProductSqlMediator();
        sm.addFindParam("igotitId", igotitId, 1)
          .runFind();
        List<Map<String, Object>> listObject = sm.getResultsFind();
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("productId"));
        });
        return listInt;
    }
    
    public List<String> getTags(int igotitId){
        TagService ts = new TagService();
        List<Map<String, Object>> listObject = ts.find("", igotitId);
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
        PhotoService ps = new PhotoService();
        return ps.upsert(photoId, "", igotitId);
    }
    
    @RequestMapping(value = "/igotit/product", method = RequestMethod.POST)
    public @ResponseBody String upsertProduct(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="productId", required=true) int productId
    ){
        SqlMediator sm = this.getProductSqlMediator();
        sm.addUpsertParam("productId", productId)
          .addUpsertParam("igotitId", igotitId)
          .runUpsert();
        // Product/Igotit depends on access on igotit, so there is nothing to do
        return sm.getId();
    }
    
    @RequestMapping(value = "/igotit/tag", method = RequestMethod.POST)
    public @ResponseBody String upsertTag(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="tag", required=true) String tag
    ){
        TagService ts = new TagService();
        return ts.upsert(tag, igotitId);
    }
    
    @RequestMapping(value = "/igotit/photo", method = RequestMethod.DELETE)
    public @ResponseBody String deletePhoto(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="photoId", required=false, defaultValue="-1") int photoId
    ){
        PhotoService ps = new PhotoService();
        if (photoId > 0){
            ps.delete(photoId);
        }
        else {
            List<Integer> search = this.findPhotos(igotitId);
            search.stream().forEach((item) -> {
                ps.delete(item);
            }); 
        }
        return ""+igotitId+":"+((photoId>0)?photoId:"all");
    }
    
    @RequestMapping(value = "/igotit/product", method = RequestMethod.DELETE)
    public @ResponseBody String deleteProduct(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="productId", required=false, defaultValue="-1") int productId
    ){
        SqlMediator sm = this.getProductSqlMediator();
        sm.addFindParam("igotitId", igotitId, 1);
        if (productId>0){
            sm.addFindParam("productId", productId, 1);
        }
        sm.runDelete();
        return ""+igotitId+":"+((productId>0)?productId:"all");
    }
    
    @RequestMapping(value = "/igotit/tag", method = RequestMethod.DELETE)
    public @ResponseBody String deleteTag(
           @RequestParam(value="id", required=true) int igotitId,
           @RequestParam(value="tag", required=false, defaultValue="") String tag
    ){
        TagService ts = new TagService();
        if (!tag.isEmpty()){
            ts.delete(tag,igotitId);
        }
        else {
            List<String> search = this.findTags(igotitId);
            search.stream().forEach((item) -> {
                ts.delete(item,igotitId);
            }); 
        }
        return ""+igotitId+":"+((!tag.isEmpty())?tag:"all");
    }
}
