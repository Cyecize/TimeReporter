using System.Diagnostics;

namespace ServerRunner.Util
{
    public class ConsoleProcess
    {

        private readonly string _command;

        private readonly bool _showWindow;

        private readonly Process _process;

        public ConsoleProcess(string command, bool showWindow)
        {
            this._command = command;
            this._showWindow = showWindow;
            this._process = new Process();
            this.InitProcess();
        }

        public void ExecuteCommand()
        {
            this._process.Start();
        }

        private void InitProcess()
        {
            ProcessStartInfo startInfo = new ProcessStartInfo
            {
                WindowStyle = this._showWindow ? ProcessWindowStyle.Normal : ProcessWindowStyle.Hidden,
                FileName = "cmd.exe",
                Arguments = $"/C {this._command}"
            };

            this._process.StartInfo = startInfo;
        }
    }
}
