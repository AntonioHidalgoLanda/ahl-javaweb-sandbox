/* 
 * Post
 * The submit button will manage the different post calls to each entity:
 *    Create Igotit
 *    Add Photos
 *    Add Tags
 * 
 * 
 * 
 *  Ajax example with jQuery
 *  
 *  
 *  // this is the id of the submit button
$("#submitButtonId").click(function() {

    var url = "path/to/your/script.php"; // the script where you handle the form input.

    $.ajax({
           type: "POST",
           url: url,
           data: $("#idForm").serialize(), // serializes the form's elements.
           success: function(data)
           {
               alert(data); // show response from the php script.
           }
         });

    return false; // avoid to execute the actual submit of the form.
});

 * 
 *  Simple post
 *      Generate form
 *  Photo post
 *      Generate add photos form
 *  Add tags
 *      Generate add tags form
 *  rating
 *  Find Product
 *  Add products
 *  Create Product
 *  Create Brand
 * 
 * Visibility
 * 
 * See the wardrove of my connections (followers and friends)
 * 
 * See the wardrove of one connection
 * 
 * See my wardrove
 * 
 */


/* global ahlDisplay, ahlTable, ahlAutocomplete */

function IgotitController(){
    var that = this;
    this.refresh = function () {
        return that;
    };
    
    this.open = function(id, readonly){
        that.display.openRecord(id, readonly);
        return that;
    };
    
    this.igotitOpen = function(id,readonly){
        var data = {'extended' : 'true','id':id};

        $.ajax({
               type: "GET",
               url: that.display.API_GET,
               data: data,
               success: function(data)
               {
                    if (data.constructor === Array){
                        that.display.displayData(data[0], readonly);
                    }
               }
        });
    };
    
    // fetch(filters)
    // fetch(filters,last item)
    // this.wall ~= fetch(this.filters)
    
    this.display = new AhlDisplay('/igotits', '/igotit',this.refresh);
    
    this.display.openRecord = this.igotitOpen;
    this.init();
}

IgotitController.prototype.init = function(){
    // Fields: id, publishdate, enduserid, visibility, usercomment, coordinates, accessLevel, rating, photoList, productList, tagList
    this.display.editFields.setIdField ('id','id');
    this.display.editFields.setReadOnlyField('publish date','publishdate');
    this.display.editFields.setReadOnlyField ('enduser','user');
    this.display.editFields.setSelectboxField ('visibility','visibility');      // Especial, Selectbox controller
    this.display.editFields.setCoordinatesField('coordinates','coordinates');   // Especial, Coordinate controller
    this.display.editFields.setRatingField('rating','rating');                  // Especial, rating controller
    this.display.editFields.setTextField('usercomment','comment');
    this.display.editFields.setPhotoField('photoList','photos');                // Especial, upload file + photo list + camera controller
    this.display.editFields.setProductPickerField('productList','products');    // Especial, product "autocomplete"              
    this.display.editFields.setTagField('tagList','tags');                      // Especial, Tags controller
    
    this.display.viewFields.setReadOnlyField ('id','id');
    this.display.viewFields.setReadOnlyField('publish date','publishdate');     // Especial, These fields have custom CSS
    this.display.viewFields.setReadOnlyField ('enduser','user');                // Needs to convert enduserid into user (name)
    this.display.viewFields.setReadOnlyIcon ('visibility','visibility');        // Especial, field with an icon with hover text
    this.display.viewFields.setReadOnlyCoodinates('coordinates','coordinates'); // Especial, field to show a map
    this.display.viewFields.setReadOnlyDate('rating','rating');                 // Especial, display ratings
    this.display.viewFields.setReadOnlyField('usercomment','comment');
    this.display.viewFields.setReadOnlyList('photoList','photos');              // Especial, handle lists
    this.display.viewFields.setReadOnlyList('productList','products');
    this.display.viewFields.setReadOnlyList('tagList','tags');
    
    // We need to configure the wall here
    //this.wall.setColumns(columns);
};


