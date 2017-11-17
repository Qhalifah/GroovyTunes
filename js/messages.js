function get_playlists(){
    var msg = {
        'type':'get-playlists',
        'level':'playlist-level',
    }
    socket.send(JSON.stringify(msg));
}


function get_all_songs(){
    msg = {
        'type':'get-all-songs',
        'level':'playlist-level'
    }
    socket.send(JSON.stringify(msg));
}

function createPlaylist(){
    var msg = {
        type: 'create-playlist',
        name: generateName(),
        level:'playlist-level',
    }
    socket.send(JSON.stringify(msg));
}

function get_playlist_songs(_name){
    var msg = {
        'type': 'get-songs',
        'name': _name,
        'level':'playlist-level',
    }
    socket.send(JSON.stringify(msg));
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


//need to be reworked
function removeSong(_id){
    var msg = {
        type: 'removeSong',
        playlistId: playlist.playlistId,
        songId: _id,
    }
    socket.send(JSON.stringify(msg));
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
