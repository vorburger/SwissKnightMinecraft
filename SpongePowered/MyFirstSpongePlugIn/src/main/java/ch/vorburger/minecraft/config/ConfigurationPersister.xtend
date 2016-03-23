package ch.vorburger.minecraft.config

import com.google.common.reflect.TypeToken
import java.io.IOException
import java.nio.file.Path
import javax.inject.Inject
import ninja.leaping.configurate.ConfigurationOptions
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import ninja.leaping.configurate.objectmapping.DefaultObjectMapperFactory
import ninja.leaping.configurate.objectmapping.ObjectMappingException
import org.spongepowered.api.config.DefaultConfig

/**
 * Utility which deals with Configuration persistence details.
 *
 * @author Michael Vorburger
 */
class ConfigurationPersister<T> {
    
    @Inject @DefaultConfig(sharedRoot = true) Path configPath
    @Inject @DefaultConfig(sharedRoot = true) ConfigurationLoader<CommentedConfigurationNode> configManager
    @Inject DefaultObjectMapperFactory objectMapperFactory
    Class<T> classT
    T configuration;
    
    def T load(Class<T> classT) {
        this.classT = classT
        try {
            if (configPath.toFile().exists()) {
                val CommentedConfigurationNode node = configManager.load(ConfigurationOptions.defaults().setObjectMapperFactory(objectMapperFactory))
                configuration = node.getValue(TypeToken.of(classT))
                if (configuration == null) {
                    throw new IllegalStateException("WTF did getValue() return null instead of an empty instance when loading the (existing!) config. file: " + configPath)
                }
            } else {
                configuration = classT.newInstance
                save
            }
            return configuration
        } catch (ObjectMappingException e) {
            throw new IllegalArgumentException("Could not load configuration" , e)
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load configuration" , e)
        }
    }
    
    def void save() {
        val CommentedConfigurationNode node = configManager.createEmptyNode(ConfigurationOptions.defaults().setObjectMapperFactory(objectMapperFactory))
        objectMapperFactory.getMapper(classT).bind(configuration).serialize(node)
        configManager.save(node)
    }
    
    def T configuration() {
        configuration
    }
}