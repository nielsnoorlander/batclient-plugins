package biz.noorlander.batclient.model;

public class CustomConfig extends AbstractConfig {
    public enum Name { PLAYER_STATS }
    public CustomConfig(CustomConfig.Name configName, String baseDir) {
        super(configName.name(), baseDir);
    }

    @Override
    public String getPrefix() {
        return "custom";
    }
}
