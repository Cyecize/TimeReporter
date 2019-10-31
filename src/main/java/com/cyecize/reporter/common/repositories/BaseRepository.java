package com.cyecize.reporter.common.repositories;

import com.cyecize.reporter.common.repositories.utils.ActionResult;
import com.cyecize.reporter.common.repositories.utils.NoEntityManagerForSessionException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class BaseRepository<E, I> {

    /**
     * Value is populated and erased by @{@link com.cyecize.reporter.conn.interceptors.DatabaseConnectionInterceptor}
     */
    public static EntityManager currentEntityManager;

    protected final Class<E> persistentClass;

    protected final Class<I> primaryKeyType;

    protected EntityManager entityManager;

    protected CriteriaBuilder criteriaBuilder;

    @SuppressWarnings("unchecked")
    public BaseRepository() {
        final ParameterizedType parameterizedType = this.getParameterizedType();

        this.persistentClass = (Class<E>) parameterizedType.getActualTypeArguments()[0];
        this.primaryKeyType = (Class<I>) parameterizedType.getActualTypeArguments()[1];
    }

    public void persist(E entity) {
        this.execute((actionResult -> currentEntityManager.persist(entity)));
    }

    public void merge(E entity) {
        this.execute(repositoryActionResult -> currentEntityManager.merge(entity));
    }

    public void remove(E entity) {
        this.execute((actionResult -> currentEntityManager.remove(entity)));
    }

    public Long count() {
        String query = String.format("SELECT count (t) FROM %s t", this.persistentClass.getSimpleName());
        return this.execute((ar) -> ar.set(currentEntityManager.createQuery(query, Long.class).getSingleResult()), Long.class).get();
    }

    public E find(I id) {
        return this.execute(eActionResult -> eActionResult.set(this.entityManager.find(this.persistentClass, id))).get();
    }

    public List<E> findAll() {
        //Empty method means no conditions, therefore select all
        return this.queryBuilderList(((eCriteriaQuery, eRoot) -> {
        }));
    }

    protected synchronized <T> ActionResult<T> execute(Consumer<ActionResult<T>> invoker, Class<? extends T> returnType) {
        this.entityManager = currentEntityManager;
        this.criteriaBuilder = currentEntityManager.getCriteriaBuilder();

        if (currentEntityManager == null) {
            throw new NoEntityManagerForSessionException("No entity manager for session.");
        }

        ActionResult<T> actionResult = new ActionResult<>();
        EntityTransaction transaction = currentEntityManager.getTransaction();

        transaction.begin();
        try {
            invoker.accept(actionResult);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw new RuntimeException(ex);
        }

        return actionResult;
    }

    protected ActionResult<E> execute(Consumer<ActionResult<E>> invoker) {
        return this.execute(invoker, this.persistentClass);
    }

    @SuppressWarnings("unchecked")
    protected <T> T queryBuilderSingle(BiConsumer<CriteriaQuery<E>, Root<E>> invoker, Class<T> returnType) {
        return this.execute(ar -> {
            CriteriaQuery<E> criteria = this.criteriaBuilder.createQuery(this.persistentClass);
            Root<E> root = criteria.from(this.persistentClass);
            criteria.select(root);

            invoker.accept(criteria, root);

            ar.set((T) currentEntityManager.createQuery(criteria).getResultStream().findFirst().orElse(null));
        }, returnType).get();
    }

    protected E queryBuilderSingle(BiConsumer<CriteriaQuery<E>, Root<E>> invoker) {
        return this.queryBuilderSingle(invoker, this.persistentClass);
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> queryBuilderList(BiConsumer<CriteriaQuery<E>, Root<E>> invoker, Class<T> returnType) {
        return this.execute(ar -> {
            CriteriaQuery<E> criteria = this.criteriaBuilder.createQuery(this.persistentClass);
            Root<E> root = criteria.from(this.persistentClass);
            criteria.select(root);

            invoker.accept(criteria, root);

            ar.set(currentEntityManager.createQuery(criteria).getResultList());
        }, List.class).get();
    }

    protected List<E> queryBuilderList(BiConsumer<CriteriaQuery<E>, Root<E>> invoker) {
        return this.queryBuilderList(invoker, this.persistentClass);
    }

    private ParameterizedType getParameterizedType() {
        if (this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
            return (ParameterizedType) this.getClass().getGenericSuperclass();
        }

        return (ParameterizedType) this.getClass().getSuperclass().getGenericSuperclass();
    }
}
