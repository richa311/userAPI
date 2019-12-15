package com.bemychef.users.user.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bemychef.users.user.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
