package com.bemychef.users.dao;

import javax.inject.Named;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bemychef.users.model.ConfirmationToken;

@Repository
@Named(value = "confirmationTokenRepo")
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long>{

}
