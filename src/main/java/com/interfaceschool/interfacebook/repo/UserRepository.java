package com.interfaceschool.interfacebook.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.interfaceschool.interfacebook.pojo.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	public User findFirstByName(String name);
}
