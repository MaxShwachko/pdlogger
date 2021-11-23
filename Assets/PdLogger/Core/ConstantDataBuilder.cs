using UnityEngine;

namespace PdLogger.Core
{
    public readonly struct ConstantDataBuilder
    {
        private readonly string _source;

        public ConstantDataBuilder(string source) : this()
        {
            _source = source;
        }

        public ConstantData Build()
        {
            return new ConstantData
            {
                Source = _source,
                Os = Application.platform.ToString().ToLower() == "iphoneplayer"
                    ? "ios"
                    : Application.platform.ToString().ToLower(),
                OsVersion = SystemInfo.operatingSystem,
                Device = SystemInfo.deviceName,
                DeviceUid = SystemInfo.deviceUniqueIdentifier,
                Resolution = Screen.currentResolution.ToString(),
                AppVersion = Application.version,
                AppId = Application.identifier,
                AppName = Application.productName,
                Cpu = Application.productName,
                Opengl = SystemInfo.graphicsDeviceVersion,
                MemorySize = (short)SystemInfo.systemMemorySize,
                GpuMemorySize = (short)SystemInfo.graphicsMemorySize,
                ScreenOrientation = (byte)Screen.orientation,
            };
        }
    }
}