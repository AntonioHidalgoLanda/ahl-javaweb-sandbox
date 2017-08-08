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
public class ProductServiceTests {
    final static int N_RECORDS = 3;
    final static int N_NO_ID = -1;
    
    final static String STR_PREFIX_SKU = "SKU",
            STR_PREFIX_NAME = "NAME",
            STR_PREFIX_BRANDLINK = "BRANDLINK";
    
    public void smokeCreate(ProductService ps, List<Integer> listId , int brandid){
        List<Map<String, Object>> find;
        for (int i =0; i< ProductServiceTests.N_RECORDS; i++){
            String sku = ProductServiceTests.STR_PREFIX_SKU+i,
                    name = ProductServiceTests.STR_PREFIX_NAME+i,
                    brandLink = ProductServiceTests.STR_PREFIX_BRANDLINK+i;
            int id = Integer.parseInt(ps.upsert(N_NO_ID, sku, brandid, N_NO_ID, name, brandLink));
            listId.add(id);
            find = ps.find(id, "", N_NO_ID, N_NO_ID, "", "",false);
            Assert.assertEquals("Check find("+id+") returns a single record",1, find.size());
            String fSku = find.get(0).get("sku").toString();
            Assert.assertEquals("Check that the returned record has given pageurl",sku, fSku);
            String fName = find.get(0).get("name").toString();
            Assert.assertEquals("Check that the returned record has given name",name, fName);
            String fBrandLink = find.get(0).get("brandLink").toString();
            Assert.assertEquals("Check that the returned record has given pageurl",brandLink, fBrandLink);
        }
    }
    
    public void smokeUpdate(ProductService ps, List<Integer> listId, int brandid) {
        List<Map<String, Object>> find;
        String strUpdate = "XXXXX";
        int id = listId.get(0);
        ps.upsert(id, strUpdate, N_NO_ID, N_NO_ID, "", "");
        find = ps.find(id, "", N_NO_ID, N_NO_ID, "", "",false);
        Assert.assertEquals("Check find(id) returns a single record",1, find.size());
        String fSku = find.get(0).get("sku").toString();
        Assert.assertEquals("Check that the returned record has given name",strUpdate, fSku);
        id = listId.get(1);
        ps.upsert(id, strUpdate, brandid, N_NO_ID, "", "");
        find = ps.find(id, "", N_NO_ID, N_NO_ID, "", "",false);
        Assert.assertEquals("Check find(id) returns a single record",1, find.size());
        fSku = find.get(0).get("sku").toString();
        Assert.assertEquals("Check that the returned record has given name",strUpdate, fSku);
        
        find = ps.find(N_NO_ID, strUpdate, N_NO_ID, N_NO_ID, "", "",false);
        Assert.assertTrue("Check find(id) returns a single record",2 <= find.size());
        
    
    }
    
    public void smokeDelete(ProductService ps, List<Integer> listId){
        List<Map<String, Object>> find;
        find = ps.find(N_NO_ID, "", N_NO_ID, N_NO_ID, "", "",false);
        int currentSize = find.size();
        listId.stream().forEach((id) -> {
            ps.delete(id);
        });
        find = ps.find(N_NO_ID, "", N_NO_ID, N_NO_ID, "", "",false);
        Assert.assertEquals("Check all the records have been deleted",
                currentSize - listId.size(), find.size()); 
        
    }
    
    @Test
    public void Crud(){/*
        List<Integer> listBrandId = new ArrayList<>(BrandServiceTests.N_RECORDS),
                listProductId = new ArrayList<>(ProductServiceTests.N_RECORDS);
        BrandService bs = new BrandService();
        ProductService ps = new ProductService();
        BrandServiceTests bt = new BrandServiceTests();
        
        // create
        bt.smokeCreate(bs, listBrandId);
        
        this.smokeCreate(ps, listProductId,listBrandId.get(0));
        
        // update
        this.smokeUpdate(ps, listBrandId,listBrandId.get(1));
        
        // delete
        this.smokeDelete(ps, listBrandId);
        */
    }
    
    
    public void testBrandCrud(){
        List<Integer> listBrandId = new ArrayList<>(BrandServiceTests.N_RECORDS),
                listProductId = new ArrayList<>(ProductServiceTests.N_RECORDS);
        BrandService bs = new BrandService();
        ProductService ps = new ProductService();
        BrandServiceTests bt = new BrandServiceTests();
        
        // create
        bt.smokeCreate(bs, listBrandId);
        this.smokeCreate(ps, listProductId,listBrandId.get(0));
        //bs.upsertProduct(brandid, productid);
        //bs.find(brandid, STR_NAME_PREFIX, STR_PAGEURL_PREFIX, true);
        //bs.findProducts(brandId);
        //bs.deleteProduct(brandid, productid); // if we delete a brand we need tu update the product
        
    }
    
    /*
    public void testShoppingOnlineLinkCrud(){
        
    }
    
    public void testStoreCrud(){
        
    }
    
    public void testIgotitCrud(){
        
    }
    */
    
}
