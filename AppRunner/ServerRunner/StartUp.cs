using System;
using System.IO;
using System.Threading;
using System.Windows.Forms;
using CefSharp;
using CefSharp.WinForms;
using ServerRunner.Config;
using ServerRunner.Forms;
using ServerRunner.Interfaces;
using ServerRunner.Services;
using ServerRunner.Util;

namespace ServerRunner
{
    public class StartUp
    {
        private static bool _appLoaded;

        [STAThread]
        public static void Main(string[] args)
        {
            Application.EnableVisualStyles();

            ILocalConfigManager configManager = new LocalConfigManager();
            configManager.AdjustPortValues();

            ICommunicationManager communicationManager = new CommunicationManager(configManager);

            ICommandParser commandParser = new CommandParser(configManager);

            communicationManager.OnMessage += msg => Console.WriteLine($"Msg from app: {msg}");
            communicationManager.OnAppStarted += msg => _appLoaded = true;
            communicationManager.Start();

            ConsoleProcess consoleProcess = new ConsoleProcess(commandParser.ParseCommand(), bool.Parse(configManager.GetConfig(LocalConfigKeys.ShowCmd)));
            consoleProcess.ExecuteCommand();

            long connectionTimeout = long.Parse(configManager.GetConfig(LocalConfigKeys.AppStartTimeout));
            long connectionElapsedTime = 0L;

            while (!_appLoaded)
            {
                connectionElapsedTime += 1;
                Thread.Sleep(1);

                if (connectionElapsedTime > connectionTimeout)
                {
                    MessageBox.Show("App was not started", "Error");
                    consoleProcess.Kill();
                    return;
                }
            }

            InitCef();
            //TODO: add utility class for this.
            //TODO on form close, save sizes.
            BrowserForm form = new BrowserForm(commandParser.ParseBaseUrl())
            {
                Width = int.Parse(configManager.GetConfig(LocalConfigKeys.WindowWidth)),
                Height = int.Parse(configManager.GetConfig(LocalConfigKeys.WindowHeight))
            };

            form.SizeChanged += (sender, eventArgs) => { Console.WriteLine("Size has changed"); };

            Application.Run(form);
        }

        private static void InitCef()
        {
            CefSharpSettings.SubprocessExitIfParentProcessClosed = true;
            Cef.EnableHighDPISupport();

            CefSettings settings = new CefSettings()
            {
                CachePath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "CefSharp\\Cache")
            };

            Cef.Initialize(settings, performDependencyCheck: true, browserProcessHandler: null);
        }
    }
}