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
public class EndUserServiceTests {
    final static int N_RECORDS = 3;
    final static String STR_FEDERATION_ID_PREFIX = "federationId";
    final static String STR_PROFILE_NAME_PREFIX = "My Profile";
    final static String STR_RECOVERY_EMAIL_PREFIX = "myEmail@subdomain.test";
    final static String STR_AVATAR_URL_PREFIX = "avatar";
    final static int N_NO_ID = -1;
    
    public void smokeCreate(EndUserService eus, List<Integer> listId ){
        List<Map<String, Object>> find;
        for (int i =0; i< EndUserServiceTests.N_RECORDS; i++){
            String givenFederationId = EndUserServiceTests.STR_FEDERATION_ID_PREFIX+i;
            String givenProfileName = EndUserServiceTests.STR_PROFILE_NAME_PREFIX+i;
            String givenRecoveryEmail = EndUserServiceTests.STR_RECOVERY_EMAIL_PREFIX+i;
            String givenAvatarUrl = EndUserServiceTests.STR_AVATAR_URL_PREFIX+i;
            int id = Integer.parseInt(eus.upsert(N_NO_ID, givenFederationId, givenProfileName, givenRecoveryEmail, givenAvatarUrl));
            listId.add(id);
            find = eus.find(id, "", "","", "",false);
            TestUtils.assertField(find, "federationId", givenFederationId);
            TestUtils.assertField(find, "profileName", givenProfileName);
            TestUtils.assertField(find, "recoveryEmail", givenRecoveryEmail);
            TestUtils.assertField(find, "avatarUrl", givenAvatarUrl);
        }
    }
    
    public void smokeUpdate(EndUserService eus, List<Integer> listId) {
        List<Map<String, Object>> find;
        String strUpdate = "XXXXX";
        int id = listId.get(0);
        eus.upsert(id, "", "", strUpdate, "");
        eus.upsert(id, strUpdate, "", "", "");
        find = eus.find(id, "", "","", "",false);
        TestUtils.assertField(find, "recoveryEmail", strUpdate);
        TestUtils.assertField(find, "federationId", strUpdate);
        id = listId.get(1);
        eus.upsert(id, strUpdate, "", "", "");
        eus.upsert(id, "", "", strUpdate, "");
        find = eus.find(id, "", "","", "",false);
        TestUtils.assertField(find, "recoveryEmail", strUpdate);
        TestUtils.assertNotField(find, "federationId", strUpdate);
        
        find = eus.find(N_NO_ID, "","", strUpdate,"",false);
        TestUtils.assertMultiField(find, "recoveryEmail", 2);
        
        find = eus.find(N_NO_ID, strUpdate, "","","",false);
        TestUtils.assertUniqueField(find, "recoveryEmail");
        
    
    }
    
    public void smokeDelete(EndUserService eus, List<Integer> listId){
        List<Map<String, Object>> find;
        find = eus.find(EndUserServiceTests.N_NO_ID, "", "","","",false);
        int currentSize = find.size();
        listId.stream().forEach((id) -> {
            SessionMediator.setEmulationMode(id);
            eus.delete(id);
        });
        find = eus.find(EndUserServiceTests.N_NO_ID, "", "","","",false);
        Assert.assertEquals("Check all the records have been deleted",
                currentSize - listId.size(), find.size()); 
        
    }
    
    @Test
    public void Crud(){
        List<Integer> listId = new ArrayList<>(EndUserServiceTests.N_RECORDS);
        EndUserService bs = new EndUserService();
        
        // create
        this.smokeCreate(bs, listId);
        
        // update
        this.smokeUpdate(bs, listId);
        
        // delete
        this.smokeDelete(bs, listId);
        
    }
    
}
