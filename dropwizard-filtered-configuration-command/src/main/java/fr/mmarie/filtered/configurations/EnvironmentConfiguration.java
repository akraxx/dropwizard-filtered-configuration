package fr.mmarie.filtered.configurations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Null;
import java.lang.reflect.ParameterizedType;

/**
 * Created by Maximilien on 30/10/2014.
 */
@Getter
@Setter
@ToString
@Slf4j
public class EnvironmentConfiguration<T extends FilteredConfiguration> extends Configuration {

    @JsonIgnore
    private T filteredConfiguration;

    @JsonIgnore
    public  Class<T> getFilteredConfigurationClass() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
