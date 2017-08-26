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
import java.util.LinkedList;
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
public class EndUserService  implements SqlEntityMediator{
    public enum RelationshipLevel{
        NONE,
        FRIEND
    }
    
    BasicDataSource connectorPool = null;
    
    public EndUserService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public SqlMediator getSqlMediator() {
    PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("enduser")
                .addFindField("id")
                .addFindField("federationId")
                .addFindField("profileName")
                .addFindField("recoveryEmail")
                .addFindField("avatarUrl");
        return sm;
    }

    private SqlMediator getFriendSqlMediator() {
        SqlMediator sm = new PostgreSQLMediator(this.connectorPool);
            sm.clear()
                    .turnIdOff()
                    .setTable("friend")
                    .setAccessId("enduserid")
                    .setAccessTable("enduser")
                    .addFindField("friendid")
                    .addFindField("enduserid");
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
            sm.grantAccessAllUsers(true, Arrays.asList(entityId));
        }
        return this;
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId, List<Integer> listUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SqlEntityMediator revokeAccess(int entityId) {
        SqlMediator sm = this.getSqlMediator();
        sm.revokeAccessAllUsers(Arrays.asList(entityId));
        return this;
    }
   
    @RequestMapping(value = "/endusers", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int enduserID,
           @RequestParam(value="federationId", required=false, defaultValue="") String federationId,
           @RequestParam(value="profileName", required=false, defaultValue="") String profileName,
           @RequestParam(value="recoveryEmail", required=false, defaultValue="") String recoveryEmail,
           @RequestParam(value="avatarUrl", required=false, defaultValue="") String avatarUrl,
           @RequestParam(value="extended", required=false, defaultValue="true") boolean bextended
    ){
        SqlMediator sm = this.getSqlMediator();
        
        if (enduserID >= 0){
            sm.addFindParam("id", enduserID, 1);
        }
        if (!federationId.isEmpty()){
            sm.addFindParam("federationId", federationId, 1);
        }
        if (!profileName.isEmpty()){
            sm.addFindParam("profileName", profileName, 1);
        }
        if (!recoveryEmail.isEmpty()){
            sm.addFindParam("recoveryEmail", recoveryEmail, 1);
        }
        if (!avatarUrl.isEmpty()){
            sm.addFindParam("avatarUrl", avatarUrl, 1);
        }
        sm.runFind();
System.out.println(((PostgreSQLMediator)sm).getLastQuery());
        List<Map<String, Object>> result = sm.getResultsFind();
        if(bextended){
            result.stream().forEach((obj) -> {
                int objId = (Integer)obj.get("id");
                obj.put("igotitList", this.findIgotits(objId));
                RelationshipLevel level = RelationshipLevel.FRIEND;
                obj.put(
                        level.toString()+"List",
                        this.getFriends(objId,-1,level)
                );
            });
        }
        return result;
    }
    
    @RequestMapping(value = "/enduser", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int enduserID,
           @RequestParam(value="federationId", required=false, defaultValue="") String federationId,
           @RequestParam(value="profileName", required=false, defaultValue="") String profileName,
           @RequestParam(value="recoveryEmail", required=false, defaultValue="") String recoveryEmail,
           @RequestParam(value="avatarUrl", required=false, defaultValue="") String avatarUrl
    ){
        SqlMediator sm = this.getSqlMediator();
        if (enduserID >= 0){
            sm.addId(enduserID);
        }
        if (!federationId.isEmpty()){
            sm.addUpsertParam("federationId", federationId);
        }
        if (!profileName.isEmpty()){
            sm.addUpsertParam("profileName", profileName);
        }
        if (!recoveryEmail.isEmpty()){
            sm.addUpsertParam("recoveryEmail", recoveryEmail);
        }
        if (!avatarUrl.isEmpty()){
            sm.addUpsertParam("avatarUrl", avatarUrl);
        }
        sm.runUpsert();
        int newId = (enduserID>0)?enduserID:Integer.parseInt(sm.getId());
        this.grantAccess(newId);
        return sm.getId();
    }
    
    @RequestMapping(value = "/enduser", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int enduserID
    ){
        this.deleteIgotit(enduserID, -1);
        this.revokeAccess(enduserID);
        
        SqlMediator sm = this.getSqlMediator();
        sm.addFindParam("id", enduserID, 1)
          .runDelete();
        return ""+enduserID;
    }
    
    public List<Integer> getIgotits(int enduserid, IgotItService.AccessLevel maxAccesslevel){
        IgotItService is = new IgotItService();
        List<Map<String, Object>> listObject = new LinkedList<>();
        switch (maxAccesslevel){
            case FRIEND:
                listObject = is.find(-1, enduserid, -1, "", "", -1, "FRIEND", false);
            case PUBLIC:
                listObject.addAll(is.find(-1, enduserid, -1, "", "", -1, "PUBLIC", false));
                break;
            default:
                listObject = is.find(-1, enduserid, -1, "", "", -1, "", false);
        }
        
        List<Integer> listInt = new ArrayList<>(listObject.size());
        listObject.stream().forEach((obj) -> {
            listInt.add((Integer)obj.get("id"));
        });
        return listInt;
    }
    
