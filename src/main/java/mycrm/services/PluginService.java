package mycrm.services;

import mycrm.models.Plugin;

import java.util.List;

public interface PluginService {
    Plugin getPluginByApi(String name);

    List<Plugin> findAll();

    Plugin save(Plugin plugin);

    Plugin findById(Long id);

    void updatePrimaryDataset(String diallerProviderName, String dataset);
}
