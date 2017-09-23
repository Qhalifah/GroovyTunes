from mutagen.easyid3 import EasyID3
import glob, os, json


def getMusic(_dir):
    os.chdir(_dir)
    songs = {};
    for f in glob.glob("*"):
        songs["../songs/" + f] = dict(EasyID3(f))
    return songs

# for testing
# data = getMusic("../songs")
# print(json.dumps(data, indent=4, sort_keys=True))
