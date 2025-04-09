package com.lenarsharipov.bookservice.repository;

import com.lenarsharipov.bookservice.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FilterBookRepositoryImpl implements FilterBookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Book> findAllByFilter(String title, String brand, Integer year, Pageable pageable) {
        // 1. Создаем основной запрос
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> book = query.from(Book.class);

        // 2. Строим предикаты для фильтрации
        List<Predicate> predicates = buildPredicates(cb, book, title, brand, year);

        // 3. Применяем условия и сортировку
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        query.orderBy(cb.asc(book.get("id"))); // Сортировка по ID

        // 4. Получаем результат с пагинацией
        List<Book> result = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // 5. Считаем общее количество с учетом фильтров
        Long count = getTotalCount(cb, title, brand, year);

        return new PageImpl<>(result, pageable, count);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Book> root,
                                            String title, String brand, Integer year) {
        List<Predicate> predicates = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
            ));
        }

        if (brand != null && !brand.isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("brand")),
                    "%" + brand.toLowerCase() + "%"
            ));
        }

        if (year != null) {
            predicates.add(cb.equal(root.get("publicationYear"), year));
        }

        return predicates;
    }

    private Long getTotalCount(CriteriaBuilder cb, String title, String brand, Integer year) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Book> countRoot = countQuery.from(Book.class);

        List<Predicate> countPredicates = buildPredicates(cb, countRoot, title, brand, year);

        countQuery.select(cb.count(countRoot));

        if (!countPredicates.isEmpty()) {
            countQuery.where(countPredicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
