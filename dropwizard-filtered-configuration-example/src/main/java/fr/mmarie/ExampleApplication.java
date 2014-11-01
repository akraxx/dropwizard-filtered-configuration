package fr.mmarie;

import fr.mmarie.filtered.commands.ServerEnvironmentCommand;
import fr.mmarie.resources.TestResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Maximilien on 31/10/2014.
 */
@Slf4j
public class ExampleApplication extends Application<ExampleConfiguration> {

    @Override
    public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
        bootstrap.addCommand(new ServerEnvironmentCommand<>(bootstrap.getApplication()));
    }

    @Override
    public void run(ExampleConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new TestResource());

        log.info("RUN DROPWIZARD WITH FOLLOWING CONFIG : {}", configuration);
    }

    public static void main(String[] args) throws Exception {
        new ExampleApplication().run(new String[] {"serverenv", "-e", "stg", "properties.yml"});
    }
}
