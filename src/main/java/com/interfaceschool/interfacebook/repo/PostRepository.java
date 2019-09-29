package com.interfaceschool.interfacebook.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.interfaceschool.interfacebook.pojo.Post;

public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {
	public List<Post> findAllByOrderByDateDesc(Pageable page);
	public Optional<Post> findById(Integer id);
	public Post save(Post post);
	public List<Post> findAllByContentContainingOrderByDateDesc(String content, Pageable page);
}

