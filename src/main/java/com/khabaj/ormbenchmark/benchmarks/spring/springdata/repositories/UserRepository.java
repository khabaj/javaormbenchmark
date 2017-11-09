package com.khabaj.ormbenchmark.benchmarks.spring.springdata.repositories;

import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByFirstName(String firstName);
    List<User> findByLastName(String lastName);

    @Query("select u from User u, Phone p where u = p.user and p.phoneNumber = :phoneNumber")
    List<User> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
