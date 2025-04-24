package com.mytradein.repository;

import com.mytradein.model.Authtoken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthtokenRepository extends JpaRepository<Authtoken, Long>
{
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Authtoken t WHERE t.tokenType = 'API'")
    boolean isApiTokenConfigured();

    @Query("SELECT t FROM Authtoken t WHERE t.login = :login AND t.tokenValue = :tokenValue AND t.active = true")
    Authtoken getActiveTokenByType(String login, String tokenValue);

    @Query("SELECT t FROM Authtoken t WHERE t.tokenValue = :tokenValue AND t.active = true")
    Authtoken getActiveTokenByValue(String tokenValue);

    @Query("SELECT t FROM Authtoken t WHERE t.login = :login  AND t.active = true AND t.tokenType = :tokenType")
    Authtoken getApiTokenByLogin(String login, String tokenType);
}
