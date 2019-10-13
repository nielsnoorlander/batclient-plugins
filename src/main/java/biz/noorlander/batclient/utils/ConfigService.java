package biz.noorlander.batclient.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import biz.noorlander.batclient.model.AbstractConfig;
import biz.noorlander.batclient.model.CustomConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.mythicscape.batclient.interfaces.BatClientPlugin;

import biz.noorlander.batclient.model.WindowsConfig;

public class ConfigService {
	private Gson gson;
	private ConfigService() {
		this.gson = new GsonBuilder()
	             .disableHtmlEscaping()
//	             .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
	             .setPrettyPrinting()
	             .serializeNulls()
	             .create();
	}
	private static class ConfigServiceHelper {
		private static final ConfigService INSTANCE = new ConfigService();
	}
	public static ConfigService getInstance() {
		return ConfigServiceHelper.INSTANCE;
	}
	public WindowsConfig loadWindowsConfig(String name, String baseDirectory) {
		System.out.println("Loading windows config '" + name + "' from " + baseDirectory);
		WindowsConfig config = new WindowsConfig(name, baseDirectory);
		System.out.println("Try to read windows config from: " + config.getConfigFile().getAbsolutePath());
		if (config.getConfigFile().exists()) {
			try {
				WindowsConfig windowsConfig = gson.fromJson(new FileReader(config.getConfigFile()), WindowsConfig.class);
				windowsConfig.setBaseDirectory(baseDirectory);
				windowsConfig.setConfigName(name);
				System.out.println("Set windows config baseDir to " + windowsConfig.getConfigFile().getParent());
				return windowsConfig;
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				System.err.println("Unable to read windows config from: " + config.getConfigFile().getPath());
				e.printStackTrace();
				return config;
			}
		} else {
			return config;
		}
	}

	public <T extends CustomConfig> T loadCustomConfig(T config) {
		try {
			T t = gson.fromJson(new FileReader(config.getConfigFile()), (Type) config.getClass());
			t.setConfigName(config.getConfigName());
			t.setBaseDirectory(config.getConfigFile().getParent());
			System.out.println("Set custom config baseDir to " + config.getConfigFile().getParent());
			return t;
		} catch (FileNotFoundException e) {
			System.err.println("Unable to read custom config from: " + config.getConfigFile().getPath());
			e.printStackTrace();
			return config;
		}
	}

	public <T extends AbstractConfig> void saveConfig(T config) {
		File configFile = config.getConfigFile();
		System.out.println("Saving config '" + config.getConfigName() + "' at " + config.getConfigFile().getPath());

		FileWriter fileWriter = null;
		try {
			if (configFile.createNewFile()) {
				System.out.println("Created new configFile: " + configFile.getPath());
			}
			fileWriter = new FileWriter(configFile);
			gson.toJson(config, fileWriter);
		} catch (IOException e) {
			System.err.println("Unable to create configFile: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.flush();
					fileWriter.close();
				} catch (IOException e) {
					System.err.println("Unable to flush/close configFile: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}
