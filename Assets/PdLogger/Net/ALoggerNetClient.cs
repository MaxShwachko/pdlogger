using PdLogger.Core;
using PdLogger.Net.Requests;
using PdNetwork.Client;
using PdNetwork.Engine;
using PdNetwork.Engine.Impl;
using PdNetwork.Exchange.Responses;
using UnityEngine;

namespace PdLogger.Net
{
	public abstract class ALoggerNetClient
	{
		private readonly string _authToken;
		private readonly string _apiVersion;
		private readonly string _host;
		private readonly int _port;

		public ALoggerNetClient(string authToken, string apiVersion, string host, int port)
		{
			_authToken = authToken;
			_apiVersion = apiVersion;
			_host = host;
			_port = port;
		}

		private IPdNetEngine _pdNetEngine;

		public bool IsConnected => _pdNetEngine.IsConnected;

		protected abstract ISocketClient CreateSocketClient();
		
		public void Connect(string source)
		{
			var tcpSocketClient = CreateSocketClient();
			_pdNetEngine = new PdNetEngine();
			_pdNetEngine.Configure(_authToken, _apiVersion, tcpSocketClient, int.MaxValue);
			_pdNetEngine.PingMonitor = new EmptyPingMonitor();
			
			var constantData = new ConstantDataBuilder(source).Build();
			
			_pdNetEngine.OnResponse += (resp) =>
			{
				if (resp is HandshakeResp)
				{
					Debug.Log("Send Constant data");
					var req = new ConstantDataReq(constantData);
					_pdNetEngine.Send(req);
				}
			};
			
			_pdNetEngine.Connect(_host, _port);
		}

		public void KillConnection()
		{
			_pdNetEngine.KillConnection();
		}
		
		public void Destroy()
		{
			_pdNetEngine.Disconnect();
			_pdNetEngine.Destroy();
		}
		
		public void Send(Message message)
		{
			var req = new LogReq(message);
			_pdNetEngine.Send(req);
		}
	}
}