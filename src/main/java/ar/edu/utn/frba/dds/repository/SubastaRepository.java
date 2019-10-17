package ar.edu.utn.frba.dds.repository;

import ar.edu.utn.frba.dds.domain.Subasta;

import javax.persistence.EntityManager;

public class SubastaRepository extends CrudRepository<Subasta, Integer> {

    public SubastaRepository(EntityManager em) {
        super(em);
    }
}
