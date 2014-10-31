package fr.mmarie;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.mmarie.filtered.configurations.FilteredConfiguration;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Maximilien on 30/10/2014.
 */
@Getter
@ToString
public class FilteredConfigurationExample implements FilteredConfiguration {

    @JsonProperty("testEnv")
    public String testEnv;

}
