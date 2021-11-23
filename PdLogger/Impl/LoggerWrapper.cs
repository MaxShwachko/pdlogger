using PdLogger.Core;
using UnityEngine;

namespace PdLogger.Impl
{
    public class LoggerWrapper : MonoBehaviour, ILogger
    {
        public ILogger SetLogLevelConverter(ILogLevelConverter logLevelConverter)
        {
            return this;
        }

        public ILogger AddDeniedType<T>()
        {
            return this;
        }

        public ILogger AddCustomName(string customName)
        {
            return this;
        }

        public ILogger AddNamespace(string @namespace)
        {
            return this;
        }
    }
}