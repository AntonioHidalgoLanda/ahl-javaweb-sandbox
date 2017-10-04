/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function AhlTable(api_get,api_post,openRowFunction){
    this.namingField = "name";
    this.identifyingField = "id";
    this.API_GET = api_get;
    this.API_POST = api_post;
    this.tableId = "";
    this.columns = {};
    this.table = null;
    this.open = openRowFunction; //open(id, readonly)
}

AhlTable.prototype.setNamingField = function(name){
    this.namingField = name;
};

AhlTable.prototype.setIdentifyingField = function(id){
    this.identifyingField = id;
};

AhlTable.prototype.setTableId = function (divId) {
    this.tableId = divId;
    return this;
};

AhlTable.prototype.setColumns = function (columns) {
    this.columns = columns;
    return this;
};

// protected
AhlTable.prototype.generateTableStructure = function () {
    var tableDom = $('#'+this.tableId);
    var domHeathers = "<thead> "
                + " <tr> "
                + "  <th>"+this.namingField+"</th> ";
    for (var column in this.columns){
        domHeathers += " <th>"+this.columns[column]+"</th> ";
    }
    domHeathers+=  " </tr> "
                 +"</thead>";
    tableDom.html(domHeathers);
    return this;
};

// protected
AhlTable.prototype.getTableColumnMap = function () {
    var columns = [
            { "data": this.namingField }
        ];
    for (var column in this.columns){
        columns.push( { "data": column }) ;
    }
    return columns;
};

// public
AhlTable.prototype.populateTable = function (){
    var controller = this;
    this.generateTableStructure();
    this.table= $('#'+this.tableId).DataTable( {
        "processing": true,
        "rowId" : this.identifyingField,
        "rowCallback": function( row, data, index ) {
            if (typeof data['readonly'] !== 'undefined' && data['readonly']){
                $(row).css('color', 'gray');
            }
        },
        "columns": this.getTableColumnMap()
    } );
    $('#'+this.tableId+' tbody').on( 'click', 'tr', function () {
        var rowData = controller.table.row(this).data();
        var id = rowData['id'];
        var readonly = rowData['readonly'];
        if (typeof controller.open === 'function'){
            controller.open(id, readonly);
        }
    } );
    this.refresh();
    return this;
};

// public
// Virtual; This can/should be override
AhlTable.prototype.refresh = function(){
    var controller = this;
    $.ajax({
           type: "GET",
           url: this.API_GET,
           success: function(dataSet)
           {
                controller.load(dataSet);
           }
         });
};

AhlTable.prototype.load = function(dataSet){
    if (this.table !== null){
        this.table.clear(0);
        this.table.rows.add(dataSet);
        this.table.draw();
    }
    return this;
};


