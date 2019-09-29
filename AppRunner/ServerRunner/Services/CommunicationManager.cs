using System;
using Fleck;
using ServerRunner.Config;
using ServerRunner.Interfaces;

namespace ServerRunner.Services
{
    public class CommunicationManager : ICommunicationManager
    {

        private readonly ILocalConfigManager _configManager;

        private readonly CommunicationServer _communicationServer;

        private IWebSocketConnection _clientConnection;

        public CommunicationManager(ILocalConfigManager configManager)
        {
            this._configManager = configManager;
            this._communicationServer = new CommunicationServer(this._configManager.GetConfig(LocalConfigKeys.CommunicationAddress), this.GetOpenPort(), this.OnApplicationConnection);
        }

        public void Start()
        {
            this._communicationServer.Start();
        }

        public void CloseConnection()
        {
            this._communicationServer.Stop();
        }

        public void SendMessage(string message, params string[] parameters)
        {
            this._clientConnection?.Send($"{message} {string.Join(" ", parameters)}");
        }

        public void SendInitMessage(params string[] parameters)
        {
            this.SendMessage(CommunicationMessages.AppInitializeMsg, parameters);
        }

        public void SendStopMessage(params string[] parameters)
        {
            this.SendMessage(CommunicationMessages.TerminateAppMsg, parameters);
        }

        public bool HasConnection()
        {
            return this._clientConnection != null && this._clientConnection.IsAvailable;
        }

        public event Action<string> OnMessage;

        public event Action<string> OnAppStarted;

        private int GetOpenPort()
        {
            return int.Parse(this._configManager.GetConfig(LocalConfigKeys.CommunicationPort));
        }

        private void OnApplicationConnection(IWebSocketConnection client)
        {
            this._clientConnection = client;
            this._clientConnection.OnMessage += this.OnClientMessage;
        }

        private void OnClientMessage(string msg)
        {
            this.OnMessage?.Invoke(msg);
            if (msg == CommunicationMessages.AppStartedMsg)
            {
                this.OnAppStarted?.Invoke(msg);
            }
        }
    }
}
