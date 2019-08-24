package com.cyecize.reporter.common.repositories;

import com.cyecize.reporter.common.repositories.utils.ActionResult;
import com.cyecize.reporter.common.repositories.utils.NoEntityManagerForSessionException;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class BaseRepository<E, I> {

    /**
     * Value is populated and erased by @DatabaseConnectionInterceptor
     */
    public static EntityManager currentEntityManager;

    protected Class<E> persistentClass;

    protected Class<I> primaryKeyType;

    protected String primaryKeyFieldName;

    protected EntityManager entityManager;

    protected CriteriaBuilder criteriaBuilder;

    @SuppressWarnings("unchecked")
    public BaseRepository() {
        this.persistentClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.primaryKeyType = (Class<I>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.setPrimaryKeyFieldName();
    }

    public void persist(E entity) {
        this.execute((actionResult -> {
            currentEntityManager.persist(entity);
            currentEntityManager.flush();
        }));
    }

    public void merge(E entity) {
        this.execute(repositoryActionResult -> {
            currentEntityManager.merge(entity);
            currentEntityManager.flush();
        });
    }

    public void remove(E entity) {
        this.execute((actionResult -> {
            currentEntityManager.remove(entity);
            currentEntityManager.flush();
        }));
    }

    public Long count() {
        String query = String.format("SELECT count (t) FROM %s t", this.persistentClass.getSimpleName());
        return this.execute((ar) -> ar.set(currentEntityManager.createQuery(query, Long.class).getSingleResult()), Long.class).get();
    }

    public E find(I id) {
        return this.queryBuilderSingle(((eCriteriaQuery, eRoot) -> eCriteriaQuery.where(this.criteriaBuilder.equal(eRoot.get(this.primaryKeyFieldName), id))));
    }

    public List<E> findAll() {
        //Empty method means no conditions, therefore select all
        return this.queryBuilderList(((eCriteriaQuery, eRoot) -> {}));
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

    private void setPrimaryKeyFieldName() {
        Field primaryKeyField = Arrays.stream(this.persistentClass.getDeclaredFields())
                .filter(f -> f.getType() == this.primaryKeyType && f.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);

        if (primaryKeyField == null) {
            throw new RuntimeException(String.format("Entity %s does not have primary key", this.persistentClass.getName()));
        }

        Column columnAnnotation = primaryKeyField.getAnnotation(Column.class);

        if (columnAnnotation != null && !columnAnnotation.name().equals("")) {
            this.primaryKeyFieldName = columnAnnotation.name();
        } else {
            this.primaryKeyFieldName = primaryKeyField.getName();
        }
    }
}
