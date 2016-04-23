window.onload = function(){
    document.getElementById("locationButton").onclick = function()
    {
        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(
            displayLocation,
            displayError
          );
        }
        else {
          alert("Geolocation is not supported by this browser");
        }

        function displayLocation(position) {
          var latitude = Math.round(position.coords.latitude);
          var longitude = Math.round(position.coords.longitude);
          var request = new XMLHttpRequest();

          var method = 'GET';
          var url = 'http://maps.googleapis.com/maps/api/geocode/json?latlng='+latitude+','+longitude+'&sensor=true';
          var async = true;

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
                  $('#city').val(addressComponents[i].long_name);
                }
              }
              //alert(address.city.short_name);
            }
          };
          request.send();
        };

        function displayError(error) {
          var errors = {
            1: 'Permission denied',
            2: 'Position unavailable',
            3: 'Request timeout'
          };
          alert("Error: " + errors[error.code]);
        }
    };

};

$(document).ready(function(){
    const red = 100;
    const blue = 230;
    const divisions = 23
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
