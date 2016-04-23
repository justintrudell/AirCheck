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
    if(c_quality === -1){
        $(this).css('background-color', 'red');
        console.log($(this).css('background-color'))
    } else {
        console.log("HERE")
    }
})
