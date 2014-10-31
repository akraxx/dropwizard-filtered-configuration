dropwizard-filtered-configuration
=================================

A way to launch dropwizard with an environment arg which will filter your configuration automatically

======

To launch the current example with mvn : mvn clean install

======

Arguments to launch the dropwizard :

serverenv -e [dev,stg,acc,prod] properties.yml

serverenv --environment [dev,stg,acc,prod] properties.yml

The file [env].yml must be in the folder "filters" in the resources folder.

The file properties.yml (or any name you want) must be in the resources folder.

