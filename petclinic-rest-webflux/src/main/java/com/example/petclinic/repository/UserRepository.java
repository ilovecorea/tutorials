package com.example.petclinic.repository;

import com.example.petclinic.model.User;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveSortingRepository<User, String> {

}
