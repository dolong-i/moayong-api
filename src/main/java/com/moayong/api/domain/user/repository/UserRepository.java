package com.moayong.api.domain.user.repository;

import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.auth.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderIdAndProvider(String provider_id, AuthProvider provider);
}
