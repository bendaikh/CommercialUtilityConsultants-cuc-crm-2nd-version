package mycrm.services;

import mycrm.models.Plugin;
import mycrm.repositories.PluginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class PluginServiceImpl implements PluginService {

    private final PluginRepository pluginRepository;

    @Autowired
    public PluginServiceImpl(PluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }


    @Override
    public Plugin getPluginByApi(String name) {
        return this.pluginRepository.findByApi(name);
    }

    @Override
    public List<Plugin> findAll() {
        return this.pluginRepository.findAll();
    }

    @Override
    public Plugin save(Plugin plugin) {
        return this.pluginRepository.save(plugin);
    }

    @Override
    public Plugin findById(Long id) {
        return this.pluginRepository.findOne(id);
    }

    @Override
    public void updatePrimaryDataset(String diallerProviderName, String dataset) {
        this.pluginRepository.updatePrimaryDataset(diallerProviderName, dataset);
    }
}
