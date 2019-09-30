using System;
using System.Diagnostics;
using System.Management;

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

        public void Kill()
        {
            KillProcessAndChildren(this._process.Id);
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

        private static void KillProcessAndChildren(int pid)
        {
            ManagementObjectSearcher processSearcher = new ManagementObjectSearcher
                ("Select * From Win32_Process Where ParentProcessID=" + pid);

            ManagementObjectCollection processCollection = processSearcher.Get();

            // We must kill child processes first!
            if (processCollection != null)
            {
                foreach (ManagementObject mo in processCollection)
                {
                    KillProcessAndChildren(Convert.ToInt32(mo["ProcessID"])); //kill child processes(also kills childrens of childrens etc.)
                }
            }

            // Then kill parents.
            try
            {
                Process proc = Process.GetProcessById(pid);
                if (!proc.HasExited) proc.Kill();
            }
            catch (ArgumentException)
            {
                // Process already exited.
            }
        }
    }
}
