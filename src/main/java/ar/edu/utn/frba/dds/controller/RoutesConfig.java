package ar.edu.utn.frba.dds.controller;

import com.google.gson.Gson;
import spark.ResponseTransformer;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static ar.edu.utn.frba.dds.controller.SubastaController.getSubastas;
import static ar.edu.utn.frba.dds.controller.util.AuthenticationFilter.authenticate;
import static ar.edu.utn.frba.dds.controller.util.ContentTypeFilter.contentAppJson;
import static ar.edu.utn.frba.dds.controller.util.RouteWithSessionWrapper.wrap;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.staticFiles;

public class RoutesConfig {

    private static final HandlebarsTemplateEngine templateEngine = new HandlebarsTemplateEngine();
    private static final ResponseTransformer transformToJson = new Gson()::toJson;

    public static void routes() {

        staticFiles.location("/public");

        path("/api", () -> {

            before("/*", authenticate);
            after("/*", contentAppJson);

            path("/subastas", () -> {

                // TODO completar API para el recurso subasta

                get("", wrap(true, getSubastas), transformToJson);
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
