using PdNetwork.Client;
using PdNetwork.Client.Impl;
using PdNetwork.Sockets.Impl;

namespace PdLogger.Net.Impl
{
    public class TcpLoggerNetClient : ALoggerNetClient
    {
        public TcpLoggerNetClient(string authToken, string apiVersion, string host, int port) 
            : base(authToken, apiVersion, host, port)
        {
        }

        protected override ISocketClient CreateSocketClient()
        {
            var exchangeHandler = new ExchangeManager();
            exchangeHandler.Initialize();
			
            var tcpSocket = new TcpSocketLayer();
            var tcpSocketClient = new SocketClient(tcpSocket, exchangeHandler);
            return tcpSocketClient;
        }
    }
}