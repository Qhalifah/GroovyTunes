var socket = null;
var isopen = false;
var songs = {};
var playlists = {};
var playlist = '';
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
                $('.sign_in').addClass('hidden_element');
                get_playlists();
                get_all_songs();
            }else{
                alert(message.message);
            }
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
            playlist = message.name;
            var s = {};
            _.each(message.songs, function(_song){
                s[_song.songID] = _song;
            });
            showSongs(message.name, s);
            break;
        case 'ret-create-playlist':
            get_playlists();
            break;
        case 'ret-add-song':
            if(playlist == message.name){
                get_playlist_songs(playlist);
            }
            break;
    }
  }

  socket.onclose = function(e) {
    console.log("Connection closed.");
    socket = null;
    isopen = false;
  }
};

function showSongs(_name, _songs, _add){
    var fields = ['title', 'albumartist', 'album', 'genre', 'duration'];
    var html = '<table class="table table-hover"><tr>';
    _.each(fields, function(_field){html += '<th>' + capitalizeFirst(_field) + '</th>'});
    html += '</tr>';
    _.each(_songs, function(_meta, _id) {
        html += '<tr>';
        _.each(fields, function(_field){
            var toDisp = (_meta[_field] || '');
            html += '<td onclick="dispPlayer(\'' + _id + '\')">' + toDisp + '</td>';
        });
        if(_add){
            html += '<td onclick="clickedAddButton(\'' + _id + '\')">+</td>';
        }else{
            html += '<td onclick="removeSong(\'' + _id + '\')">-</td>';
        }
        html += '</tr>';
    });
    html += '</table>';
    $('#songs').html(html);
    $('#playlistTitle').html(_name);
}
function showAllSongs() {
    showSongs('All Songs', songs, true)
}

function clickedAddButton(_id){
    html = '';
    _.each(playlists, function(playlist){
        html += '<option value="' + playlist.name +  ' ' + _id + '">' + playlist.name + '</option>';
    });
    $('#options').html(html);
    $('#myModal').modal('show');
}

function capitalizeFirst(_str){
  return _str.charAt(0).toUpperCase() + _str.slice(1);
}

function generateName(){
    return 'new_' + (new Date()).getMilliseconds();
}

function dispPlaylists(){
    var html = '';
        html += '<li onclick="showAllSongs()">' + 'All Songs' + '</li>';
    _.each(playlists, function(playlist){
        html += '<li onclick="get_playlist_songs(\'' + playlist.name + '\')">' + (playlist.name || 'New Playlist') + '</li>';
    });
    $('#playlists').html(html);
}


function dispPlayer(_src){
  html = '<h3>' + songs[_src].title + ' by ' + songs[_src].albumartist +'</h3><audio controls><source src="\.' + _src + '" type="audio/mpeg">Your browser does not support the audio element.</audio>';
  $('#player').html(html);
}

function sign_in(){

}

function sign_up(){

}
