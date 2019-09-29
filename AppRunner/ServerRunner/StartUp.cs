using System;
using System.IO;
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
    //    private static void InitAppTimeout()
    //    {
    //        Timer timer = new Timer();
    //        timer.Interval = 15000;
    //        timer.Elapsed += (sender, eventArgs) =>
    //        {
    //            Console.WriteLine("App did not start properly!");
    //            Console.ReadKey();
    //            Environment.Exit(1);
    //        };

    //        timer.Start();
    //    }

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

            communicationManager.OnAppStarted += msg => _appLoaded = true;
            communicationManager.Start();

            ConsoleProcess consoleProcess = new ConsoleProcess(commandParser.ParseCommand(), bool.Parse(configManager.GetConfig(LocalConfigKeys.ShowCmd)));
            consoleProcess.ExecuteCommand();

            while (!_appLoaded)
            {
                //TODO set timeout
            }

            OnAppLoaded(configManager, commandParser);
        }

        private static void OnAppLoaded(ILocalConfigManager configManager, ICommandParser commandParser)
        {
            CefSharpSettings.SubprocessExitIfParentProcessClosed = true;
            Cef.EnableHighDPISupport();

            CefSettings settings = new CefSettings()
            {
                CachePath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "CefSharp\\Cache")
            };

            Cef.Initialize(settings, performDependencyCheck: true, browserProcessHandler: null);

            //TODO: add utility class for this.
            BrowserForm form = new BrowserForm(commandParser.ParseBaseUrl())
            {
                Width = int.Parse(configManager.GetConfig(LocalConfigKeys.WindowWidth)),
                Height = int.Parse(configManager.GetConfig(LocalConfigKeys.WindowHeight))
            };

            Application.Run(form);
        }
    }
}