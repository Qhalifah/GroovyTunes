var socket = null;
var isopen = false;
var songs = {};
var user = {};
var playlists = {};

window.onload = function() {
  socket = new WebSocket("ws://127.0.0.1:8080");
  socket.onopen = function() {
    console.log("Connected!");
    isopen = true;
    var msg = {
        type: 'getMusic'
    };
    socket.send(JSON.stringify(msg));
    msg = {
        type: 'getUser'
    };
    socket.send(JSON.stringify(msg));
    msg = {
        type: 'getPlaylists'
    };
    socket.send(JSON.stringify(msg));
  }
  socket.onmessage = function(e) {
    var message = JSON.parse(e.data);
    console.log(message);
    switch(message.type){
        case "retGetMusic":
            songs = message.message;
            dispSongs();
            break;
        case "retGetUser":
            break;
        case "retGetPlaylists":
            playlists = message.message;
            dispPlaylists();
            break;
    }
  }

  socket.onclose = function(e) {
    console.log("Connection closed.");
    socket = null;
    isopen = false;
  }
};

function createPlaylist(){
    var msg = {
        type: 'createPlaylist'
    }
    socket.send(JSON.stringify(msg));
}

function capitalizeFirst(_str){
  return _str.charAt(0).toUpperCase() + _str.slice(1);
}

function dispPlaylists(){
    var html = '';
    _.each(playlists, function(playlist){
        html += '<li onclick="dispPlaylist(\'' + playlist.playlistId + '\')">' + (playlist.playlistName || 'New Playlist') + '</li>';
    });
    $('#playlists').html(html);
}

function dispPlaylist(_id){
    var msg = {
        type: 'dispPlaylist',
        id: _id,
    }
    socket.send(JSON.stringify(msg));
}
function dispSongs() {
  var fields = ['title', 'albumartist', 'album', 'genre', 'duration']
  var html = '<table class="table table-hover"><tr>'
  _.each(fields, function(_field){html += '<th>' + capitalizeFirst(_field) + '</th>'});
  html += '</tr>'
  _.each(songs, function(_meta, _src) {
    html += '<tr>'
    _src = _src.replace(/\\/g,'\\\\');
    _.each(fields, function(_field){
        var toDisp = (_meta[_field] || '');
      html += '<td onclick="dispPlayer(\'' + _src + '\')">' + toDisp + '</td>'
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
