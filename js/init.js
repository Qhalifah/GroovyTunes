var socket = null;
var isopen = false;
var songs = {};
var user = {};
var playlists = {};
var playlist = {};
window.onload = function() {
    if(onload == true){return}
    var onload = true;
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
            }else{
                alert(message.message);
            }
            break;
        case "retGetMusic":
            songs = message.message;
            break;
        case "retGetUser":
            break;
        case "retGetPlaylists":
            playlists = message.message;
            dispPlaylists();
            if(onload){
            var p = _.find(playlists, function(playlist){
                return playlist.playlistName == "All Songs";
            });
            dispPlaylist(p.playlistId);
            onload = false;
            }
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
  var fields = ['title', 'albumartist', 'album', 'genre', 'duration'];
  var html = '<table class="table table-hover"><tr>';
  _.each(fields, function(_field){html += '<th>' + capitalizeFirst(_field) + '</th>'});
  html += '</tr>';
  _.each(songs, function(_meta, _src) {
      if(!_.contains(playlist.songs, _meta.songId))return;
    html += '<tr>';
    _src = _src.replace(/\\/g,'\\\\');
    _.each(fields, function(_field){
        var toDisp = (_meta[_field] || '');
      html += '<td onclick="dispPlayer(\'' + _src + '\')">' + toDisp + '</td>';
    });
    if(_.size(playlists) > 1 && playlist.playlistName == "All Songs"){
        html += '<td onclick="addSong(\'' + _meta.songId + '\')">+</td>';
    }
    html += '<td onclick="removeSong(\'' + _meta.songId + '\')">-</td>';
    html += '</tr>';
  });
  html += '</table>';
  $('#songs').html(html);
  $('#playlistTitle').html((playlist.playlistName != "null" ? playlist.playlistName : "New Playlist"));
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
