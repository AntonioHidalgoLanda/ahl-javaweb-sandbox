/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamediator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;
import product.SessionMediator;

/**
 *
 * @author antonio
 */
public class PostgreSQLMediator implements SqlMediator{
    private final Map<String,String> mapStringParam = new HashMap<>();
    private final Map<String,Integer> mapIntParam = new HashMap<>();
    private final Map<String,Double> mapDoubleParam = new HashMap<>();
    private final Map<String,Double> mapWeight = new HashMap<>();
    private final List<Map<String, Object>> listFindResult = new LinkedList<>();
    private final List<String> listFindFields = new LinkedList<>();
    private final BasicDataSource connectionPool;
    private String strId = "";
    private int nId = -1;
    private String tablename = "";
    private String lastQuery = "";
    private String alias = "";
    private String accessIdColumn = "id";
    private String accesstablename = "";

    public PostgreSQLMediator(BasicDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }
    
    @Override
    public SqlMediator setAccessId(String accessId) {
        this.accessIdColumn = accessId;
        return this;
    }
    
    @Override
    public SqlMediator setAccessTable(String accessTableName) {
        this.accesstablename = accessTableName;
        return this;
    }

    @Override
    public SqlMediator setTable(String tablename) {
        this.tablename = tablename;
        return this;
    }
    
    public String getLastQuery(){
        return this.lastQuery;
    }
    
    @Override
    public SqlMediator clear() {
        this.strId="";
        this.nId = -1;
        this.mapStringParam.clear();
        this.mapIntParam.clear();
        this.mapDoubleParam.clear();
        this.listFindFields.clear();
        this.mapWeight.clear();
        this.listFindResult.clear();     // We assume that the collector will take care of the inner Maps
        return this;
    }

    @Override
    public SqlMediator addUpsertParam(String fieldname, String value) {
        if (this.mapIntParam.containsKey(fieldname)
                || this.mapDoubleParam.containsKey(fieldname)){
            return this;
        }
        if (fieldname.equalsIgnoreCase("id")){
            this.strId = value;
        }
        this.mapStringParam.put(fieldname, value);
        return this;
    }

    @Override
    public SqlMediator addUpsertParam(String fieldname, double value) {
        if (this.mapStringParam.containsKey(fieldname)
                || this.mapIntParam.containsKey(fieldname)){
            return this;
        }
        this.mapDoubleParam.put(fieldname, value);
        return this;
    }

    @Override
    public SqlMediator addUpsertParam(String fieldname, int value) {
        if (this.mapStringParam.containsKey(fieldname)
                || this.mapDoubleParam.containsKey(fieldname)){
            return this;
        }
        if (fieldname.equalsIgnoreCase("id")){
            this.nId = value;
        }
        this.mapIntParam.put(fieldname, value);
        return this;
    }

    @Override
    public SqlMediator addId(String id) {
        this.strId = id;
        return this;
    }

    @Override
    public SqlMediator addId(int id) {
        this.nId = id;
        return this;
    }

    @Override
    public SqlMediator addFindParam(String fieldname, String value, double weight) {
        this.mapStringParam.put(fieldname, value);
        this.mapWeight.put(fieldname,weight);
        return this;
    }

    @Override
    public SqlMediator addFindParam(String fieldname, double value, double weight) {
        this.mapDoubleParam.put(fieldname, value);
        this.mapWeight.put(fieldname,weight);
        return this;
    }

    @Override
    public SqlMediator addFindParam(String fieldname, int value, double weight) {
        this.mapIntParam.put(fieldname, value);
        this.mapWeight.put(fieldname,weight);
        return this;
    }

    @Override
    public String getId() {
        if (this.nId < 0 && !this.strId.isEmpty()) return this.strId;
        if (this.nId > -1 && this.strId.isEmpty()) return ""+this.nId;
        return "";
    }
    
    private String listToCsvString (List list){
         return this.listToCsvString(list, ",");
    }
    
    
    private String listToCsvString (List list, String delimiter){
        String csv = "";
        for (Object o :list){
            if (!csv.isEmpty()){
                csv += " "+delimiter+" ";
            }
            if(!this.alias.isEmpty()){
                csv += this.alias +".";
            }
            csv += o.toString();
        }
        return csv;
        
    } 
    
