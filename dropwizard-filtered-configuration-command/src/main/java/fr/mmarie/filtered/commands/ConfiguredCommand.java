package fr.mmarie.filtered.commands;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.mmarie.filtered.configurations.factories.FilteredConfigurationFactory;
import fr.mmarie.filtered.configurations.factories.FilteredConfigurationFactoryFactory;
import fr.mmarie.filtered.source.ResourceConfigurationSourceProvider;
import io.dropwizard.Configuration;
import io.dropwizard.cli.Command;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.util.Generics;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import javax.validation.Validator;
import java.io.IOException;

/**
 * A command whose first parameter is the location of a YAML configuration file. That file is parsed
 * into an instance of a {@link io.dropwizard.Configuration} subclass, which is then validated. If the
 * configuration is valid, the command is run.
 *
 * @param <T> the {@link io.dropwizard.Configuration} subclass which is loaded from the configuration file
 * @see io.dropwizard.Configuration
 */
@Getter
@Slf4j
public abstract class ConfiguredCommand<T extends Configuration> extends Command {
    private boolean asynchronous;

    private T configuration;

    protected ConfiguredCommand(String name, String description) {
        super(name, description);
        this.asynchronous = false;
    }

    /**
     * Returns the {@link Class} of the configuration type.
     *
     * @return the {@link Class} of the configuration type
     */
    protected Class<T> getConfigurationClass() {
        return Generics.getTypeParameter(getClass(), Configuration.class);
    }

    /**
     * Configure the command's {@link net.sourceforge.argparse4j.inf.Subparser}. <p><strong> N.B.: if you override this method, you
     * <em>must</em> call {@code super.override(subparser)} in order to preserve the configuration
     * file parameter in the subparser. </strong></p>
     *
     * @param subparser the {@link net.sourceforge.argparse4j.inf.Subparser} specific to the command
     */
    @Override
    public void configure(Subparser subparser) {

        subparser.addArgument("file").nargs("?").help("application configuration file");
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());

        configuration = parseConfiguration(new FilteredConfigurationFactoryFactory<>(),
                                           bootstrap.getConfigurationSourceProvider(),
                                           bootstrap.getValidatorFactory().getValidator(),
                                           namespace.getString("file"),
                                            namespace.getString("environment"),
                                           getConfigurationClass(),
                                           bootstrap.getObjectMapper());

        try {
            if (configuration != null) {
                configuration.getLoggingFactory().configure(bootstrap.getMetricRegistry(),
                                                            bootstrap.getApplication().getName());
            }

            run((Bootstrap<T>) bootstrap, namespace, configuration);
        } finally {
            if (!asynchronous) {
                cleanup();
            }
        }
    }

    protected void cleanupAsynchronously() {
        this.asynchronous = true;
    }

    protected void cleanup() {
        if (configuration != null) {
            configuration.getLoggingFactory().stop();
        }
    }

    /**
     * Runs the command with the given {@link io.dropwizard.setup.Bootstrap} and {@link io.dropwizard.Configuration}.
     *
     * @param bootstrap     the bootstrap bootstrap
     * @param namespace     the parsed command line namespace
     * @param configuration the configuration object
     * @throws Exception if something goes wrong
     */
    protected abstract void run(Bootstrap<T> bootstrap,
                                Namespace namespace,
                                T configuration) throws Exception ;

    private T parseConfiguration(ConfigurationFactoryFactory<T> configurationFactoryFactory,
                                 ConfigurationSourceProvider provider,
                                 Validator validator,
                                 String path,
                                 String env,
                                 Class<T> klass,
                                 ObjectMapper objectMapper) throws IOException, ConfigurationException {
        final ConfigurationFactory<T> configurationFactory = configurationFactoryFactory.create(klass, validator, objectMapper, "dw");


        if(configurationFactory instanceof FilteredConfigurationFactory) {
            return ((FilteredConfigurationFactory<T>)configurationFactory).build(provider, path, env);
        } else if (path != null) {
            return configurationFactory.build(provider, path);
        }
        return configurationFactory.build();
    }
}
