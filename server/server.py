from autobahn.twisted.websocket import WebSocketServerProtocol, \
    WebSocketServerFactory
import glob, os, json

class MyServerProtocol(WebSocketServerProtocol):

    def onConnect(self, request):
        print("Client connecting: {0}".format(request.peer))

    def onOpen(self):
        print("WebSocket connection open.")

    def onMessage(self, payload, isBinary):
        if(payload == "getMusic"):
            os.chdir('../songs')
            data = {};
            data['songs'] = []
            for f in glob.glob("*"):
                data['songs'].append(f)
            json_data = json.dumps(data)
            print(json_data)
            self.sendMessage(json_data)
        else:
            print payload
            self.sendMessage(payload, isBinary)

    def onClose(self, wasClean, code, reason):
        print("WebSocket connection closed: {0}".format(reason))


if __name__ == '__main__':

    import sys

    from twisted.python import log
    from twisted.internet import reactor

    log.startLogging(sys.stdout)

    factory = WebSocketServerFactory(u"ws://127.0.0.1:9000")
    factory.protocol = MyServerProtocol
    # factory.setProtocolOptions(maxConnections=2)

    # note to self: if using putChild, the child must be bytes...

    reactor.listenTCP(9000, factory)
    reactor.run()
