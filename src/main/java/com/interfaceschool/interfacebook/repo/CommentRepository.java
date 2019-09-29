package com.interfaceschool.interfacebook.repo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.interfaceschool.interfacebook.pojo.Comment;
import com.interfaceschool.interfacebook.pojo.Post;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {
	public Comment save(Comment comment);
	public List<Comment> findAllByPostOrderByDateAsc(Post post);
}
