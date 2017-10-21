var socket = null;
var isopen = false;
var songs = {};
window.onload = function() {
  socket = new WebSocket("ws://127.0.0.1:8080");
  socket.onopen = function() {
    console.log("Connected!");
    isopen = true;
    socket.send('getMusic')
  }
  socket.onmessage = function(e) {
    var json = JSON.parse(e.data);
    console.log(json);
    if (json) {
      songs = json;
      dispSongs();
    }
  }

  socket.onclose = function(e) {
    console.log("Connection closed.");
    socket = null;
    isopen = false;
  }
};

function capitalizeFirst(_str){
  return _str.charAt(0).toUpperCase() + _str.slice(1);
}

function dispSongs() {
  var fields = ['title', 'albumartist', 'album', 'genre', 'length']
  var html = '<table class="table table-hover"><tr>'
  _.each(fields, function(_field){html += '<th>' + capitalizeFirst(_field) + '</th>'});
  html += '</tr>'
  _.each(songs, function(_meta, _src) {
    html += '<tr>'
    _src = _src.replace(/\\/g,'\\\\');
    _.each(fields, function(_field){
      html += '<td onclick="dispPlayer(\'' + _src + '\')">' + (_meta[_field] || '') + '</td>'
    });
    html += '</tr>'
  });
  html += '</table>'
  $('#songs').html(html);
}

function dispPlayer(_src){
  html = '<h3>' + songs[_src].title + ' by ' + songs[_src].albumartist +'</h3><audio controls><source src="\.' + _src + '" type="audio/mpeg">Your browser does not support the audio element.</audio>';
  $('#player').html(html);
}
