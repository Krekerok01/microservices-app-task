package com.specificgroup.blog.repository;

import com.specificgroup.blog.entity.Post;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostSpecification {

    public static Specification<Post> getSpecification(Long userId, String title,
                                                       LocalDate creationDate, LocalDate modificationDate) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (userId != null) {
                predicate.add(criteriaBuilder.equal(root.get("userId"), userId));
            }
            if (title != null) {
                predicate.add(criteriaBuilder.equal(root.get("title"), title.trim()));
            }
            if (creationDate != null) {
                LocalDateTime startOfDay = creationDate.atStartOfDay();
                LocalDateTime endOfDay = creationDate.plusDays(1).atStartOfDay().minusSeconds(1);
                predicate.add(criteriaBuilder.between(root.get("creationDate"), startOfDay, endOfDay));
            }
            if (modificationDate != null) {
                LocalDateTime startOfDay = modificationDate.atStartOfDay();
                LocalDateTime endOfDay = modificationDate.plusDays(1).atStartOfDay().minusSeconds(1);
                predicate.add(criteriaBuilder.between(root.get("modificationDate"), startOfDay, endOfDay));
            }

        return criteriaBuilder.and(predicate.toArray(new Predicate[0]));    });
    }
}
