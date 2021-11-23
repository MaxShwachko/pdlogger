using PdNetwork.Exchange;
using PdNetwork.Exchange.Responses;

namespace PdLogger.Net
{
	public class ExchangeManager : AExchangeManager
	{
		public override void Initialize()
		{
			RegisterResponseReader((byte) Header.Handshake, new HandshakeResp.Reader());
			RegisterResponseReader((byte) Header.PingPong, new PingPongResp.Reader());
		}
	}
}