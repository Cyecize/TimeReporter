using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Newtonsoft.Json;
using ServerRunner.Config;
using ServerRunner.Interfaces;

namespace ServerRunner.Services
{
    public class LocalConfigManager : ILocalConfigManager
    {
        private const string ConfigFileName = "runner_conf.json";

        private const string EmptyJson = "{}";

        private Dictionary<string, string> _config;

        public LocalConfigManager()
        {
            this.CreateConfigFile();
            this.ParseConfigFile();
            this.ValidateConfigFields();
        }

        public void Save()
        {
            this.WriteToFile(JsonConvert.SerializeObject(this._config));
        }

        public void SetConfig(string configName, string configValue)
        {
            this._config[configName] = configValue;
        }

        public string GetConfig(string configName)
        {
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
