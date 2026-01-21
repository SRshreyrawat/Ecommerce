package com.project.Ecommerce.Repository;

import com.project.Ecommerce.Entity.AppRole;
import com.project.Ecommerce.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);

    
}
