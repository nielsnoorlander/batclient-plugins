package biz.noorlander.batclient.model;

import java.io.File;

import com.mythicscape.batclient.interfaces.BatClientPlugin;

public abstract class AbstractConfig {
	private static final String POSTFIX_CONFIG = ".config";

	private String pluginName;
	private transient String baseDir;
	
	public <T extends BatClientPlugin> AbstractConfig(T plugin) {
		this.pluginName = plugin.getName();
		this.baseDir = plugin.getBaseDirectory();
	}
	
	public abstract String getPrefix();
	
	public File getConfigFile() {
		File configFile = new File(new File(this.baseDir, "conf").getPath(), getPrefix() + this.pluginName + POSTFIX_CONFIG);
		return configFile;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
}
