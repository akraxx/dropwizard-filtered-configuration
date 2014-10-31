package fr.mmarie.filtered.configurations.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.mmarie.filtered.configurations.EnvironmentConfiguration;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;

import javax.validation.Validator;

public class FilteredConfigurationFactoryFactory<T extends EnvironmentConfiguration> implements ConfigurationFactoryFactory<T> {
    @Override
    public ConfigurationFactory<T> create(
            Class<T> klass,
            Validator    validator,
            ObjectMapper objectMapper,
            String propertyPrefix) {
        return new FilteredConfigurationFactory<T>(klass, validator, objectMapper, propertyPrefix);
    }
}
