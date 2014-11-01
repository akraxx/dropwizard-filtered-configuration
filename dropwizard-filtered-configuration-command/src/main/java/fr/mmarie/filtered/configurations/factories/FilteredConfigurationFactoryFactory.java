package fr.mmarie.filtered.configurations.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;

import javax.validation.Validator;

public class FilteredConfigurationFactoryFactory<T extends Configuration> implements ConfigurationFactoryFactory<T> {
    @Override
    public ConfigurationFactory<T> create(
            Class<T> klass,
            Validator    validator,
            ObjectMapper objectMapper,
            String propertyPrefix) {
        return new FilteredConfigurationFactory<>(klass, validator, objectMapper, propertyPrefix);
    }
}