    private String listOfPairs(List<String> listParams){
        return this.listOfPairs(listParams,",");
    }
    
    private String listOfPairs(List<String> listParams, String delimiter){
        String strSet = "";
        for (String fieldname : listParams){
            if (!strSet.isEmpty()){
                strSet += " "+delimiter+" ";
            }
            if(!this.alias.isEmpty()){
                strSet += this.alias +".";
            }
            strSet += fieldname + " = ? ";
        }
        return strSet;
    }
    
    private String listOfQuestionMarks(int lenght){
        return this.listOfQuestionMarks(lenght,",");
    }
    
    private String listOfQuestionMarks(int lenght, String delimiter){
        String csv = "";
        for (int i =0; i < lenght; i++){
            if(!csv.isEmpty()) {
                csv+=" "+delimiter+" ";
            }
            csv+="?";
        }
        return csv;
    }
    
    // TODO add default parameters...
    private PreparedStatement addUpdateParameter(ArrayList<String> list, String fieldname, PreparedStatement updateSql)
            throws SQLException {
        int idx = list.indexOf(fieldname) + 1;
        if (this.mapStringParam.containsKey(fieldname)){
            updateSql.setString(idx,this.mapStringParam.get(fieldname));
        }
        else if (this.mapIntParam.containsKey(fieldname)){
            updateSql.setInt(idx,this.mapIntParam.get(fieldname));
        }
        else if (this.mapDoubleParam.containsKey(fieldname)){
            updateSql.setDouble(idx,this.mapDoubleParam.get(fieldname));
        }
        
        return updateSql;
    }

    private ArrayList<String> generateListParams(){
        int nParams = this.mapStringParam.keySet().size() +
                    this.mapIntParam.keySet().size() +
                    this.mapDoubleParam.keySet().size();
        ArrayList<String> listParams = new ArrayList<>(nParams);
        listParams.addAll(this.mapStringParam.keySet());
        listParams.addAll(this.mapIntParam.keySet());
        listParams.addAll(this.mapDoubleParam.keySet());
        return listParams;
    }

    private SqlMediator runInsert(ArrayList<String> listParams){
        int nNewId = 0;
            String strListFields = this.listOfQuestionMarks(listParams.size());
            String strListValues = this.listToCsvString(listParams);
            this.lastQuery = "INSERT INTO " + this.tablename + " ";
            if (!this.mapStringParam.isEmpty() || !this.mapIntParam.isEmpty() ||
                    !this.mapDoubleParam.isEmpty()){
                String statementFields = " (";
                String statementValues = ") VALUES (";
                String statementClosing = ") Returning id;";
               
                this.lastQuery += statementFields + strListValues
                        + statementValues + strListFields
                        + statementClosing;
                try (Connection connection = this.connectionPool.getConnection()){
                    PreparedStatement updateSql = connection.prepareStatement(this.lastQuery);
                    
                    for (String fieldname : listParams){
                        this.addUpdateParameter(listParams, fieldname, updateSql);
                    }

                    ResultSet rs = updateSql.executeQuery();
                    rs.next();
                    nNewId = rs.getInt(1);

                } catch (Exception e) {
                    System.err.println(e);
                }
            }
            this.nId = nNewId;
            return this;
    }
    
    // TODO: These private methods depending on listParams should go in a different class
    private SqlMediator runUpdate(ArrayList<String> listParams){
        this.lastQuery =
                "UPDATE " + this.tablename + " SET " 
                    + this.listOfPairs(listParams)
                    + "WHERE id = ?";

            try (Connection connection = this.connectionPool.getConnection()){
                PreparedStatement updateSql = connection.prepareStatement(this.lastQuery);

                for (String fieldname : listParams){
                    this.addUpdateParameter(listParams, fieldname, updateSql);
                }
                
                if (this.nId < 0){
                    updateSql.setString(listParams.size()+1, this.strId);
                }
                else{
                    updateSql.setInt(listParams.size()+1, this.nId);
                }
                
                updateSql.executeUpdate();
            } catch (Exception e) {
                System.err.println("ERROR DURING UPDATE:");
                System.err.println(e);
            }
            return this;
    }
    
    
    @Override
    public SqlMediator runUpsert() {
        ArrayList<String> listParams = this.generateListParams();
        // insert
        if (this.nId < 0 && this.strId.isEmpty()){
            this.runInsert(listParams);
        }
        // update
        else {
            this.runUpdate(listParams);
        }
        return this;
    }

