/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.List;
import java.util.Map;
import org.junit.Assert;

/**
 *
 * @author antonio
 */
public class TestUtils {
    
    public static void assertField(List<Map<String, Object>> search, String fieldName, String given){
        Assert.assertEquals("Checking " + fieldName
                + "=="+given+" returns single field. Returned: "+search.size(),1, search.size());
        String found = search.get(0).get(fieldName).toString();
        Assert.assertEquals("Check that the returned record has given name",given, found);
    }
    
    public static void assertMultiField(List<Map<String, Object>> search, String fieldName, int instances){
        Assert.assertTrue("Check find(id) returns a single record",instances <= search.size());
    }

    public static void assertNotField(List<Map<String, Object>> search, String fieldName, String given) {
        Assert.assertEquals("Check find ("+given+") returns a single record",1, search.size());
        String found = search.get(0).get(fieldName).toString();
        Assert.assertNotEquals("Checking " + fieldName
                +", expecting: " + given
                +", received: " +found, given, found);
    }

    public static void assertUniqueField(List<Map<String, Object>> search, String recoveryEmail) {
        Assert.assertTrue("Check find(id) returns a single record",search.size() == 1);
    }
    
}
