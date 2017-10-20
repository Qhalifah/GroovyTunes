from autobahn.twisted.websocket import WebSocketServerProtocol, \
    WebSocketServerFactory
import json, utility

class MyServerProtocol(WebSocketServerProtocol):

    def onConnect(self, request):
        print("Client connecting: {0}".format(request.peer))

    def onOpen(self):
        print("WebSocket connection open.")

    def onMessage(self, payload, isBinary):
        payload = str(payload, 'utf-8')
        print("Recieved: " + payload)
        if(payload == "getMusic"):
            data = utility.getMusic("../songs")
            json_data = json.dumps(data)
            print("Sending: music data")
            self.sendMessage((json_data).encode('utf-8'))
        else:
            print("No Action")

    def onClose(self, wasClean, code, reason):
        print("WebSocket connection closed: {0}".format(reason))


if __name__ == '__main__':

    import sys

    from twisted.python import log
    from twisted.internet import reactor

    log.startLogging(sys.stdout)

    factory = WebSocketServerFactory(u"ws://127.0.0.1:8080")
    factory.protocol = MyServerProtocol

    reactor.listenTCP(8080, factory)
    reactor.run()
