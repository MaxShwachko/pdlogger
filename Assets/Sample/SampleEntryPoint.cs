using System;
using PdLogger.Core;
using UnityEngine;
using Logger = PdLogger.Impl.Logger;

namespace Sample
{
    public class SampleEntryPoint : MonoBehaviour
    {
        public Logger logger;

        private int _counter = 1;
        
        private void Awake()
        {
            logger.SetLogLevelConverter(new GelfLogLevelConverter());
        }
        
        private void OnGUI()
        {
            if(GUI.Button(new Rect(Screen.width * 0.5f - 75, Screen.height * 0.5f + 100, 150f, 50f), "Send log"))
            {
                Debug.Log("[SampleEntryPoint] Hello, logger, Message " + _counter + " !!");
                _counter++;
            }
            
            if(GUI.Button(new Rect(Screen.width * 0.5f - 75, Screen.height * 0.5f, 150f, 50f), "Kill connection"))
            {
                logger.ALoggerNetClient.KillConnection();
            }
        }

        private class GelfLogLevelConverter : ILogLevelConverter
        {
            public byte ConvertTo(LogType logType)
            {
                switch (logType)
                {
                    case LogType.Error:
                        return 3;
                    case LogType.Assert:
                        return 7;
                    case LogType.Warning:
                        return 4;
                    case LogType.Log:
                        return 6;
                    case LogType.Exception:
                        return 2;
                    default:
                        throw new ArgumentOutOfRangeException(nameof(logType), logType, null);
                }
            }
        }
        
        // public enum GelfLevel : byte
        // {
        //     Emergency = 0,
        //     Alert = 1,
        //     Critical = 2,
        //     Error = 3,
        //     Warning = 4,
        //     Notice = 5,
        //     Informational = 6,
        //     Debug = 7
        // }
    }
}