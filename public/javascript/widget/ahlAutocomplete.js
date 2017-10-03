/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function AhlAutocomplete(api_get,api_post, refreshFunction){
    this.namingField = "name";
    this.identifyingField = "id";
    this.API_GET = api_get;
    this.API_POST = api_post;
    this.refresh = refreshFunction;
    
    this.filterDiv = "";
}

AhlAutocomplete.prototype.setNamingField = function(name){
    this.namingField = name;
};

AhlAutocomplete.prototype.setIdentifyingField = function(id){
    this.identifyingField = id;
};

AhlAutocomplete.prototype.setDivId = function (divId) {
    this.filterDiv = divId;
    return this;
};

// This method could be override
AhlAutocomplete.prototype.generateDiv = function () {
    if (this.filterDiv !== ""){
        var filterDom = $( "#"+this.filterDiv );
        filterDom.html("<label for=\""+this.filterDiv+"-"+this.namingField+"\">Select </label>"
                + "<input id=\""+this.filterDiv+"-"+this.namingField+"\">"
                + "<input type=\"hidden\" id=\""+this.filterDiv+"-"+this.identifyingField+"\" name=\"id\">");
    };
    return this;
};

AhlAutocomplete.prototype.select = function ( id , name) {
    if (id <= 0){
        name = name.replace('(create new) ','');
        this.addNew(name);

    }
    if (this.filterDiv !== "") {
        $("#"+this.filterDiv+"-"+this.identifyingField).val(id);
        $("#"+this.filterDiv+"-"+this.namingField).val(name);
    }
};

AhlAutocomplete.prototype.bind = function (){
        var controller = this;
        this.generateDiv();
        $( "#"+this.filterDiv+"-"+this.namingField ).autocomplete({
            source: function (request, response){
                var data = {'extended' : 'false'};
                data[controller.namingField] = '%'+request.term+'%';
                
                $.ajax({
                    type: "GET",
                    url: controller.API_GET,
                    data: data,
                    success: function(data)
                    {
                        var source = [];
                        for (var field in data){
                            var item = data[field];
                            source.push({'label':item.name,'value':item.id});
                        }
                        source.push({'label':'(create new) '+request.term,'value':-1});
                        response(source);
                    }
                  });
            },
            minLength: 3,
            select: function( event, ui ) {
                event.preventDefault();
                controller.select( ui.item.value, ui.item.label);
            }
        });
        return this;
    };
    
AhlAutocomplete.prototype.addNew = function(name) {
    var data = {};
    data[this.namingField] = name.trim();
    var controller = this;
    
    $.ajax({
        type: "POST",
        url: controller.API_POST,
        data: data,
        success: function(data)
        {   
            if (controller.filterDiv !== ""){
                $("#"+controller.filterDiv+"-"+controller.identifyingField).val(data);
            }
            if (typeof controller.refresh === 'function'){
                controller.refresh();
            }
        }
      });
};




