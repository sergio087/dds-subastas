package ar.edu.utn.frba.dds.repository;

import spark.utils.Assert;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public abstract class CrudRepository<ENTITY extends EntityObject, ID> {

    protected final EntityManager em;
    protected final Class<ENTITY> domainClass;
    protected final Class<ID> idClass;
    private static final String ID_NULL = "The id must not be null!";
    private static final String ENTITY_NULL = "The entity must not be null!";

    @SuppressWarnings(value = "unchecked")
    public CrudRepository(EntityManager em) {
        this.em = em;

        final Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();

        this.domainClass = ((Class<ENTITY>) actualTypeArguments[0]);
        this.idClass = ((Class<ID>) actualTypeArguments[1]);
    }

    public Optional<ENTITY> findById(ID id) {
        Assert.notNull(id, ID_NULL);
        return Optional.ofNullable(em.find(domainClass, id));
    }

    public List<ENTITY> findAll(){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ENTITY> query = builder.createQuery(domainClass);

        Root<ENTITY> root = query.from(domainClass);
        query.select(root);

        return em.createQuery(query).getResultList();
    }

    public ENTITY create(ENTITY entity) {
        Assert.notNull(entity, ENTITY_NULL);
        em.persist(entity);
        em.flush();

        return entity;
    }

    public void delete(ENTITY entity) {
        Assert.notNull(entity, ENTITY_NULL);
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }
}
