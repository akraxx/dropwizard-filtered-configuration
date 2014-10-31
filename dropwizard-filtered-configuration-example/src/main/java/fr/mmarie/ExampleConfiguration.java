package fr.mmarie;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.mmarie.filtered.configurations.EnvironmentConfiguration;
import lombok.ToString;

/**
 * Created by Maximilien on 31/10/2014.
 */
@ToString(callSuper = true)
public class ExampleConfiguration extends EnvironmentConfiguration<FilteredConfigurationExample> {
    @JsonProperty("common")
    public String common;
}
