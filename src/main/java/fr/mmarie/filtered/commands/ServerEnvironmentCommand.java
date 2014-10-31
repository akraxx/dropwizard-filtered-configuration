package fr.mmarie.filtered.commands;

import fr.mmarie.filtered.configurations.EnvironmentConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a application as an HTTP server.
 *
 * @param <T> the {@link io.dropwizard.Configuration} subclass which is loaded from the configuration file
 */
public class ServerEnvironmentCommand<T extends EnvironmentConfiguration> extends EnvironmentCommand<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(io.dropwizard.cli.ServerCommand.class);

    private final Class<T> configurationClass;

    public ServerEnvironmentCommand(Application<T> application) {
        super(application, "serverenv", "Runs the Dropwizard application as an HTTP server");
        this.configurationClass = application.getConfigurationClass();
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);
        subparser.addArgument("-e", "--environment")
                .required(true)
                .choices("dev", "stg", "acc", "prod")
                .dest("environment")
                .help("The environment were the application must be run.");
    }

    /*
         * Since we don't subclass ServerCommand, we need a concrete reference to the configuration
         * class.
         */
    @Override
    protected Class<T> getConfigurationClass() {
        return configurationClass;
    }


    @Override
    protected void run(Environment environment, Namespace namespace, T configuration) throws Exception {
        final Server server = configuration.getServerFactory().build(environment);
        try {
            server.addLifeCycleListener(new LifeCycleListener());
            cleanupAsynchronously();
            server.start();

        } catch (Exception e) {
            LOGGER.error("Unable to start server, shutting down", e);
            server.stop();
            cleanup();
            throw e;
        }
    }


    private class LifeCycleListener extends AbstractLifeCycle.AbstractLifeCycleListener {
        @Override
        public void lifeCycleStopped(LifeCycle event) {
            cleanup();
        }
    }
}
