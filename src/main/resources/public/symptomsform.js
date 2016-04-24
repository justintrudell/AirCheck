var map, sympmap, tempmap;

function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 13,
    center: {lat: 37.775, lng: -122.434},
  });

}

function toggleHeatmap() {
  heatmap.setMap(heatmap.getMap() ? null : map);
}

var grad = [
'rgba(0, 255, 255, 0)',
'rgba(0, 255, 255, 1)',
'rgba(0, 191, 255, 1)',
'rgba(0, 127, 255, 1)',
'rgba(0, 63, 255, 1)',
'rgba(0, 0, 255, 1)',
'rgba(0, 0, 223, 1)',
'rgba(0, 0, 191, 1)',
'rgba(0, 0, 159, 1)',
'rgba(0, 0, 127, 1)',
'rgba(63, 0, 91, 1)',
'rgba(127, 0, 63, 1)',
'rgba(191, 0, 31, 1)',
'rgba(255, 0, 255, 1)'
]


function changeRadius() {
  heatmap.set('radius', heatmap.get('radius') ? null : 20);
}

function changeOpacity() {
  heatmap.set('opacity', heatmap.get('opacity') ? null : 0.4);
}



$(document).ready(function(){

      function getOp(){
        $.ajax("http://localhost:4567/cityJSON", {
            success: function(data) {
                ret = []
                for (i in data){
                    ret.push({
                        location: new google.maps.LatLng(data[i]['latitude'], data[i]['longitude']), weight: data[i]['weight'] / 100
                    })
                }
                sympmap = new google.maps.visualization.HeatmapLayer({
                                        data: ret,
                                        map: map,
                                        radius: 50,
                                        maxIntensity: 1.0,
                                        gradient: grad,
                                        opacity: 0.4
                                      });
            }, error: function(){
                console.log("error")
            }
        })
      }

      getOp()

      function getWeather(){
        $.ajax("http://localhost:4567/cityJSON", {
            success: function(data){
                ret = []
                for (i in data){
                    ret.push({
                        location: new google.maps.LatLng(data[i]['latitude'] + 0.005, data[i]['longitude'] + 0.005), weight: data[i]['temp'] / 10
                    })
                }
                tempmap = new google.maps.visualization.HeatmapLayer({
                                        data: ret,
                                        map: map,
                                        radius: 60,
                                        maxIntensity: 1.5,
                                      });
            }, error: function(){
                console.log("Error")
            }

        })
      }

      getWeather()


    $('#heatmap').click(function(){
        $('html, body').animate({
            scrollTop: $('#one').offset().top
        }, 500);
        return false;
    });

    $('#home').click(function(){
        $('html, body').animate({
            scrollTop: $('#top').offset().top
        }, 500);
        return false;
    });

    $('#form').click(function(){
        $('html, body').animate({
            scrollTop: $('#two').offset().top
        }, 500);
        return false;
    });

    $('#toggle_symptoms').click(function(){
        if (sympmap.getMap() == null){
            sympmap.setMap(map)
        } else {
            sympmap.setMap(null)
        }
    });

    $('#generateData').click(function(){

        $.ajax("http://localhost:4567/generateUserData", {
            success: function(data) {

            }, error: function(){
                console.log("error")
            }
        })
    });


    $('#toggle_temp').click(function(){
            if (tempmap.getMap() == null){
                tempmap.setMap(map)
            } else {
                tempmap.setMap(null)
            }
        })


})

