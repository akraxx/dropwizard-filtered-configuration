package fr.mmarie.filtered.commands;

import fr.mmarie.filtered.configurations.EnvironmentConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * A command which executes with a configured {@link io.dropwizard.setup.Environment}.
 *
 * @param <T> the {@link io.dropwizard.Configuration} subclass which is loaded from the configuration file
 * @see io.dropwizard.Configuration
 */
public abstract class EnvironmentCommand<T extends EnvironmentConfiguration> extends ConfiguredCommand<T> {
    private final Application<T> application;

    /**
     * Creates a new environment command.
     *
     * @param application     the application providing this command
     * @param name        the name of the command, used for command line invocation
     * @param description a description of the command's purpose
     */
    protected EnvironmentCommand(Application<T> application, String name, String description) {
        super(name, description);
        this.application = application;
    }

    @Override
    protected final void run(Bootstrap<T> bootstrap, Namespace namespace, T configuration) throws Exception {
        final Environment environment = new Environment(bootstrap.getApplication().getName(),
                                                        bootstrap.getObjectMapper(),
                                                        bootstrap.getValidatorFactory().getValidator(),
                                                        bootstrap.getMetricRegistry(),
                                                        bootstrap.getClassLoader());
        configuration.getMetricsFactory().configure(environment.lifecycle(),
                                                    bootstrap.getMetricRegistry());
        bootstrap.run(configuration, environment);
        application.run(configuration, environment);
        run(environment, namespace, configuration);
    }

    /**
     * Runs the command with the given {@link io.dropwizard.setup.Environment} and {@link io.dropwizard.Configuration}.
     *
     * @param environment   the configured environment
     * @param namespace     the parsed command line namespace
     * @param configuration the configuration object
     * @throws Exception if something goes wrong
     */
    protected abstract void run(Environment environment, Namespace namespace, T configuration) throws Exception;
}
