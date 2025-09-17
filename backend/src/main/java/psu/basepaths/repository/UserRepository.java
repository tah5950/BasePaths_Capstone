package psu.basepaths.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.basepaths.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
