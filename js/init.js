var socket = null;
var isopen = false;
window.onload = function() {
            socket = new WebSocket("ws://127.0.0.1:9000");
            socket.onopen = function() {
               console.log("Connected!");
               isopen = true;
            }
            socket.onmessage = function(e) {
              var json = JSON.parse(e.data);
              console.log(json.songs)
              if(json.songs){
                dispSongs(json.songs)
              }
            }

            socket.onclose = function(e) {
               console.log("Connection closed.");
               socket = null;
               isopen = false;
            }
         };



function dispSongs(_songs){
  var html = ''
  _.each(_songs, function(_song){
    html+='<div><audio controls><source src="../songs/' + _song + '" type="audio/mpeg">Your browser does not support the audio element.</audio></div>'
  });
  console.log(html);
  $('#songs').html(html);
}
