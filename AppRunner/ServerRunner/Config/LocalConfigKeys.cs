using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServerRunner.Config
{
    public static class LocalConfigKeys
    {
        public const string JavaExePath = "java_exe_path";

        public const string JavaCommand = "java_command";

        public const string WindowWidth = "window_width";

        public const string WindowHeight = "window_height";

        public static readonly string[] All = { JavaCommand, JavaExePath, WindowHeight, WindowWidth };

        public static readonly Dictionary<string, string> Defaults = new Dictionary<string, string>
        {
            {JavaExePath, @"jre\bin\java.exe" },
            {JavaCommand, "" },
            {WindowWidth, "800" },
            {WindowHeight, "600" },
        };

    }
}
