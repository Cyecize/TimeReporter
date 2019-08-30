using System;
using System.Diagnostics;
using System.Net;
using System.Timers;

namespace ServerRunner
{
    class StartUp
    {
        static void Main(string[] args)
        {
            const int initialPort = 8000;
            const int initialAppLoadedListenerPort = 4000;

            Console.WriteLine("\n\nStarting Time Reporter\n\n");
            Process process = new Process();

            process.StartInfo.FileName = "cmd.exe";
            process.StartInfo.CreateNoWindow = true;
            process.StartInfo.RedirectStandardInput = true;
            process.StartInfo.RedirectStandardOutput = true;
            process.StartInfo.UseShellExecute = false;

            process.Start();

            int appPort = FreePortFinder.FindFreePort(initialPort);
            int appLoadedListenerPort = FreePortFinder.FindFreePort(initialAppLoadedListenerPort);

            process.StandardInput.WriteLine(@"jre\bin\java.exe -cp "".;./bin/javache-api-1.2.6.jar;"" com.cyecize.StartUp " + appPort + " " + appLoadedListenerPort);

            process.StandardInput.Flush();
            process.StandardInput.Close();

            process.OutputDataReceived += (sender, eventArgs) => { Console.WriteLine(eventArgs.Data); };
            process.BeginOutputReadLine();

            InitAppTimeout();
            InitAppLoadedListener(new string[] { $"http://localhost:{appLoadedListenerPort}/" });
        }

        private static void InitAppTimeout()
        {
            Timer timer = new Timer();
            timer.Interval = 10000;
            timer.Elapsed += (sender, eventArgs) =>
            {
                Console.WriteLine("App did not start properly!");
                Environment.Exit(1);
            };

            timer.Start();
        }

        private static void InitAppLoadedListener(string[] prefixes)
        {
            HttpListener listener = new HttpListener();

            foreach (string s in prefixes)
            {
                listener.Prefixes.Add(s);
            }

            listener.Start();

            HttpListenerContext context = listener.GetContext();
            HttpListenerRequest request = context.Request;

            HttpListenerResponse response = context.Response;
            response.Redirect("https://www.google.com");

            System.IO.Stream output = response.OutputStream;
            output.WriteByte(0);

            output.Close();
            listener.Stop();
            Environment.Exit(0);
        }
    }
}