package uk.co.optimisticpanda.app;

import com.google.common.io.Resources;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ImportApplication extends Application<ImportApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new ImportApplication().run("server", Resources.getResource("config.yml").getFile());
    }

    @Override
    public String getName() {
        return "importer";
    }

    @Override
    public void initialize(Bootstrap<ImportApplicationConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(ImportApplicationConfiguration configuration,
                    Environment environment) {
        environment.jersey().register(new ImportResource());
        environment.jersey().register(new ValidationFilter());
    }

}