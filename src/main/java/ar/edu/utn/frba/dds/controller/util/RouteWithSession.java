package ar.edu.utn.frba.dds.controller.util;

import spark.Request;
import spark.Response;

import javax.persistence.EntityManager;

@FunctionalInterface
public interface RouteWithSession<R> {

    R apply(Request request, Response response, EntityManager entityManager);
}
