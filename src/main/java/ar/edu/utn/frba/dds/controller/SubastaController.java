package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.controller.util.RouteWithSession;
import ar.edu.utn.frba.dds.repository.SubastaRepository;
import spark.Request;
import spark.Response;

import javax.persistence.EntityManager;

class SubastaController {

    static RouteWithSession getSubastas = (Request request, Response response, EntityManager entityManager) -> {

        final SubastaRepository repository = new SubastaRepository(entityManager);

        return repository.findAll(); //TODO Hay que paginar
    };
}
