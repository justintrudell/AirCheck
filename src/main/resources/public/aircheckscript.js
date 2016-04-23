function setBgColor(c_quality) {
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
    $("#air-quality-status").fadeOut(200);
    //Comment following line out if you hate color
    //c_quality = -1
    if(c_quality === -1){
        $('#air-quality-status').css('background-color', 'white');
    } else {
        c_quality++
        shown_red =  red + divisions * c_quality
        shown_blue = blue - shown_red
        $('#air-quality-status').css('background-color', 'rgb(' + shown_red + ',180,'+ 100+')');
        //$('body').css('background-color', 'rgb(shown_red, 0, shown_blue)');
    }
    $("#air-quality-status").fadeIn(200);
}

function displayLocation(position) {
    map.setCenter({lat: position.coords.latitude, lng: position.coords.longitude})
    var latitude = position.coords.latitude.toFixed(3);
    var longitude = position.coords.longitude.toFixed(3);
    if(document.getElementById("longitude") != null) document.getElementById("longitude").value = longitude;
    if(document.getElementById("latitude") != null) document.getElementById("latitude").value = latitude;
    var request = new XMLHttpRequest();

//    map = new GMaps({
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
//    });
//    map.addMarker({
//        lat: latitude,
//        lng: longitude,
//    });

    var method = 'GET';
    var url = 'http://maps.googleapis.com/maps/api/geocode/json?latlng='+latitude+','+longitude+'&sensor=true';
    var async = true;
    var city = "";

    request.open(method, url, async);
    request.onreadystatechange = function(){
        if(request.readyState == 4 && request.status == 200){
            var data = JSON.parse(request.responseText);
            console.log(data);
            var addressComponents = data.results[0].address_components;
            for(i=0;i<addressComponents.length;i++){
                var types = addressComponents[i].types
                //alert(types);
                if(types=="locality,political"){
                    //alert(addressComponents[i].long_name); // this should be your city, depending on where you are
                    document.getElementById("city").value = addressComponents[i].long_name;
                    try {
                        $('#city').val(addressComponents[i].long_name);
                        city = addressComponents[i].long_name;
                    }
                    catch(err) {
                    }

                    // Ajax to get weather and air quality data
                    if(city != "") {
                        $.ajax({
                          url: window.location.href + "data",
                          data: {
                            "city" : city
                          },
                          type: "GET"
                        }).done(function(data) {
                            $('#air-quality').text("Air Quality:"  + data.quality);
                            $('#humidity').text("Humitidy: " + data.humidity);
                            setBgColor(data.color_quality);
                        });
                    }
                }
            }
        }
    };
    request.send();
}

function displayError(error) {
    var errors = {
        1: 'Permission denied',
        2: 'Position unavailable',
        3: 'Request timeout'
    };
    alert("Error: " + errors[error.code]);
}

window.onload = function(){
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            displayLocation,
            displayError
        );
    }
    else {
        alert("Geolocation is not supported by this browser");
    }
}

