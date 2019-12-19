package com.bemychef.users.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bemychef.users.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
