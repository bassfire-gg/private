package org.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.user_service.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
        SELECT u 
        FROM User u
        WHERE u.created_at >= :start
            AND created_at < :end
    """)
    List<User> findUsersCreatedOn(@Param("start")LocalDateTime start,
                                  @Param("end") LocalDateTime end);

}
