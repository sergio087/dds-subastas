package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.domain.exception.OfertaInvalidaException;
import org.eclipse.jetty.http.HttpStatus;
import spark.Spark;

public class ExceptionHandlersConfig {

    public static void exceptionHandlers() {

        Spark.exception(OfertaInvalidaException.class, (exception, request, response) -> {
            response.status(HttpStatus.BAD_REQUEST_400);
        } ) ;
    }
}