    @Override
    public SqlMediator runFind() {
        return this.runFind(Double.NaN);
    }
    
// TODO ORDER and top
    @Override
    public SqlMediator runFind(double threshold) {
        this.listFindResult.clear();
        ArrayList<String> listParams = generateListParams();
        this.alias = "t";
        this.lastQuery = "SELECT DISTINCT "
                + this.listToCsvString(this.listFindFields)
                + " FROM " + this.tablename +" "+this.alias;
        this.lastQuery += this.accessibilityFindJoin();
        if (!listParams.isEmpty() || this.nId > -1 || !this.strId.isEmpty()){
            this.lastQuery += " WHERE ";
            if (!listParams.isEmpty()){
                this.lastQuery += this.listOfPairs(listParams, "AND");
            }
            if (this.nId > -1 || !this.strId.isEmpty()){
                if (!listParams.isEmpty()){
                    this.lastQuery += " AND ";
                }
                this.lastQuery += " "+this.alias+".id = ? ";
            }
        }

        try (Connection connection = this.connectionPool.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(this.lastQuery);

            for (String fieldname : listParams){
                this.addUpdateParameter(listParams, fieldname, stmt);
            }
            if (this.nId > -1 || !this.strId.isEmpty()){
                if (this.nId < 0){
                    stmt.setString(listParams.size(), this.strId);
                }
                else{
                    stmt.setInt(listParams.size(), this.nId);
                }
            }
                
            ResultSet rs = stmt.executeQuery();


          while (rs.next()) {
            Map<String, Object> result = new HashMap<>();
            for (String fieldname : this.listFindFields){
                Object field = rs.getObject(fieldname);
                if (field instanceof java.sql.Timestamp){
                    field = field.toString();
                }
                result.put(fieldname, field);
            }
            this.listFindResult.add(result);
          }
        } catch (Exception e) {
            System.err.println("ERROR DURING FIND:");
            System.err.println(e);
            System.err.println(this.lastQuery);
        }
        this.alias = "";
        return this;
    }

    @Override
    public SqlMediator runDelete() {
        this.listFindResult.clear();
        ArrayList<String> listParams = generateListParams();
        this.alias = "t";
        this.lastQuery =
                "DELETE FROM " + this.tablename + " "+ this.alias
                    + " WHERE ";

        if (!listParams.isEmpty()){
            this.lastQuery += this.listOfPairs(listParams, "AND");
        }
                
        try (Connection connection = this.connectionPool.getConnection()){
            PreparedStatement updateSql = connection.prepareStatement(this.lastQuery);

            for (String fieldname : listParams){
                this.addUpdateParameter(listParams, fieldname, updateSql);
            }

            updateSql.executeUpdate();
        } catch (Exception e) {
            System.err.println("ERROR DURING DELETE:");
            System.err.println(e);
        }
        this.alias = "";
        return this;
    }

    @Override
    public SqlMediator addFindField(String fieldname) {
        this.listFindFields.add(fieldname);
        return this;
    }

    @Override
    public List<Map<String, Object>> getResultsFind() {
        return new ArrayList<>(listFindResult);
    }

    private String accessibilityFindJoin() {
        int currentuserid = SessionMediator.getCurrentUserId();
        String accessTable = (this.accesstablename.isEmpty())?this.tablename:this.accesstablename;
        String strJoin = "INNER JOIN accessResource ar " +
                  " ON ar.localid = "+this.alias+"."+this.accessIdColumn+" " +
                  " AND ar.tablename like '"+accessTable+"' " +
                "INNER JOIN access a " +
                  " ON a.accessid = ar.id " +
                  " AND a.enduserid IN ("+currentuserid+", -1) ";
        return strJoin;
    }
    
