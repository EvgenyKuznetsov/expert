package com.evgKuznetsov.expert.repository;

import com.evgKuznetsov.expert.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public interface RoleRepository extends JpaRepository<Role, Long> {

}
