using ByteFormatter.Runtime;
using PdLogger.Core;
using PdNetwork.Exchange;

namespace PdLogger.Net.Requests
{
    public readonly struct ConstantDataReq : IRequest
    {
        private readonly ConstantData _constantData;

        public ConstantDataReq(ConstantData constantData)
        {
            _constantData = constantData;
        }
        
        public byte GetHeader() => 3;

        public void WriteBody(ByteWriter writer)
        {
            writer.Write(_constantData.Source);
            writer.Write(_constantData.Os);
            writer.Write(_constantData.OsVersion);
            writer.Write(_constantData.Device);
            writer.Write(_constantData.DeviceUid);
            writer.Write(_constantData.Resolution);
            writer.Write(_constantData.AppVersion);
            writer.Write(_constantData.AppId);
            writer.Write(_constantData.AppName);
            writer.Write(_constantData.Cpu);
            writer.Write(_constantData.Opengl);
            writer.Write(_constantData.MemorySize);
            writer.Write(_constantData.GpuMemorySize);
            writer.Write(_constantData.ScreenOrientation);
        }
    }
}