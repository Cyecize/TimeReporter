package com.cyecize.reporter.common.repositories.utils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;

public class NotNullPredicateList extends ArrayList<Predicate> {

    public NotNullPredicateList() {
        super();
    }

    public NotNullPredicateList(Predicate... predicates) {
        super();
        for (Predicate predicate : predicates) {
            this.add(predicate);
        }
    }

    @Override
    public boolean add(Predicate predicate) {
        if (predicate == null) {
            return false;
        }

        return super.add(predicate);
    }

    @Override
    public Predicate[] toArray() {
        return this.toArray(Predicate[]::new);
    }
}
