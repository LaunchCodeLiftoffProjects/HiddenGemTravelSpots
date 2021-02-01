let x = document.getElementById("currentLocation");

function getLocation() {
 if (navigator.geolocation) {
   navigator.geolocation.watchPosition(showPosition);
 } else {
   x.innerHTML = "Geolocation is not supported by this browser.";
 }
}

function showPosition(position) {
   x.innerHTML="Latitude: " + position.coords.latitude +
   "<br>Longitude: " + position.coords.longitude;
}
function moveNumbers(num) {
   let txt=document.getElementById("result").value;
   txt=txt + num;
   document.getElementById("result").value=txt;
   }

