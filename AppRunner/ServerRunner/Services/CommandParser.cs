using System.Text.RegularExpressions;
using ServerRunner.Interfaces;
using ServerRunner.Util;

namespace ServerRunner.Services
{
    public class CommandParser : ICommandParser
    {
        public string ParseCommand(string command)
        {
            this.HandlePortFormatting(ref command);
            //TODO: you can add more handlers.

            return command;
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
    }
}
