/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamediator;

import java.util.List;
import java.util.Map;

/**
 *
 * @author antonio
 */
public interface SqlMediator {
    public SqlMediator clear();
    public SqlMediator setTable(String tablename);
    public SqlMediator setAccessId(String accessId);
    public SqlMediator setAccessTable(String accessTableName);
    public SqlMediator addUpsertParam(String fieldname,String value);
    public SqlMediator addUpsertParam(String fieldname,double value);
    public SqlMediator addUpsertParam(String fieldname,int value);
    public SqlMediator addId(String id);
    public SqlMediator addId(int id);
    public SqlMediator addFindField(String fieldname);
    public SqlMediator addFindParam(String fieldname, String value,double weight);
    public SqlMediator addFindParam(String fieldname, double value,double weight);
    public SqlMediator addFindParam(String fieldname, int value,double weight);
    public String getId();
    /**
     * this.addUpsertParam()
     * this....
     * this.addUpsertParam()
     * [this.addId()] if not found, it inserts a new record
     * this.runUpsert()
     * this.getId()
     * @return this
     */
    public SqlMediator runUpsert();
    /**
     * this.addFindField(field1)
     * ...
     * this.addFindField(fieldN)
     * this.addFindParam(Param1)
     * this....
     * this.addFindParam(ParamN)
     * this.runFind()
     * 
     * or 
     * this.addId()
     * this.runFind()
     * @return this
     */
    public SqlMediator runFind();
    public SqlMediator runFind(double threshold);
    /**
     * this.addId()
     * this.runDelete()
     * @return this
     */
    public SqlMediator runDelete();
    public List<Map<String,Object>> getResultsFind();
    public boolean hasFullAccess(int id);
    public boolean hasReadAccess(int id);
    public SqlMediator grantAccess(boolean readonly, List<Integer> listId);
    public SqlMediator revokeAccess(List<Integer> listId);
}
