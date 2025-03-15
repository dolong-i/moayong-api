package com.moayong.api.domain.verification.repository;


import com.moayong.api.domain.verification.domain.Verification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends CrudRepository<Verification, String> {
}