package com.mach.valtech.rnrauth.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query(value = "SELECT u FROM Customer u where u.userName = ?1 and u.password = ?2 ")
    Optional<Customer> login(String username,String password);

    Optional<Customer> findByToken(String token);

    //To validate the token with user ID
    @Query("SELECT c from Customer c where c.token =:token")
    public Customer validateToken(String token);
}
