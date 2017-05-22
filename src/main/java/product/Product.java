/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

/**
 *
 * @author antonio
 */
public class Product {
    
    private String sku;
    private String serialNumber;
    private String description;
    private int brandID;  // This is an example, and at some point we expect that this will be another class
    
    public Product(String sku, String serialNumber, String description, int brandID) {
        this.sku = sku;
        this.serialNumber = serialNumber;
        this.description = description;
        this.brandID = brandID;
    }

    public String getSku() {
        return this.sku;
    }
    
    public String getSerialNumber() {
        return this.serialNumber;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public int getBrandID() {
        return this.brandID;
    }
    
    public Product setSku(String sku){
        this.sku = sku;
        return this;
    }
    
    public Product setSerialNumber(String serialNumber){
        this.serialNumber = serialNumber;
        return this;
    }
    
    public Product setDescription(String description){
        this.description = description;
        return this;
    }
    
    public Product setBrandID(int brandID){
        this.brandID = brandID;
        return this;
    }
    
}
