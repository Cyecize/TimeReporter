﻿using System.Text.RegularExpressions;
using ServerRunner.Config;
using ServerRunner.Interfaces;
using ServerRunner.Util;

namespace ServerRunner.Services
{
    public class CommandParser : ICommandParser
    {

        private readonly ILocalConfigManager _configManager;

        public CommandParser(ILocalConfigManager localConfig)
        {
            this._configManager = localConfig;
        }

        public string ParseCommand()
        {
            string command = this._configManager.GetConfig(LocalConfigKeys.Command);

            this.HandlePortFormatting(ref command);
            this.HandleConfigFormatting(ref command);

            return $"{this._configManager.GetConfig(LocalConfigKeys.InterpreterExePath)} {command}";
        }

        public string ParseBaseUrl()
        {
            string baseUrl = this._configManager.GetConfig(LocalConfigKeys.AppStartingUrl);

            this.HandlePortFormatting(ref baseUrl);
            this.HandleConfigFormatting(ref baseUrl);

            return baseUrl;
        }

        private void HandlePortFormatting(ref string command)
        {
            Regex regex = new Regex(@"{port:(?<port>[0-9]{1,})}");
            if (!regex.IsMatch(command)) return;

            Match match = regex.Match(command);

            while (match.Success)
            {
                int port = int.Parse(match.Groups["port"].Value);
                command = regex.Replace(command, FreePortFinder.FindFreePort(port).ToString(), 1);

                match = regex.Match(command);
            }
        }

        private void HandleConfigFormatting(ref string command)
        {
            Regex regex = new Regex(@"{config:(?<keyName>\w{1,})}");
            if (!regex.IsMatch(command)) return;

            Match match = regex.Match(command);

            while (match.Success)
            {
                string configVal = match.Groups["keyName"].Value;
                command = regex.Replace(command, this._configManager.GetConfig(configVal), 1);

                match = regex.Match(command);
            }
        }
    }
}
