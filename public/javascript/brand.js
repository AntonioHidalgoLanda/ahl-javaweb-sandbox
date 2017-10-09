/* 
 * IMPORTANT: BrandController is a subclass of controller and you need to 
 * load both js files
 * 
 */

/* global ahlDisplay, ahlTable, ahlAutocomplete */

function BrandController(){
    this.refresh = function () {
        that.table.refresh();
        return that;
    };
    
    this.open = function(id, readonly){
        that.display.openRecord(id, readonly);
        return that;
    };
    
    this.display = new AhlDisplay('/brands', '/brand',this.refresh);
    this.table = new AhlTable('/brands', '/brand', this.open);
    this.autocomplete = new AhlAutocomplete('/brands', '/brand', this.refresh);
    var that = this;
    
    // SETUP THE REST
    this.init();
}


BrandController.prototype.init = function(){
    var columns = {'pageurl':'Page URL'};
    this.display.editFields.setReadOnlyField ('id','id');
    this.display.editFields.setTextField('name','brand');
    this.display.editFields.setTextField('pageurl','Page URL');
    this.display.viewFields.setReadOnlyField ('id','id');
    this.display.viewFields.setReadOnlyField('name','brand');
    this.display.viewFields.setReadOnlyField('pageurl','Page URL');
    this.table.setColumns(columns);
};


// Subscribe to a brand -- give an user write rights to a brand
// INITIALLY THIS WILL BE DONE WITH SQL SCRIPT DIRECTLY IN THE DB

// upsert branded Igotit

// upsert SKU?? (product, isn't it?)


