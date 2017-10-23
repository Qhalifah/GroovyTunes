var socket = null;
var isopen = false;
var songs = {};
var user = {};
var playlists = {};
var playlist = {};
window.onload = function() {
    $( "#dialog" ).hide();
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
            break;
        case "retGetUser":
            break;
        case "retGetPlaylists":
            playlists = message.message;
            dispPlaylists();
            var p = _.find(playlists, function(playlist){
                return playlist.playlistName == "All Songs";
            });
            dispPlaylist(p.playlistId);
            break;
        case "retDispPlaylist":
            playlist = message.message;
            showPlaylist();
            break;
    }
  }

  socket.onclose = function(e) {
    console.log("Connection closed.");
    socket = null;
    isopen = false;
  }
};

function showPlaylist() {
  var fields = ['title', 'albumartist', 'album', 'genre', 'duration']
  var html = '<table class="table table-hover"><tr>'
  _.each(fields, function(_field){html += '<th>' + capitalizeFirst(_field) + '</th>'});
  html += '</tr>'
  _.each(songs, function(_meta, _src) {
      if(!_.contains(playlist.songs, _meta.songId))return;
    html += '<tr>'
    _src = _src.replace(/\\/g,'\\\\');
    _.each(fields, function(_field){
        var toDisp = (_meta[_field] || '');
      html += '<td onclick="dispPlayer(\'' + _src + '\')">' + toDisp + '</td>'
    });
    if(_.size(playlists) > 1){
        html += '<td onclick="addSong(\'' + _meta.songId + '\')">+</td>'
    }
    html += '<td onclick="removeSong(\'' + _meta.songId + '\')">-</td>'
    html += '</tr>'
  });
  html += '</table>'
  $('#songs').html(html);
}

function removeSong(_song){
    var msg = {
        type: 'removeSong',
        playlistId: playlist.playlistId,
        songId: _song,
    }
    socket.send(JSON.stringify(msg));
}

function addSong(_song){
    html = '';
    _.each(playlists, function(playlist){
        if(playlist.playlistName == "All Songs") return;
        html += '<option value="' + playlist.playlistId +  ' ' + _song + '">' + (playlist.playlistName || 'New Playlist') + '</option>';
    });
    $('#options').html(html);
    $('#myModal').modal('show');
}


function dbAddSong(){
    var value =  $('#options').find(":selected").val();
    var split = value.split(" ")
    var msg = {
        type: 'addSong',
        playlistId: split[0],
        songId: split[1],
    }
    socket.send(JSON.stringify(msg));
}

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


function dispPlayer(_src){
  html = '<h3>' + songs[_src].title + ' by ' + songs[_src].albumartist +'</h3><audio controls><source src="\.' + _src + '" type="audio/mpeg">Your browser does not support the audio element.</audio>';
  $('#player').html(html);
}
