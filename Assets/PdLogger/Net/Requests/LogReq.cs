using ByteFormatter.Runtime;
using PdLogger.Core;
using PdNetwork.Exchange;

namespace PdLogger.Net.Requests
{
    public readonly struct LogReq : IRequest
    {
        private readonly Message _message;

        public LogReq(Message message)
        {
            _message = message;
        }

        public byte GetHeader() => 4;

        public void WriteBody(ByteWriter writer)
        {
            writer.Write(_message.ShortMessage);
            writer.Write(_message.Timestamp);
            writer.Write(_message.LogLevel);
            
            writer.Write(_message.Battery);
            writer.Write(_message.Online);

            writer.Write((byte)_message.AdditionalFields.Count);
            foreach (var additionalField in _message.AdditionalFields)
            {
                writer.Write(additionalField.Key);
                writer.Write(additionalField.Value);
            }
        }
    }
}