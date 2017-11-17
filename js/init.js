var socket = null;
var isopen = false;
var songs = {};
var user = {};
var playlists = {};
var playlist = {};
window.onload = function() {
    var originalValue;
    $('#playlistTitle').on('dblclick', function(){
        originalValue = $(this).text();
        console.log(originalValue)
        $(this).text('');
        $("<input type='text'>").appendTo(this).focus();
    });
    $('#playlistTitle').on('focusout', 'input', function(){
        if($(this).val() == '' || $(this).val() == originalValue){
            $(this).parent().text(originalValue);
            console.log('ding');
        }else{
            var msg = {
                type: 'changeName',
                id: playlist.playlistId,
                name: $(this).val(),
            };
            socket.send(JSON.stringify(msg));
        }
        $(this).remove();
    });




  socket = new WebSocket("ws://127.0.0.1:8080");
  socket.onopen = function() {
    console.log("Connected!");
    isopen = true;
  }
  socket.onmessage = function(e) {
    var message = JSON.parse(e.data);
    console.log(message);
    switch(message.type){
        case "retLogin":
            if(message.status == 'success'){
                $('.sign_in').addClass('hidden');
                var msg = {
                    'type':'get-playlists',
                    'level':'playlist-level',
                }
                socket.send(JSON.stringify(msg));
                msg = {
                    'type':'get-all-songs',
                    'level':'playlist-level'
                }
                socket.send(JSON.stringify(msg));
            }else{
                alert(message.message);
            }
            break;
        case "retGetMusic":
            songs = message.message;
            break;
        case "retGetUser":
            break;
        case "ret-get-playlists":
            playlists = message.playlists;
            dispPlaylists();
            break;
        case "get-all-songs":
            _.each(message.songs, function(_song){
                songs[_song.songID] = _song;
            });
            showAllSongs();
            break;
        case "ret-get-songs":
            var s = {};
            _.each(message.songs, function(_song){
                s[_song.songID] = _song;
            });
            showSongs(message.name, s);
    }
  }

  socket.onclose = function(e) {
    console.log("Connection closed.");
    socket = null;
    isopen = false;
  }
};

function showSongs(_name, _songs){
    var fields = ['title', 'albumartist', 'album', 'genre', 'duration'];
    var html = '<table class="table table-hover"><tr>';
    _.each(fields, function(_field){html += '<th>' + capitalizeFirst(_field) + '</th>'});
    html += '</tr>';
    _.each(_songs, function(_meta, _src) {
      html += '<tr>';
      _src = _src.replace(/\\/g,'\\\\');
      _.each(fields, function(_field){
          var toDisp = (_meta[_field] || '');
        html += '<td onclick="dispPlayer(\'' + _src + '\')">' + toDisp + '</td>';
      });
      html += '<td onclick="addSong(\'' + _meta.songId + '\')">+</td>';
      html += '</tr>';
    });
    html += '</table>';
    $('#songs').html(html);
    $('#playlistTitle').html(_name);
}
function showAllSongs() {
  showSongs('All Songs', songs)
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
        html += '<li onclick="showAllSongs()">' + 'All Songs' + '</li>';
    _.each(playlists, function(playlist){
        html += '<li onclick="dispPlaylist(\'' + playlist.name + '\')">' + (playlist.name || 'New Playlist') + '</li>';
    });
    $('#playlists').html(html);
}

function dispPlaylist(_name){
    var msg = {
        'type': 'get-songs',
        'name': _name,
        'level':'playlist-level',
    }
    socket.send(JSON.stringify(msg));
}


function dispPlayer(_src){
  html = '<h3>' + songs[_src].title + ' by ' + songs[_src].albumartist +'</h3><audio controls><source src="\.' + _src + '" type="audio/mpeg">Your browser does not support the audio element.</audio>';
  $('#player').html(html);
}

function sign_in(){

}

function sign_up(){

}


function login(){
    var username = $('#inputUsername').val();
    var pass = $('#inputPassword').val();
    var msg = {
        'type': 'login',
        'level': 'user',
        'username': username,
        'password':pass,
    }
    socket.send(JSON.stringify(msg));
}
