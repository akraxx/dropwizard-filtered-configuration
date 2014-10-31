package fr.mmarie.filtered.source;

import com.google.common.base.Preconditions;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Maximilien on 30/10/2014.
 */
@Slf4j
public class ResourceConfigurationSourceProvider implements ConfigurationSourceProvider {
    @Override
    public InputStream open(String path) throws IOException {
        try {
            return checkNotNull(getClass().getResourceAsStream("/".concat(path)));
        } catch (Exception e) {
            log.error("Impossible to get the file {}, check that you added it into your resources folder", path);
            throw e;
        }
    }
}
