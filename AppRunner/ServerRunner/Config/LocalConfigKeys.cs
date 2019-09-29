using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServerRunner.Config
{
    public static class LocalConfigKeys
    {
        public const string InterpreterExePath = "interpreter_path";

        public const string Command = "command";

        public const string CommunicationPort = "communication_port";

        public const string AppPort = "app_port";

        public const string CommunicationAddress = "communication_address";

        public const string WindowWidth = "window_width";

        public const string WindowHeight = "window_height";

        public static readonly string[] All = { Command, InterpreterExePath, WindowHeight, WindowWidth, CommunicationPort, CommunicationAddress, AppPort };

        //TODO add UseCommunicationPort config in case this app is used for different applications.

        public static readonly Dictionary<string, string> Defaults = new Dictionary<string, string>
        {
            {InterpreterExePath, @"jre\bin\java.exe" },
            {Command, "" },
            {CommunicationPort, "4000" },
            {CommunicationAddress, "ws://127.0.0.1" },
            {WindowWidth, "800" },
            {WindowHeight, "600" },
            {AppPort, "8080" },
        };

    }
}
