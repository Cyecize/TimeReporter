using System.Collections.Generic;

namespace ServerRunner.Config
{
    public static class LocalConfigKeys
    {
        public const string InterpreterExePath = "interpreter_path";

        public const string Command = "command";

        public const string CommunicationPort = "communication_port";

        public const string AppPort = "app_port";

        public const string AppStartingUrl = "app_start_url";

        public const string AppStartTimeout = "app_start_timeout";

        public const string CommunicationAddress = "communication_address";

        public const string WindowWidth = "window_width";

        public const string WindowHeight = "window_height";

        public const string WindowMaximizedState = "window_maximized_state";

        public const string ShowCmd = "show_cmd";

        public static readonly string[] All =
        {
            Command,
            InterpreterExePath,
            WindowHeight,
            WindowWidth,
            CommunicationPort,
            CommunicationAddress,
            AppPort,
            ShowCmd,
            AppStartingUrl,
            AppStartTimeout,
            WindowMaximizedState
        };

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
            {ShowCmd, "false" },
            {AppStartingUrl, "http://localhost:{config:app_port}" },
            {AppStartTimeout, "10000" },
            {WindowMaximizedState, "false" },
        };

    }
}
