package ar.edu.utn.frba.dds.controller.util;

import ar.edu.utn.frba.dds.repository.SessionProvider;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

@Slf4j
public class RouteWithSessionWrapper {

    public static <R> Route wrap(Boolean inTransaction, RouteWithSession<R> route) {

        return (Request request, Response response) -> {

            EntityManager session = SessionProvider.get();

            try {

                if (inTransaction) session.getTransaction().begin();

                try {

                    R r = route.apply(request, response, session);

                    if (inTransaction) session.getTransaction().commit();

                    return r;

                } catch (IllegalStateException | RollbackException e) {

                    log.error("Commit fail.", e);

                    throw e;
                } catch (Throwable e) {

                    if (inTransaction) session.getTransaction().rollback();

                    throw e;
                }
            } finally {
                session.close();
            }
        };
    }
}
