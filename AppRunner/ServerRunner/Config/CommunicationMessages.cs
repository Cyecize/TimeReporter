using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServerRunner.Config
{
    public static class CommunicationMessages
    {
        public const string AppStartedMsg = "app_started";

        public const string TerminateAppMsg = "app_stop";

        public const string AppInitializeMsg = "app_initialize";
    }
}
