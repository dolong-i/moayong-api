package com.moayong.api.domain.user.repository;

import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);

    Optional<User> findByNickname(String nickname);
}
