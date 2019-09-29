using System;
using Fleck;

namespace ServerRunner
{
    public class CommunicationServer
    {
        private readonly WebSocketServer _server;

        private readonly Action<IWebSocketConnection> _onClientConnectedAction;

        public CommunicationServer(string address, int port, Action<IWebSocketConnection> onClientConnected)
        {
            this._server = new WebSocketServer($"{address}:{port}");
            this._onClientConnectedAction = onClientConnected;
        }

        public void Start()
        {
            this._server.Start(socket =>
            {
                socket.OnOpen = () => Console.WriteLine($"Established connection {socket.ConnectionInfo.Id}");
                socket.OnClose = () => Console.WriteLine($"Closed connection {socket.ConnectionInfo.Id}");
                this._onClientConnectedAction?.Invoke(socket);
            });
        }

        public void Stop()
        {
            this._server.Dispose();
        }
    }
}
