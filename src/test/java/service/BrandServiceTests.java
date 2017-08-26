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
import product.SessionMediator;
import utils.TestUtils;

/**
 *
 * @author antonio
 */
public class BrandServiceTests {
    final static int N_RECORDS = 3;
    final static String STR_NAME_PREFIX = "brand";
    final static String STR_PAGEURL_PREFIX = "http://page.url";
    final static int N_NO_BRAND_ID = -1;
    
    public void smokeCreate(BrandService bs, List<Integer> listId ){
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
            TestUtils.assertField(find, "name", givenName);
            TestUtils.assertField(find, "pageurl", givenPageurl);
        }
    }
    
    public void smokeUpdate(BrandService bs, List<Integer> listId) {
        List<Map<String, Object>> find;
        String strUpdate = "XXXXX";
        int id = listId.get(0);
        bs.upsert(id, strUpdate, "");
        find = bs.find(id, "", "",false);
        TestUtils.assertField(find, "name", strUpdate);
        id = listId.get(1);
        bs.upsert(id, strUpdate, "");
        find = bs.find(id, "", "",false);
        TestUtils.assertField(find, "name", strUpdate);
        
        find = bs.find(N_NO_BRAND_ID, strUpdate, "",false);
        TestUtils.assertMultiField(find, "name", 2);
        
    
    }
    
    public void smokeDelete(BrandService bs, List<Integer> listId){
        List<Map<String, Object>> find;
        find = bs.find(BrandServiceTests.N_NO_BRAND_ID, "", "",false);
        int currentSize = find.size();
        listId.stream().forEach((id) -> {
            bs.delete(id);
        });
        find = bs.find(BrandServiceTests.N_NO_BRAND_ID, "", "",false);
        Assert.assertEquals("Check all the records have been deleted",
                currentSize - listId.size(), find.size()); 
        
    }
    
    @Test
    public void Crud(){
        List<Integer> listId = new ArrayList<>(BrandServiceTests.N_RECORDS);
        BrandService bs = new BrandService();
        SessionMediator.setEmulationMode(1);
        
        // create
        this.smokeCreate(bs, listId);
        
        // update
        this.smokeUpdate(bs, listId);
        
        // delete
        this.smokeDelete(bs, listId);
        
    }

}
