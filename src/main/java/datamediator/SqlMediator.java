/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamediator;

import java.util.Date;

/**
 *
 * @author antonio
 */
public interface SqlMediator {
    public SqlMediator addUpsertParam(String fieldname,String value);
    public SqlMediator addUpsertParam(String fieldname,double value);
    public SqlMediator addUpsertParam(String fieldname,int value);
    public SqlMediator addUpsertParam(String fieldname,Date value);
    public SqlMediator addId(String id);
    public SqlMediator addId(int id);
    public SqlMediator addFindParam(String filename, String value,double weight);
    public SqlMediator addFindParam(String filename, double value,double weight);
    public SqlMediator addFindParam(String filename, int value,double weight);
    public SqlMediator addFindParam(String filename, Date value,double weight);
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
     * this.addFindParam()
     * this....
     * this.addFindParam()
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
    public SqlMediator setTable(String tablename);
}
