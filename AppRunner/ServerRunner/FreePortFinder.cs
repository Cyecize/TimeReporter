using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace ServerRunner
{
    static class FreePortFinder
    {
        public static int FindFreePort(int startingPort)
        {
            for (int port = startingPort; port < startingPort + 100; port++)
            {
                if (IsPortFree(port))
                {
                    return port;
                }
            }

            throw new Exception($"Port {startingPort} is taken and none of the next following 100 are free!");
        }

        private static bool IsPortFree(int port)
        {
            IPAddress ipAddress = Dns.GetHostEntry("localhost").AddressList[0];
            try
            {
                TcpListener tcpListener = new TcpListener(ipAddress, port);
                tcpListener.Start();
                tcpListener.Stop();
                return true;
            }
            catch (SocketException ex)
            {
                return false;
            }
        }
    }
}
