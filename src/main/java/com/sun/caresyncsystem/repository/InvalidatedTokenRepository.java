package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.InvalidatedToken;
import org.springframework.data.repository.CrudRepository;

public interface InvalidatedTokenRepository extends CrudRepository<InvalidatedToken, String> {
}
