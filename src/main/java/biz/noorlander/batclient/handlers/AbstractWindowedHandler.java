package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.model.WindowsConfig;
import biz.noorlander.batclient.utils.ConfigService;
import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ClientGUI;

import javax.swing.*;

abstract class AbstractWindowedHandler extends AbstractHandler {

    private BatWindow clientWin;
    private String basePath;
    private WindowsConfig windowsConfig;

    AbstractWindowedHandler(ClientGUI gui, String pluginLabel) {
        super(gui, pluginLabel);
        this.basePath = gui.getBaseDirectory();
    }

    public void createWindow(String handle, String tabName, JComponent panel, String configName) {
        this.windowsConfig = ConfigService.getInstance().loadWindowsConfig(configName, basePath);
        this.clientWin = this.createWindow( handle, windowsConfig);
        this.clientWin.removeTabAt( 0 );
        this.clientWin.newTab( tabName, panel );
        this.clientWin.setVisible( true );
    }

    public void saveWindowsConfig() {
        this.windowsConfig.setLeft(clientWin.getLocation().x);
        this.windowsConfig.setTop(clientWin.getLocation().y);
        this.windowsConfig.setWidth(clientWin.getSize().width);
        this.windowsConfig.setHeight(clientWin.getSize().height);
        this.windowsConfig.setVisible(clientWin.isVisible());
        ConfigService.getInstance().saveConfig(this.windowsConfig);
    }


}
