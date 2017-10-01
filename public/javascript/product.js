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

/* global Controller */

function ProductController(){
    Controller.call(this,"/products","/product");
    
}

ProductController.prototype = Object.create(Controller.prototype);
ProductController.prototype.constructor = ProductController;



// override
ProductController.prototype.generateTableStructure = function () {
    var tableDom = $('#'+this.tableId);
    tableDom.html("<thead> "
                 + " <tr> "
                 +  " <th>Name</th> "
                 +  " <th>Brand</th> "
                 +  " <th>SKU</th> "
                 +  " <th>Brand Link</th> "
                 + " </tr> "
                 +"</thead>");
    return this;
};

// override
ProductController.prototype.getTableColumnMap = function () {
    return [
            { "data": this.namingField },
            { "data": "brand" },
            { "data": "sku" },
            { "data": "brandLink" }
        ];
        // hidden data: brandedIgotitId (field); shoppingOnlineLinkList (list); storeList (list); igotitList (list)
};

ProductController.prototype.tableBrandRefresh = function(productid, brandid){
      var controller = this;
      $.ajax({
            type: "GET",
            url: '/brands',
            data: {'id':brandid},
            success: function(brandDataSet)
            {
                var row = controller.table.row("#"+productid);
                var data = row.data();
                data ['brand'] = brandDataSet[0].name;
                row.data(data).draw();
            }
          });
};


//
// #TODO
// Ideally, we want to use a first call to all the products and then
// a second call to all the brands that we need
// but, currently, it is not possible, because we can only request 
// one single ID to brands, not a list
ProductController.prototype.tableRefresh = function(){
    var controller = this;
    $.ajax({
           type: "GET",
           url: this.API_GET,
           success: function(productDataSet)
           {
               for (var i in productDataSet){
                   productDataSet[i]['brand'] = '';
               }
               controller.tableLoad(productDataSet);
               
               for (var i in productDataSet){
                   var product = productDataSet[i];
                   controller.tableBrandRefresh(product.id,product.brandid);
                }
           }
         });
};

// override
ProductController.prototype.generateInputFieldsForm = function (){
    return '   <label for="'+this.formDivId+'-name">Name</label> '
         + '   <input type="text" name="name" id="'+this.formDivId+'-name" value="" class="text ui-widget-content ui-corner-all"> '
         + '   <label for="'+this.formDivId+'-sku">SKU</label> '
         + '   <input type="text" name="sku" id="'+this.formDivId+'-sku" value="" class="text ui-widget-content ui-corner-all"> '
         // BRAND ?? Who can change a brand
         // Use Case, reseller, they shouldn't be changing but clonning
         // We need Garbage collector to remove products with no IGOTIT, 
         + '   <label for="'+this.formDivId+'-brandLink">Brand Link</label> '
         + '   <input type="text" name="brandLink" id="'+this.formDivId+'-brandLink" value="" class="text ui-widget-content ui-corner-all"> ';

         // Missing -- LISTS
         // shoppingOnlineLinkList (list); storeList (list); igotitList (list)
         // 
         // SKU, brandedIgotitId, name, brand
         //   showld be onli accessible to Brand (people with  Write rigths)
         //        Brand should have themselves as a reseller to
         //        
         // shoppingOnlineLinkList, storeList
         //    Are external links, and are editable by Resellers which has
         //    WRITE access to the pertinent tables
         // igotitList No editable here
};

// dialod for readonly

