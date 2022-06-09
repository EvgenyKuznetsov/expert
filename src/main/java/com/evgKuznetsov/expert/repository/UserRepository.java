package com.evgKuznetsov.expert.repository;

import com.evgKuznetsov.expert.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT usr FROM user usr LEFT JOIN FETCH usr.roles")
    List<User> findAll();

    Optional<User> getByPhoneNumber(String phone);

    Optional<User> getByEmail(String email);

    @Override
    User save(User user);


}
