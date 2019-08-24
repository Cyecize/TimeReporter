package com.cyecize.reporter.display.models;

import com.cyecize.reporter.config.AppConstants;
import com.cyecize.reporter.conn.services.DbConnectionStorageService;
import com.cyecize.reporter.display.Constants;
import com.cyecize.summer.SummerBootApplication;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefAppHandlerAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private CefBrowser browser;

    private WindowPreferences preferences;

    public MainFrame() {

    }

    public void initBrowser(String startURL, boolean useOSR) {
        if (!CefApp.startup()) {
            System.out.println("Startup initialization failed!");
            return;
        }

        this.initEvents();
        this.initEmbeddedBrowser(startURL, useOSR);
        this.initWindow();
    }

    public void setUserPreferences(WindowPreferences windowPreferences) {
        this.preferences = windowPreferences;
        this.initWindowPreferences();
    }

    private void initEvents() {
        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
            @Override
            public void stateHasChanged(CefApp.CefAppState state) {
                if (state == CefApp.CefAppState.TERMINATED) {
                     SummerBootApplication.dependencyContainer.getObject(DbConnectionStorageService.class).closeAllConnections();
                    System.exit(0);
                }
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                dispose();
            }
        });
    }

    private void initEmbeddedBrowser(String startUrl, boolean useOSR) {
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = useOSR;
        settings.cache_path = AppConstants.JCEF_CACHE_DIR;

        final CefApp cefApp = CefApp.getInstance(settings);
        final CefClient cefClient = cefApp.createClient();

        this.browser = cefClient.createBrowser(startUrl, useOSR, false);
        this.browser.reloadIgnoreCache();
    }

    private void initWindow() {
        Component browserUI = this.browser.getUIComponent();
        this.getContentPane().add(browserUI, BorderLayout.CENTER);

        Image image = Toolkit.getDefaultToolkit().getImage(Constants.FAVICON_RESOURCE_LOCATION);
        this.setIconImage(image);
        this.setTitle(Constants.WINDOW_TITLE);

        this.pack();
        if (this.preferences != null) {
            this.initWindowPreferences();
        }

        this.setMinimumSize(new Dimension(Constants.WINDOW_MIN_WIDTH, Constants.WINDOW_MIN_HEIGHT));
        this.setLocationRelativeTo(null);

        this.setVisible(true);
    }

    private void initWindowPreferences() {
        this.setSize(this.preferences.getWindowWidth(), this.preferences.getWindowHeight());
        this.setExtendedState(this.preferences.getExtendedState());
    }
}
