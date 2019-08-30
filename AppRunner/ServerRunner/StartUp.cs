using System;
using System.Diagnostics;
using System.Net;
using System.Timers;

namespace ServerRunner
{
    /**
     * This app does the following things:
     *      Finds 2 available ports
     *      Executes a cmd command to run the Javache server.
     *      Creates a listener on one of the ports and waits for javache to connect.
     *          When javache connects, this app will close out.
     *          If javache does not connect, this app will close due to timeout.
     *      Prints the output of the Javache server on the console.
     */
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

            process.StandardInput.WriteLine(@"jre\bin\java.exe -cp "".;./bin/javache-api-1.2.7.jar;"" com.cyecize.StartUp " + appPort + " " + appLoadedListenerPort);

            process.StandardInput.Flush();
            process.StandardInput.Close();

            process.OutputDataReceived += (sender, eventArgs) => { Console.WriteLine(eventArgs.Data); };
            process.BeginOutputReadLine();

            InitAppTimeout();
            InitAppLoadedListener(appPort, appLoadedListenerPort);
        }

        private static void InitAppTimeout()
        {
            Timer timer = new Timer();
            timer.Interval = 15000;
            timer.Elapsed += (sender, eventArgs) =>
            {
                Console.WriteLine("App did not start properly!");
                Console.ReadKey();
                Environment.Exit(1);
            };

            timer.Start();
        }

        private static void InitAppLoadedListener(int appPort, int callbackPort)
        {
            HttpListener listener = new HttpListener();

            listener.Prefixes.Add($"http://localhost:{callbackPort}/database/connect/");

            listener.Start();

            HttpListenerContext context = listener.GetContext();
            HttpListenerRequest request = context.Request;

            HttpListenerResponse response = context.Response;
            response.Redirect($"http://localhost:{appPort}/database/connect?loadStoredCredentials");

            System.IO.Stream output = response.OutputStream;
            output.WriteByte(0);

            output.Close();
            listener.Stop();
            Environment.Exit(0);
        }
    }
}