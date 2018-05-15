package com.djavid.checkserver.model.repository;

import com.djavid.checkserver.model.entity.RegistrationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationTokenRepository extends CrudRepository<RegistrationToken, Long> {

    RegistrationToken findRegistrationTokenByToken(String token);
    RegistrationToken findRegistrationTokenById(Long id);

}