    @Override
    public boolean hasFullAccess(int id){
        int currentuserid = SessionMediator.getCurrentUserId();
        return this.hasAccess(id, currentuserid, true);
        
    }

    @Override
    public boolean hasFullAccess(int resourceid, int userid) {
        return this.hasAccess(resourceid, userid, true);
    }
    
    @Override
    public boolean hasReadAccess(int id){
        int currentuserid = SessionMediator.getCurrentUserId();
        return this.hasAccess(id, currentuserid, false);
    }

    @Override
    public boolean hasReadAccess(int resourceid, int userid) {
        return this.hasAccess(resourceid, userid, false);
     }
    
    private boolean hasAccess(int id, int userid, boolean fullaccess){
        boolean anyResults = false;
        String accessTable = (this.accesstablename.isEmpty())?this.tablename:this.accesstablename;
        
        String query = "SELECT DISTINCT a.enduserid, a.accessid, ar.tablename, ar.localid"
                + " FROM access a "
                + " INNER JOIN accessResource ar " +
                      " ON a.accessid = ar.id " +
                      " AND ar.localid = "+ id +" " +
                      " AND ar.tablename like '"+accessTable+"' " +
                      " AND a.enduserid IN ("+userid+", -1) ";
        if (fullaccess) {
            query += " AND a.readonly = false ";
        }


        try (Connection connection = this.connectionPool.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            anyResults = rs.isBeforeFirst();

        } catch (Exception e) {
            System.err.println("ERROR DURING HAS ACCESS:");
            System.err.println(e);
            System.err.println(query);
        }
        return anyResults;
    }
    
    @Override
    public SqlMediator grantAccess(boolean readonly, List<Integer> listId){
        int currentuserid = SessionMediator.getCurrentUserId();
        return this.grantAccess(readonly, listId, Arrays.asList(currentuserid));
    }
    
    @Override
    public SqlMediator grantAccess(boolean readonly, List<Integer> listResource, List<Integer> listUserID){
        String accessTable = (this.accesstablename.isEmpty())?this.tablename:this.accesstablename;
        
        String query = "INSERT INTO access (enduserid,accessid,readonly)";
        query += " SELECT u.id, " +
                 " ar.id, " +
                 Boolean.toString(readonly) +" " +
                " FROM accessResource ar, enduser u " +
                " WHERE ar.tablename Like '" + accessTable + "'"+
                   " AND ar.localid IN (" + this.listToCsvString(listResource) +")"+
                   " AND ur.id IN (" + this.listToCsvString(listUserID) +")";

            try (Connection connection = this.connectionPool.getConnection()){
                PreparedStatement updateSql = connection.prepareStatement(query);

                ResultSet rs = updateSql.executeQuery();

            } catch (Exception e) {
                System.err.println(e);
            }
                
        return this;
        
    }
    
    // listId of the ids entered in accessResources as local id
    @Override
    public SqlMediator revokeAccess(List<Integer> listId){
        int currentuserid = SessionMediator.getCurrentUserId();
        return this.revokeAccess(listId,Arrays.asList(currentuserid));
    }

    @Override
    public SqlMediator revokeAccess(List<Integer> listResource, List<Integer> listUser) {
        String accessTable = (this.accesstablename.isEmpty())?this.tablename:this.accesstablename;
        listUser.add(-1);
        String query =
                "DELETE FROM access a " +
                " USING accessResource ar " +
                " WHERE a.accessid = ar.id " +
                    " AND ar.tablename Like '" + accessTable + "'";
        if (listUser.size() == 1){
           query += " AND a.enduserid IN (" + this.listToCsvString(listUser) + ") ";
        }
           query += " AND ar.localid IN (" +this.listToCsvString(listResource) + ")";
                
        try (Connection connection = this.connectionPool.getConnection()){
            PreparedStatement updateSql = connection.prepareStatement(query);
            updateSql.executeUpdate();
        } catch (Exception e) {
            System.err.println("ERROR DURING DENNY ACCESS:");
            System.err.println(e);
        }
        return this;
    }
    
    @Override
    public SqlMediator revokeAccessAllUsers(List<Integer> listId){
        return this.revokeAccess(listId,Arrays.asList());
    }
   
}
