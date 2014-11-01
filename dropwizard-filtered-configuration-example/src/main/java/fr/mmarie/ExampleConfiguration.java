package fr.mmarie;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.ToString;

/**
 * Created by Maximilien on 31/10/2014.
 */
@ToString
public class ExampleConfiguration extends Configuration {
    @JsonProperty("common")
    public String common;

    @JsonProperty("testEnv")
    public String testEnv;
}
