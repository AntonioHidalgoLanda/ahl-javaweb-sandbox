/* 
 * IMPORTANT: BrandController is a subclass of controller and you need to 
 * load both js files
 * 
 * browse
 * 
 * search
 * 
 * create
 * 
 * rate
 */

/* global ahlDisplay, ahlTable, ahlAutocomplete */

function ProductController(){
    var that = this;
    this.refresh = function () {
        that.table.refresh();
        return that;
    };  
    this.open = function(id, readonly){
        that.display.openRecord(id, readonly);
        return that;
    };
    
    //
    // #TODO
    // Ideally, we want to use a first call to all the products and then
    // a second call to all the brands that we need
    // but, currently, it is not possible, because we can only request 
    // one single ID to brands, not a list
    this.productRefresh = function(){
        $.ajax({
               type: "GET",
               url: that.table.API_GET,
               success: function(productDataSet)
               {
                   for (var i in productDataSet){
                       productDataSet[i]['brand'] = '';
                   }
                   that.table.load(productDataSet);

                   for (var i in productDataSet){
                       var product = productDataSet[i];
                       that.brandRefresh(product.id,product.brandid);
                    }
               }
         });
    };  
    this.brandRefresh = function(productid, brandid){
        $.ajax({
            type: "GET",
            url: '/brands',
            data: {'id':brandid},
            success: function(brandDataSet)
            {
                var row = that.table.table.row("#"+productid);
                var data = row.data();
                data ['brand'] = brandDataSet[0].name;
                row.data(data).draw();
            }
        });
    };
    
    this.productOpen = function(id,readonly){
        var data = {'extended' : 'true','id':id};

        $.ajax({
               type: "GET",
               url: that.display.API_GET,
               data: data,
               success: function(data)
               {
                    if (data.constructor === Array){
                        that.brandOpen(data[0], readonly);
                    }
               }
        });
    };
    this.brandOpen = function(productData,readonly){
        var data = {'extended' : 'false'};
        data['id'] = productData['brandid'];

        $.ajax({
               type: "GET",
               url: '/brands',
               data: data,
               success: function(data)
               {
                    if (data.constructor === Array){
                        productData['brand'] = data[0].name;
                        that.display.displayData(productData, readonly);
                    }
               }
         });
    };
    
    this.display = new AhlDisplay('/products', '/product',this.refresh);
    this.table = new AhlTable('/products', '/product', this.open);
    
    // SETUP THE REST
    this.table.refresh = this.productRefresh;
    this.display.openRecord = this.productOpen;
    this.init();
}

ProductController.prototype.init = function(){
    var fields = {'sku':'SKU','brandLink':'Brand Link'};    // #TODO Brand should be diplayed but read only
    var columns = {'brand':'Brand','sku':'SKU','brandLink':'Brand Link'};
    this.display.setFields(fields);
    this.table.setColumns(columns);
};


         // Missing -- LISTS
         // shoppingOnlineLinkList (list); storeList (list); igotitList (list)
         // 
         // SKU, brandedIgotitId, name, brand (EDITABLE)
         //   showld be onli accessible to Brand (people with  Write rigths)
         //        Brand should have themselves as a reseller to
         //        
         // shoppingOnlineLinkList, storeList (READ ONLY)
         //    Are external links, and are editable by Resellers which has
         //    WRITE access to the pertinent tables
         // igotitList No editable here ; Displayed only Top XXX; Ranking based on Usefulness// pagerank

