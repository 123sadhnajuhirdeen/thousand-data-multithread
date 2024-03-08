package com.sadhna.async.multithread.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sadhna.async.multithread.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
