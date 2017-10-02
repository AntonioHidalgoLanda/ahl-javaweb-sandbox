/* 
 * IMPORTANT: BrandController is a subclass of controller and you need to 
 * load both js files
 * 
 */

/* global Controller */

function BrandController(){
    Controller.call(this,"/brands","/brand");
    
}

BrandController.prototype = Object.create(Controller.prototype);
BrandController.prototype.constructor = BrandController;



// override
BrandController.prototype.generateTableStructure = function () {
    var tableDom = $('#'+this.tableId);
    tableDom.html("<thead> "
                 + " <tr> "
                 +  " <th>Name</th> "
                 +  " <th>URL</th> "
                 + " </tr> "
                 +"</thead>");
    return this;
};

// override
BrandController.prototype.getTableColumnMap = function () {
    return [
            { "data": this.namingField },
            { "data": "pageurl" }
        ];
};

// override
BrandController.prototype.generateInputFieldsForm = function (){
    return '   <label for="'+this.formDivId+'-name">Name</label> '
         + '   <input type="text" name="name" id="'+this.formDivId+'-name" value="" class="text ui-widget-content ui-corner-all"> '
         + '   <label for="'+this.formDivId+'-pageurl">page URL</label> '
         + '   <input type="text" name="pageurl" id="'+this.formDivId+'-pageurl" value="" class="text ui-widget-content ui-corner-all"> ';
};

// Could be override
BrandController.prototype.generateView = function (){
     var div = $("#"+this.viewDivId);
     div.html( '<label for="f-'+this.viewDivId+'-'+this.identifyingField+'">ID</label>'
             + '<div id="f-'+this.viewDivId+'-'+this.identifyingField+'"></div>'
             + '<label for="f-'+this.viewDivId+'-'+this.namingField+'">Name</label>'
             + '<div id="f-'+this.viewDivId+'-'+this.namingField+'"></div>'
             + '<label for="f-'+this.viewDivId+'-pageurl">Page URL</label>'
             + '<div id="f-'+this.viewDivId+'-pageurl"></div>');
    return this;
};

// Subscribe to a brand -- give an user write rights to a brand
// INITIALLY THIS WILL BE DONE WITH SQL SCRIPT DIRECTLY IN THE DB

// upsert branded Igotit

// upsert SKU?? (product, isn't it?)


