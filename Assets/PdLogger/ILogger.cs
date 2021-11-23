using PdLogger.Core;

namespace PdLogger
{
    public interface ILogger
    {
        ILogger SetLogLevelConverter(ILogLevelConverter logLevelConverter);
        ILogger AddDeniedType<T>();
        ILogger AddCustomName(string customName);
        ILogger AddNamespace(string @namespace);
    }
}