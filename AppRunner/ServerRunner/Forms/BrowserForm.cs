using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using CefSharp.WinForms;

namespace ServerRunner.Forms
{
    public partial class BrowserForm : Form
    {

        private readonly ChromiumWebBrowser _browser;

        public BrowserForm(string startupUrl)
        {
            InitializeComponent();

            this._browser = new ChromiumWebBrowser(startupUrl)
            {
                Dock = DockStyle.Fill,
            };

            this.Controls.Add(this._browser);
        }
    }
}
