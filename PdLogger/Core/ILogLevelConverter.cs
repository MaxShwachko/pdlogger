using UnityEngine;

namespace PdLogger.Core
{
    public interface ILogLevelConverter
    {
        byte ConvertTo(LogType logType);
    }
}