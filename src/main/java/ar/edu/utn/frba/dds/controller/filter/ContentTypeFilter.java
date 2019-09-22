package ar.edu.utn.frba.dds.controller.filter;

import spark.Filter;

public class ContentTypeFilter {

    public static Filter contentAppJson = (request, response) -> {
        response.type("application/json");
    };
}
