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
using CefSharp.WinForms.Internals;

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

            this._browser.IsBrowserInitializedChanged += this.OnIsBrowserInitializedChanged;

            this.Controls.Add(this._browser);
        }

        private void OnIsBrowserInitializedChanged(object sender, EventArgs e)
        {
            this._browser.Show();
            this.LblLoadingApp.Hide();
        }
    }
}
