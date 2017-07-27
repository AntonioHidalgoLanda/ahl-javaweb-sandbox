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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author antonio
 */
public class PostgreSQLMediator implements SqlMediator{
    private final Map<String,String> mapStringParam = new HashMap<>();
    private final Map<String,Integer> mapIntParam = new HashMap<>();
    private final Map<String,Double> mapDoubleParam = new HashMap<>();
    private final Map<String,Date> mapDateParam = new HashMap<>();
    private final Map<String,Double> mapWeight = new HashMap<>();
    private final List<Map<String, Object>> listFindResult = new LinkedList<>();
    private final List<String> listFindFields = new LinkedList<>();
    private final BasicDataSource connectionPool;
    private String strId = "";
    private int nId = -1;
    private String tablename = "";
    private String lastQuery = "";

    public PostgreSQLMediator(BasicDataSource connectionPool) {
        this.connectionPool = connectionPool;
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
        this.mapDateParam.clear();
        this.listFindFields.clear();
        this.mapWeight.clear();
        this.listFindResult.clear();     // We assume that the collector will take care of the inner Maps
        return this;
    }

    @Override
    public SqlMediator addUpsertParam(String fieldname, String value) {
        if (this.mapIntParam.containsKey(fieldname)
                || this.mapDoubleParam.containsKey(fieldname)
                || this.mapDateParam.containsKey(fieldname)){
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
                || this.mapIntParam.containsKey(fieldname)
                || this.mapDateParam.containsKey(fieldname)){
            return this;
        }
        this.mapDoubleParam.put(fieldname, value);
        return this;
    }

    @Override
    public SqlMediator addUpsertParam(String fieldname, int value) {
        if (this.mapStringParam.containsKey(fieldname)
                || this.mapDoubleParam.containsKey(fieldname)
                || this.mapDateParam.containsKey(fieldname)){
            return this;
        }
        if (fieldname.equalsIgnoreCase("id")){
            this.nId = value;
        }
        this.mapIntParam.put(fieldname, value);
        return this;
    }

    @Override
    public SqlMediator addUpsertParam(String fieldname, Date value) {
        if (this.mapStringParam.containsKey(fieldname)
                || this.mapIntParam.containsKey(fieldname)
                || this.mapDoubleParam.containsKey(fieldname)){
            return this;
        }
        this.mapDateParam.put(fieldname, value);
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
    public SqlMediator addFindParam(String fieldname, Date value, double weight) {
        this.mapDateParam.put(fieldname, value);
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
        else if (this.mapDateParam.containsKey(fieldname)){
            updateSql.setDate(idx, (java.sql.Date) this.mapDateParam.get(fieldname));
        }
        
        return updateSql;
    }
    
    private Object getResult(String fieldname, ResultSet result)
            throws SQLException {
        if (this.mapStringParam.containsKey(fieldname)){
            return result.getString(fieldname);
        }
        else if (this.mapIntParam.containsKey(fieldname)){
            return result.getInt(fieldname);
        }
        else if (this.mapDoubleParam.containsKey(fieldname)){
            return result.getDouble(fieldname);
        }
        else if (this.mapDateParam.containsKey(fieldname)){
            return result.getDate(fieldname);
        }
        
        return null;
    }
    
    private ArrayList<String> generateListParams(){
        int nParams = this.mapStringParam.keySet().size() +
                    this.mapIntParam.keySet().size() +
                    this.mapDoubleParam.keySet().size() +
                    this.mapDateParam.keySet().size();
        ArrayList<String> listParams = new ArrayList<>(nParams);
        listParams.addAll(this.mapStringParam.keySet());
        listParams.addAll(this.mapIntParam.keySet());
        listParams.addAll(this.mapDoubleParam.keySet());
        listParams.addAll(this.mapDateParam.keySet());
        return listParams;
    }

    private SqlMediator runInsert(ArrayList<String> listParams){
        int nNewId = 0;
            String strListFields = this.listOfQuestionMarks(listParams.size());
            String strListValues = this.listToCsvString(listParams);
            this.lastQuery = "INSERT INTO " + this.tablename + " ";
            if (!this.mapStringParam.isEmpty() || !this.mapIntParam.isEmpty() ||
                    !this.mapDoubleParam.isEmpty() || !this.mapDateParam.isEmpty()){
                String statementFields = " (";
                String statementValues = ") VALUES (";
                String statementClosing = ") Returning id";
               
                this.lastQuery += statementFields + strListFields
                        + statementValues + strListValues
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
                    //return e.getMessage();
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
                    updateSql.setString(listParams.size(), this.strId);
                }
                else{
                    updateSql.setInt(listParams.size(), this.nId);
                }
                
                //nRowUpdated = 
                updateSql.executeUpdate();
            } catch (Exception e) {
                //return e.getMessage();
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
        this.lastQuery = "SELECT "
                + this.listToCsvString(this.listFindFields)
                + " FROM " + this.tablename;
        if (!listParams.isEmpty() || this.nId > -1 || !this.strId.isEmpty()){
            this.lastQuery += " WHERE ";
        }
        if (!listParams.isEmpty()){
            this.lastQuery += this.listOfPairs(listParams, "AND");
        }
        if (this.nId > -1 || !this.strId.isEmpty()){
            if (!listParams.isEmpty()){
                this.lastQuery += " AND ";
            }
            this.lastQuery += " id = ? ";
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
                result.put(fieldname, this.getResult(fieldname, rs));
            }
            this.listFindResult.add(result);
          }
        } catch (Exception e) {
            this.lastQuery += ";Error: "+e.getMessage();
            System.err.println(e);//e.getMessage()
        }
        return this;
    }

    @Override
    public SqlMediator runDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
