package ar.edu.utn.frba.dds.controller;

import com.google.gson.Gson;
import spark.ResponseTransformer;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static ar.edu.utn.frba.dds.controller.filter.AuthenticationFilter.authenticate;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.staticFiles;

public class RoutesConfig {

    public static void routes() {

        final HandlebarsTemplateEngine templateEngine = new HandlebarsTemplateEngine();
        final ResponseTransformer transformToJson = new Gson()::toJson;


        staticFiles.location("/public");

        path("/api", () -> {

            before("/*",authenticate);

            path("/subastas", () -> {

                // TODO completar API para el recurso subasta

                get("", SubastaController.getSubastas, transformToJson);
            });


            path("/ofertas", () -> {
                // TODO completar API para el recurso oferta
            });

            path("/usuarios", () -> {
                // TODO completar API para el recurso usuario
            });
        });

        get("/", HomeViewController.home, templateEngine);
    }
}
