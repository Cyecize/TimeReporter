using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServerRunner.Interfaces
{
    public interface ICommunicationManager
    {
        void Start();

        void CloseConnection();

        void SendMessage(string message, params string[] parameters);

        void SendInitMessage(params string[] parameters);

        void SendStopMessage(params string[] parameters);

        bool HasConnection();

        event Action<string> OnMessage;

        event Action<string> OnAppStarted;
    }
}
