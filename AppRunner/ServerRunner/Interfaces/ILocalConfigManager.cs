using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServerRunner.Interfaces
{
    public interface ILocalConfigManager
    {
        void Save();

        void AdjustPortValues();

        void SetConfig(string configName, string configValue);

        void SetTempConfig(string configName, string configValue);

        string GetConfig(string configName);
    }
}
