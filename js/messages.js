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

function add_song(){
    var value =  $('#options').find(":selected").val();
    var split = value.split(" ")
    var msg = {
        'type': 'add-song',
        'level': 'playlist-level',
        'name': split[0],
        'id': split[1],
    }
    socket.send(JSON.stringify(msg));
}

function rename_playlist(_new_name){
    var msg = {
        'type': 'rename-playlist',
        'level':'playlist-level',
        'name': playlist,
        'new-name': _new_name,
    };
    socket.send(JSON.stringify(msg));
}

//need to be reworked
function remove_song(_id){
    var msg = {
        'type': 'remove-song',
        'level':'playlist-level',
        'name': playlist,
        'id': _id,
    }
    socket.send(JSON.stringify(msg));
}
