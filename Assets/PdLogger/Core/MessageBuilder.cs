using System;
using System.Collections.Generic;
using UnityEngine;

namespace PdLogger.Core
{
    public readonly struct MessageBuilder
    {
        private readonly string _message;
        private readonly byte _logLevel;
        private readonly Dictionary<string, string> _additionalFields;

        public MessageBuilder(string message, byte logLevel) : this()
        {
            _message = message;
            _logLevel = logLevel;
            _additionalFields = new Dictionary<string, string>();
        }
        
        public MessageBuilder AddAdditionalField(string key, string value)
        {
            _additionalFields.Add(key, value);
            return this;
        }
        
        public Message ToMessage()
        {
            return new Message
            {
                ShortMessage = _message,
                LogLevel = _logLevel,
                Timestamp = DateTimeOffset.UtcNow.ToUnixTimeSeconds(),

                Battery = SystemInfo.batteryLevel,
                Online = Application.internetReachability > 0,
                
                AdditionalFields = _additionalFields,
            };
        }
    }
}