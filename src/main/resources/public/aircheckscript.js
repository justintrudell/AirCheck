window.onload = function(){
if(document.getElementById("locationButton") != null) {
//    document.getElementById("locationButton").onclick = function()
//    {
//        if (navigator.geolocation) {
//          navigator.geolocation.getCurrentPosition(
//            displayLocation,
//            displayError
//          );
//        }
//        else {
//          alert("Geolocation is not supported by this browser");
//        }
//
//        function displayLocation(position) {
//          var latitude = position.coords.latitude.toFixed(3);
//          var longitude = position.coords.longitude.toFixed(3);
//          if(document.getElementById("longitude") != null) document.getElementById("longitude").value = longitude;
//          if(document.getElementById("latitude") != null) document.getElementById("latitude").value = latitude;
//          var request = new XMLHttpRequest();
//
//      map = new GMaps({
//        el: '#map',
//        lat: latitude,
//        lng: longitude,
//        zoomControl : true,
//        zoomControlOpt: {
//            style : 'SMALL',
//            position: 'TOP_LEFT'
//        },
//        panControl : false,
//        streetViewControl : false,
//        mapTypeControl: false,
//        overviewMapControl: false,
//      });
//      map.addMarker({
//          lat: latitude,
//          lng: longitude,
//      });
//
//          var method = 'GET';
//          var url = 'http://maps.googleapis.com/maps/api/geocode/json?latlng='+latitude+','+longitude+'&sensor=true';
//          var async = true;
//
//          request.open(method, url, async);
//          request.onreadystatechange = function(){
//            if(request.readyState == 4 && request.status == 200){
//              var data = JSON.parse(request.responseText);
//              console.log(data);
//              var addressComponents = data.results[0].address_components;
//              for(i=0;i<addressComponents.length;i++){
//                var types = addressComponents[i].types
//                //alert(types);
//                if(types=="locality,political"){
//                  //alert(addressComponents[i].long_name); // this should be your city, depending on where you are
//                  document.getElementById("city").value = addressComponents[i].long_name;
//                  try {
//                  $('#city').val(addressComponents[i].long_name);
//                  }
//                  catch(err) {
//                  }
//                }
//              }
//              alert(address.city.short_name);
//              document.getElementById("city").value = address.city.short_name;
//            }
//          };
//          request.send();
//        };
//
//        function displayError(error) {
//          var errors = {
//            1: 'Permission denied',
//            2: 'Position unavailable',
//            3: 'Request timeout'
//          };
//          alert("Error: " + errors[error.code]);
//        }
//    };
}
if(document.getElementById("submitCity") != null) {
    document.getElementById("submitCity").onclick = function()
//        {
//            $.get("getCityCoords", { city: document.getElementById("city").value }, function(res){
//                    var json = JSON.parse(res);
//                    document.getElementById("longitude").value = json['latitude'];
//                    document.getElementById("latitude").value = json['longitude'];
//                    map = new GMaps({
//                            el: '#map',
//                            lat: json['latitude'],
//                            lng: json['longitude'],
//                            zoomControl : true,
//                            zoomControlOpt: {
//                                style : 'SMALL',
//                                position: 'TOP_LEFT'
//                            },
//                            panControl : false,
//                            streetViewControl : false,
//                            mapTypeControl: false,
//                            overviewMapControl: false,
//                          });
//                          map.addMarker({
//                              lat: json['latitude'],
//                              lng: json['longitude'],
//                          });
//                });
//                $(map).attr("id", "map-canvas")
//
//            /**
//            * HEATMAP JS
//            **/
//
//            var latlng = new google.maps.LatLng(43.6532,-79.3832)
//            var myOptions = {
//              zoom: 3,
//              center: latlng
//            };
//            heat_map = new google.maps.Map(document.getElementById("map-canvas"), myOptions);
//
//            heatmap = new HeatmapOverlay(heat_map,
//              {
//                // radius should be small ONLY if scaleRadius is true (or small radius is intended)
//                "radius": 2,
//                "maxOpacity": 1,
//                // scales the radius based on map zoom
//                "scaleRadius": true,
//                // if set to false the heatmap uses the global maximum for colorization
//                // if activated: uses the data maximum within the current map boundaries
//                //   (there will always be a red spot with useLocalExtremas true)
//                "useLocalExtrema": true,
//                // which field name in your data represents the latitude - default "lat"
//                latField: 'lat',
//                // which field name in your data represents the longitude - default "lng"
//                lngField: 'lng',
//                // which field name in your data represents the data value - default "value"
//                valueField: 'count'
//              }
//            );
//
//            var testData = {
//              max: 8,
//              data: [{lat: 24.6408, lng:46.7728, count: 3},{lat: 50.75, lng:-1.55, count: 1}]
//            };
//
//            heatmap.setData(testData);
//        };
    }
};

var map;
$(document).ready(function(){
    const red = 100;
    const blue = 230;
    const divisions = 23
//
//    map = new GMaps({
//            el: '#map',
//            lat: 43.6532,
//            lng: -79.3832,
//            zoomControl : true,
//            zoomControlOpt: {
//                style : 'SMALL',
//                position: 'TOP_LEFT'
//            },
//            panControl : false,
//            streetViewControl : false,
//            mapTypeControl: false,
//            overviewMapControl: false
//          });
    //Comment following line out if you hate color
    //c_quality = -1
    if(c_quality === -1){
        $('body').css('background-color', 'white');
    } else {
        c_quality++
        shown_red =  red + divisions * c_quality
        shown_blue = blue - shown_red
        $('body').css('background-color', 'rgb(' + shown_red + ',180,'+ 100+')');
        //$('body').css('background-color', 'rgb(shown_red, 0, shown_blue)');
    }


})

