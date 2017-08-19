/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

/**
 *
 * @author antonio
 */
public class IgotItServiceTests {
    final static int N_RECORDS = 3;
    final static Date DATE_PUBLISHDATE = Date.from(Instant.now());
    final static int STR_VISIBILITY = 1;
    final static String STR_USER_COMMENT_PREFIX = "usercomment";
    final static String STR_COORDINATES_PREFIX = "coordinates";
    final static int N_RATING = 4;
    final static int N_NO_ID = -1;
    final static int N_ACCESSLEVEL = 2;     //0 (draft/me), 1 (friends), 2 (followers), 3 (public)
    
    public void smokeCreate(IgotItService is, List<Integer> listId , int enduserid){
        List<Map<String, Object>> find;
        for (int i =0; i< IgotItServiceTests.N_RECORDS; i++){
            String givenUsercomment = IgotItServiceTests.STR_USER_COMMENT_PREFIX+i;
            String givenCoordinates = IgotItServiceTests.STR_COORDINATES_PREFIX+i;
            int id = Integer.parseInt(is.upsert(N_NO_ID, enduserid, STR_VISIBILITY, givenUsercomment, givenCoordinates, N_RATING,N_ACCESSLEVEL));
            listId.add(id);
            find = is.find(id,false);
            TestUtils.assertField(find, "usercomment", givenUsercomment);
            TestUtils.assertField(find, "coordinates", givenCoordinates);
        }
    }
    
    public void smokeUpdate(IgotItService is, List<Integer> listId) {
        List<Map<String, Object>> find;
        String strUpdate = "XXXXX";
        int id = listId.get(0);
        is.upsert(id, -1, -1, strUpdate, "", -1, -1);
        
        find = is.find(id, false);
        TestUtils.assertField(find, "usercomment", strUpdate);
        id = listId.get(1);
        is.upsert(id, -1, -1, strUpdate, "", -1, -1);
        find = is.find(id, false);
        Object pd= find.get(0).get("publishdate");
        System.out.println("Published Date ("+pd.getClass()+"): "+pd.toString());
        TestUtils.assertField(find, "usercomment", strUpdate);
        
        find = is.find(IgotItServiceTests.N_NO_ID, -1, -1, strUpdate, "", -1, -1, false);
        TestUtils.assertMultiField(find, "recoveryEmail", 2);
        
    
    }
    
    public void smokeDelete(IgotItService is, List<Integer> listId){
        List<Map<String, Object>> find;
        find = is.find(IgotItServiceTests.N_NO_ID, -1, -1, "", "", -1, -1, false);
        int currentSize = find.size();
        listId.stream().forEach((id) -> {
            is.delete(id);
        });
        find = is.find(IgotItServiceTests.N_NO_ID, -1, -1, "", "", -1, -1, false);
        Assert.assertEquals("Check all the records have been deleted",
                currentSize - listId.size(), find.size()); 
        
    }
    
    @Test
    public void Crud(){
        List<Integer> listId = new ArrayList<>(IgotItServiceTests.N_RECORDS);
        List<Integer> listEndusersId = new ArrayList<>(EndUserServiceTests.N_RECORDS);
        IgotItService bs = new IgotItService();
        EndUserServiceTests eust = new EndUserServiceTests();
        EndUserService eus = new EndUserService();
        eust.smokeCreate(eus, listEndusersId);
        int euid = listEndusersId.get(0);
        
        // create
        this.smokeCreate(bs, listId,euid);
        
        // update
        this.smokeUpdate(bs, listId);
        
        // delete
        this.smokeDelete(bs, listId);
        eust.smokeDelete(eus, listEndusersId);
        
    }
    
}
