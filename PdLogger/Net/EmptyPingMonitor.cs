using PdNetwork.LagMonitor;

namespace PdLogger.Net
{
    public class EmptyPingMonitor : IPingMonitor
    {
        public void Start() { }
        public void Stop() { }
        public void Destroy() { }
        public bool IsRunning => false;
        public long AverageRoundPing => -1;
    }
}