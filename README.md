osgi-jetty-playground
=====================

A place to play with Jetty, Jersey &amp; Guice under OSGi

How to run
----------

```shell
git clone https://github.com/ash2k/osgi-jetty-playground.git
cd osgi-jetty-playground/osgi-wadl-resource
mvn clean install
cd ../osgi-wadl
mvn clean install
```

The last command will rebuild and launch OSGi console. Type `ss` to see the loaded bundles,
`help` for assistance and `close` to shut it down.