package biz.noorlander.batclient.model;

import java.io.File;

import com.mythicscape.batclient.interfaces.BatClientPlugin;

public abstract class AbstractConfig {
	private static final String POSTFIX_CONFIG = ".config";

	private String configName;
	private transient String baseDir;
	
	public AbstractConfig(String configName, String baseDir) {
		this.configName = configName;
		this.baseDir =baseDir;
	}
	
	public abstract String getPrefix();
	
	public File getConfigFile() {
		File configFile = new File(new File(this.baseDir, "conf").getPath(), getPrefix() + this.configName + POSTFIX_CONFIG);
		return configFile;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName =configName;
	}
}
