using System.Collections.Generic;

namespace PdLogger.Core
{
    public struct Message
    {
        public string ShortMessage;

        public long Timestamp;
        public byte LogLevel;
        
        public float Battery;
        public bool Online;
        
        //custom additional fields
        public Dictionary<string, string> AdditionalFields;
    }
}
