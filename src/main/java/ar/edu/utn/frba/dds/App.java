package ar.edu.utn.frba.dds;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import spark.servlet.SparkApplication;

import java.util.Properties;

import static ar.edu.utn.frba.dds.controller.ExceptionHandlersConfig.exceptionHandlers;
import static ar.edu.utn.frba.dds.controller.RoutesConfig.routes;
import static spark.Spark.awaitInitialization;
import static spark.Spark.awaitStop;
import static spark.Spark.port;
import static spark.Spark.threadPool;

@Slf4j
public class App implements SparkApplication {

    public static void main(String[] args) {
        new App().init();
    }

    @Override
    public void init() {

        log.info("Starting application ...");

        final Properties properties = recoveryProperties();

        port(Integer.parseInt(properties.getProperty("jetty.port", "80")));

        threadPool(
                Integer.parseInt(properties.getProperty("jetty.maxThreads", "8")),
                Integer.parseInt(properties.getProperty("jetty.minThreads", "2")),
                Integer.parseInt(properties.getProperty("jetty.timeOutMillis", "30000"))
        );

        exceptionHandlers();

        routes();

        awaitInitialization();

        log.info("Done! Application started.");

        awaitStop();
    }

    @SneakyThrows
    private Properties recoveryProperties() {
        final Properties properties = new Properties();

        properties.load(getClass().getClassLoader().getResourceAsStream("server.properties"));

        return properties;
    }
}
