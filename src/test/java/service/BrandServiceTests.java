/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author antonio
 */
public class BrandServiceTests {
    final static int N_RECORDS = 5;
    final static String STR_NAME_PREFIX = "brand";
    final static String STR_PAGEURL_PREFIX = "http://page.url";
    final static int N_NO_BRAND_ID = -1;
    
    private void smokeCreate(BrandService bs, List<Integer> listId ){
        List<Map<String, Object>> find;
        for (int i =0; i< BrandServiceTests.N_RECORDS; i++){
            String givenName = BrandServiceTests.STR_NAME_PREFIX+i,
                    givenPageurl = BrandServiceTests.STR_PAGEURL_PREFIX+i;
            int id = Integer.parseInt(
                    bs.upsert(BrandServiceTests.N_NO_BRAND_ID,
                            givenName,
                            givenPageurl));
            listId.add(id);
            find = bs.find(id, "", "",false);
            Assert.assertEquals("Check find(id) returns a single record",1, find.size());
            String fName = find.get(0).get("name").toString();
            Assert.assertEquals("Check that the returned record has given name",givenName, fName);
            String fpageurl = find.get(0).get("pageurl").toString();
            Assert.assertEquals("Check that the returned record has given pageurl",givenPageurl, fpageurl);
        }
    }
    
    private void smokeUpdate(BrandService bs, List<Integer> listId) {
        List<Map<String, Object>> find;
        String strUpdate = "XXXXX";
        int id = listId.get(0);
        bs.upsert(id, strUpdate, "");
        find = bs.find(id, "", "",false);
        Assert.assertEquals("Check find(id) returns a single record",1, find.size());
        String fName = find.get(0).get("name").toString();
        Assert.assertEquals("Check that the returned record has given name",strUpdate, fName);
        id = listId.get(1);
        bs.upsert(id, strUpdate, "");
        find = bs.find(id, "", "",false);
        Assert.assertEquals("Check find(id) returns a single record",1, find.size());
        fName = find.get(0).get("name").toString();
        Assert.assertEquals("Check that the returned record has given name",strUpdate, fName);
        
        find = bs.find(-1, strUpdate, "",false);
        Assert.assertTrue("Check find(id) returns a single record",2 <= find.size());
        
    
    }
    
    private void smokeDelete(BrandService bs, List<Integer> listId){
        List<Map<String, Object>> find;
        find = bs.find(BrandServiceTests.N_NO_BRAND_ID, "", "",false);
        int currentSize = find.size();
        listId.stream().forEach((id) -> {
            bs.delete(id);
        });
        find = bs.find(BrandServiceTests.N_NO_BRAND_ID, "", "",false);
        Assert.assertEquals("Check all the records have been deleted",
                currentSize - BrandServiceTests.N_RECORDS, find.size()); 
        
    }
    
    @Test
    public void Crud(){
        List<Integer> listId = new ArrayList<>(5);
        List<Map<String, Object>> find;
        BrandService bs = new BrandService();
        
        // create
        this.smokeCreate(bs, listId);
        
        // update
        this.smokeUpdate(bs, listId);
        
        
        // delete
        this.smokeDelete(bs, listId);
        
    }
    
    public void testProductCrud(){
        
    }

}
