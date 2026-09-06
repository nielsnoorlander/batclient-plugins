package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.model.WindowsConfig;
import biz.noorlander.batclient.utils.ConfigService;
import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ClientGUI;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractWindowedHandler extends AbstractHandler {

    private Map<String, BatWindow> clientWindows;
    private String basePath;
    private Map<String, WindowsConfig> windowsConfigs;
    private static boolean fixedLookAndFeel = false;

    AbstractWindowedHandler(ClientGUI gui, String pluginLabel) {
        super(gui, pluginLabel);
        basePath = gui.getBaseDirectory();
        windowsConfigs = new HashMap<>();
        clientWindows = new HashMap<>();
        fixLookAndFeel();
    }

    private void fixLookAndFeel() {
        if (!fixedLookAndFeel) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                fixedLookAndFeel = true;
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }

    void createWindow(String handle, String tabName, JComponent panel, String configName) {
        WindowsConfig windowsConfig = ConfigService.getInstance().loadWindowsConfig(configName, basePath);
        BatWindow clientWin = this.createWindow( handle, windowsConfig);
        clientWin.removeTabAt( 0 );
        clientWin.newTab( tabName, panel );
        clientWin.setVisible( true );
        windowsConfigs.put(configName, windowsConfig);
        clientWindows.put(configName, clientWin);
    }

    void saveWindowsConfig() {
        windowsConfigs.forEach((configName, windowsConfig) -> {
            BatWindow clientWin = clientWindows.get(configName);
            windowsConfig.setLeft(clientWin.getLocation().x);
            windowsConfig.setTop(clientWin.getLocation().y);
            windowsConfig.setWidth(clientWin.getSize().width);
            windowsConfig.setHeight(clientWin.getSize().height);
            windowsConfig.setVisible(clientWin.isVisible());
            ConfigService.getInstance().saveConfig(windowsConfig);
        });
    }


}