    @RequestMapping(value = "/enduser/igotits", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findIgotits(
            @RequestParam(value="id", required=true) int enduserid
    ){
        return this.getIgotits(enduserid,IgotItService.AccessLevel.DRAFT);
    }
    
    @RequestMapping(value = "/enduser/igotit", method = RequestMethod.POST)
    public @ResponseBody String upsertIgotit(
           @RequestParam(value="id", required=true) int enduserid,
           @RequestParam(value="igotitId", required=true) int igotitId
    ){
        IgotItService is = new IgotItService();
        return is.upsert(igotitId, enduserid, -1, "", "", -1, "");
    }
    
    @RequestMapping(value = "/enduser/igotit", method = RequestMethod.DELETE)
    public @ResponseBody String deleteIgotit(
           @RequestParam(value="id",  required=true) int enduserid,
           @RequestParam(value="igotitId", required=false, defaultValue="-1") int igotitId
    ){
        IgotItService is = new IgotItService();
        if (igotitId > 0){
            is.delete(igotitId);
        }
        else{
            List<Integer> search = this.findIgotits(enduserid);
            search.stream().forEach((item) -> {
                is.delete(item);
            }); 
        }
        return ""+enduserid+":"+((igotitId>0)?igotitId:"all");
    }
    
    public static RelationshipLevel relationshipLevelParse(String level){
        switch (level.toUpperCase()){
            case "FRIEND":
            case "0":
                return RelationshipLevel.FRIEND;
            default:
                return RelationshipLevel.NONE;
        }
        
    }
    
    public static int relationshipLevelToSql(RelationshipLevel level){
        switch (level){
            case FRIEND:
                return 0;
            default:
                return -1;
        }
    }
    
    public List<Integer> getFriends(
            int enduserid,
            int friendid,
            RelationshipLevel level
    ){
        List<Integer> listInt = new LinkedList<>();
        int nLevel = EndUserService.relationshipLevelToSql(level);
        for (int i = 0; i <= nLevel; i++){
            SqlMediator sm = this.getFriendSqlMediator();
            sm.addFindParam("enduserid", enduserid, 1)
              .addFindParam("relationship", i, 1);
            if (friendid>0){
              sm.addFindParam("friendid", friendid, 1);
            }
            sm.runFind();
            List<Map<String, Object>> listObject = sm.getResultsFind();
            listObject.stream().forEach((obj) -> {
                listInt.add((Integer)obj.get("friendid"));
            });
        }
        return listInt;
    }
    
    @RequestMapping(value = "/enduser/friends", method = RequestMethod.GET)
    public @ResponseBody List<Integer> findFriends(
            @RequestParam(value="id", required=true) int enduserid,
            @RequestParam(value="friendid", required=false, defaultValue="-1") int friendid,
            @RequestParam(value="relationship", required=false, defaultValue="FRIEND") String sLevel
    ){
        return this.getFriends(
                enduserid,
                friendid,
                EndUserService.relationshipLevelParse(sLevel));
    }
    
    @RequestMapping(value = "/enduser/friend", method = RequestMethod.POST)
    public @ResponseBody String upsertFriend(
           @RequestParam(value="id", required=true) int enduserid,
           @RequestParam(value="friendid", required=true) int friendid,
           @RequestParam(value="relationship", required=false, defaultValue="NONE") String sLevel
    ){
        RelationshipLevel rLevel = relationshipLevelParse(sLevel);
        SqlMediator sm = this.getFriendSqlMediator();
        sm.addUpsertParam("enduserid", enduserid)
          .addUpsertParam("friendid", friendid);
        // note that default in INSERTS will be FRIEND
        if (rLevel != RelationshipLevel.NONE){
            sm.addUpsertParam(
                    "relationship",
                    EndUserService.relationshipLevelToSql(rLevel)
            );
        }
        sm.runUpsert();
        if (enduserid <= 0 || !sLevel.isEmpty()){
            IgotItService.AccessLevel al = IgotItService.AccessLevel.PUBLIC;
            if (rLevel == RelationshipLevel.FRIEND){
                al = IgotItService.AccessLevel.FRIEND;
            }
            IgotItService is = new IgotItService();
            SqlMediator issm = is.getSqlMediator();
            List<Integer> listIgotits = this.getIgotits(enduserid,al);//
            issm.grantAccess(true, listIgotits, Arrays.asList(friendid));
            
        }
        return sm.getId();
    }
    
    @RequestMapping(value = "/enduser/friend", method = RequestMethod.DELETE)
    public @ResponseBody String deleteFriend(
           @RequestParam(value="id",  required=true) int enduserid,
           @RequestParam(value="friendid", required=false, defaultValue="-1") int friendid
    ){
        /*
        SqlMediator smf = this.getSqlMediator();
        List<Integer> listIgotitID = this.findIgotits(enduserid);
        smf.revokeAccess(listIgotitID, Arrays.asList(friendid));
        */
        SqlMediator sm = this.getFriendSqlMediator();
        sm.revokeAccessAllUsers(Arrays.asList(enduserid));
        sm.addFindParam("enduserid", enduserid, 1);
        if (friendid>0){
            sm.addFindParam("friendid", friendid, 1);
        }
        sm.runDelete();
        
        return ""+enduserid+":"+((friendid>0)?friendid:"all");
    }
}
