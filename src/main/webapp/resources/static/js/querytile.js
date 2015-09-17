$(document).ready(function() {
    $("#getbutton").click(function() {
        queryData();
    });
    $("#longitude").keyup(function() {
        eraseData();
    });
    
    $("#latitude").keyup(function() {
        eraseData();
    });
    
    $("#zoom").keyup(function() {
        eraseData();
    });
    
    
    function queryData() {
       var x = $("#longitude").val();
       var y = $("#latitude").val();
       var page = $("#page").val();
       var z = $("#zoom").val();
       var layer = $("#layer").val();
       
       if( x && y && z && layer   ) {           
// da sostituire con la chiamata all'API Entando
           L.map('map').setView([y,x], z);
           
           $.getJSON("http://inno.crs4.it/inno/json/tile/"+layer+"/"+x+"/"+y+"/"+z, {}, function(data) {
              if ( data.id )  {
                    var html = '<div class="panel-heading">' +
                    '<a id="aria-menu-info" class="display-block btn btn-default collapsed" ' +
                    '   role="menuitem" style="font-weight: bold;" aria-haspopup="true" ' +
                    '   href="#informazioni" data-toggle="collapse">' +
                    '<span class="icon icon-info-sign icon-white"></span>Tassello: ' + data.id +
                    '<span class="icon icon-chevron-down icon-white" style="float: right;"></span></a>' +        
                    '</div>' +
                    '<div style="height: 0px;" class="collapse" id="informazioni">' +
                    'Area: ' + data.bbox.coordinates + '<br/>' +
                    'Pagina:  <strong>' + data.page + '</strong> di<strong>' + data.pages + '</strong><br/>' +
                    'Elementi:<br/>' +
                    '    <table class="table-striped table table-condensed">' +
                    '    <tbody><tr>' +
                    '    <td class="span3"><strong>ID</strong></td>' +
                    '    <td class="span3"><strong>GEOMETRIA</strong></td>' +
                    '    </tr>';
                    for ( i = 0 ; i < data.objs.length; i++ ) {
                         html += '<tr><td>' + data.objs[i].id + "</td>" +
                         '<td>' + data.objs[i].g + '</td>';
                    } 
                    html += '</table></div>';
                    document.getElementById('data').innerHtml = html;     
              }

           }); 
      }
    };
    
        

   

});

