package ar.edu.utn.frba.dds.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SessionProvider {

    private static EntityManagerFactory sessionFactory;

    public synchronized static EntityManager get(){

        if(sessionFactory == null){
            sessionFactory = Persistence.createEntityManagerFactory("ar.edu.utn.frba.dds.subasta");
        }

        return sessionFactory.createEntityManager();
    }
}
