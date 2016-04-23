window.onload = function(){
    document.getElementById("locationButton").onclick = function()
    {
        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(
            populatePosition,
            displayError
          );
        }
        else {
          alert("Geolocation is not supported by this browser");
        }

        function populatePosition(position) {
          document.getElementById('latitude').value = Math.round(position.coords.latitude);
          document.getElementById('longitude').value = Math.round(position.coords.longitude);
        }

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
    const red = 0;
    const blue = 255;
    const divisions = 51
    c_quality = 0≠
    if(c_quality === -1){
        $('body').css('background-color', 'white');
    } else {
        c_quality++
        shown_red = divisions * c_quality
        shown_blue = 255 - shown_red
        $('body').css('background-color', 'rgb(' + shown_red + ',0,'+ shown_blue+')');
        //$('body').css('background-color', 'rgb(shown_red, 0, shown_blue)');
    }
})
