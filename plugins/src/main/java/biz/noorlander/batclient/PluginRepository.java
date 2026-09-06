package biz.noorlander.batclient;

import java.util.*;

public class PluginRepository {
    private Set<BatmudPlugin<?>> plugins = new HashSet<>();
    private PluginRepository() {}

    private static class SingletonHelper {
        private static final PluginRepository INSTANCE = new PluginRepository();
    }

    public static PluginRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void registerPlugin(BatmudPlugin<?> plugin) {
        plugins.add(plugin);
        System.out.println("Registering plugin " + plugins.size() + ": " + plugin.getName());
    }

    public Set<BatmudPlugin<?>> list() {
        return Collections.unmodifiableSet(plugins);
    }
}
