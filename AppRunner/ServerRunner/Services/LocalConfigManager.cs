using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Newtonsoft.Json;
using ServerRunner.Config;
using ServerRunner.Interfaces;
using ServerRunner.Util;

namespace ServerRunner.Services
{
    public class LocalConfigManager : ILocalConfigManager
    {
        private const string ConfigFileName = "runner_conf.json";

        private const string EmptyJson = "{}";

        private readonly Dictionary<string, string> _tempConfig;

        private Dictionary<string, string> _config;

        public LocalConfigManager()
        {
            this._tempConfig = new Dictionary<string, string>();
            this.CreateConfigFile();
            this.ParseConfigFile();
            this.ValidateConfigFields();
        }

        public void Save()
        {
            this.WriteToFile(JsonConvert.SerializeObject(this._config));
        }

        public void AdjustPortValues()
        {
            this.SetTempConfig(LocalConfigKeys.AppPort, FreePortFinder.FindFreePort(int.Parse(this.GetConfig(LocalConfigKeys.AppPort))) + "");
            this.SetTempConfig(LocalConfigKeys.CommunicationPort, FreePortFinder.FindFreePort(int.Parse(this.GetConfig(LocalConfigKeys.CommunicationPort))) + "");
        }

        public void SetConfig(string configName, string configValue)
        {
            this._config[configName] = configValue;
        }

        public void SetTempConfig(string configName, string configValue)
        {
            this._tempConfig[configName] = configValue;
        }

        public string GetConfig(string configName)
        {
            if (this._tempConfig.ContainsKey(configName)) return this._tempConfig[configName];
            if (!this._config.ContainsKey(configName)) return null;

            return this._config[configName];
        }

        private void CreateConfigFile()
        {
            if (File.Exists(ConfigFileName)) return;

            using (FileStream file = File.Create(ConfigFileName))
            {
            }

            this.WriteToFile(EmptyJson);
        }

        private void ParseConfigFile()
        {
            try
            {
                this._config = JsonConvert.DeserializeObject<Dictionary<string, string>>(this.ReadFile()) ?? new Dictionary<string, string>();
            }
            catch (Exception)
            {
                this._config = new Dictionary<string, string>();
                this.WriteToFile(EmptyJson);
            }
        }

        private void ValidateConfigFields()
        {
            ICollection<string> keys = this._config.Keys;

            foreach (var keyName in LocalConfigKeys.All)
            {
                if (!keys.Contains(keyName)) this.SetConfig(keyName, LocalConfigKeys.Defaults[keyName]);
            }

            this.Save();
        }

        private string ReadFile()
        {
            return File.ReadAllText(ConfigFileName);
        }

        private void WriteToFile(string content)
        {
            File.WriteAllText(ConfigFileName, content);
        }
    }
}
