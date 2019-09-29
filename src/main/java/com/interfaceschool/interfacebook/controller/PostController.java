package com.interfaceschool.interfacebook.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interfaceschool.interfacebook.pojo.Comment;
import com.interfaceschool.interfacebook.pojo.Post;
import com.interfaceschool.interfacebook.pojo.User;
import com.interfaceschool.interfacebook.repo.CommentRepository;
import com.interfaceschool.interfacebook.repo.PostRepository;
import com.interfaceschool.interfacebook.repo.UserRepository;

@RestController
public class PostController {
	private PostRepository repo;
	private CommentRepository commentRepo;
	private UserRepository userRepo;

	@Autowired
	public PostController(PostRepository repo, UserRepository userRepo, CommentRepository commentRepo) {
		this.repo = repo;
		this.userRepo = userRepo;
		this.commentRepo = commentRepo;
	}

	@RequestMapping("/posts")
	public List<Post> getPosts(@RequestParam int limit, @RequestParam int offset) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		Pageable page = PageRequest.of(offset / limit, limit);

		List<Post> posts = (List<Post>) repo.findAllByOrderByDateDesc(page);

		for (Post post : posts) {
			if (name.equals(post.getUser().getName())) {
				post.setEditable(true);
			}
		}

		return posts;
	}

	@RequestMapping("/save-post")
	public Post savePost(@RequestParam String content, @RequestParam int id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User user = userRepo.findFirstByName(name);
		if (id > 0) {
			Post post = repo.findById(id).get();
			if (user.getName().equals(post.getUser().getName())) {
				post.setContent(content);
				post.setDate(new Date());
				//updating existing post
				post = repo.save(post);
				return post;
			}
		} else {
			Post post = new Post();
			post.setContent(content);
			post.setDate(new Date());
			post.setUser(user);
			//insert new post
			post = repo.save(post);
			return post;
		}
		return null;
	}

	@RequestMapping("/delete-post")
	public Post deletePost(@RequestParam int id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User user = userRepo.findFirstByName(name);
		Post post = repo.findById(id).get();
		if (user.getName().equals(post.getUser().getName())) {
			repo.delete(post);
		}
		return post;
	}
	
	@RequestMapping("/search-posts")
	public List<Post> searchPosts(@RequestParam int limit, @RequestParam int offset, @RequestParam String text) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		Pageable page = PageRequest.of(offset / limit, limit);

		List<Post> posts = (List<Post>) repo.findAllByContentContainingOrderByDateDesc(text, page);

		for (Post post : posts) {
			if (name.equals(post.getUser().getName())) {
				post.setEditable(true);
			}
		}

		return posts;
	}
	
	@RequestMapping("/save-comment")
	public Comment saveComment(@RequestParam String content, @RequestParam int id, @RequestParam int postId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User user = userRepo.findFirstByName(name);
		if (id > 0) {
			Comment comment = commentRepo.findById(id).get();
			if (user.getName().equals(comment.getUser().getName())) {
				comment.setText(content);
				comment.setDate(new Date());
				//updating existing comment
				comment = commentRepo.save(comment);
				return comment;
			}
		} else {
			Post post = repo.findById(postId).get();
			Comment comment = new Comment();
			comment.setText(content);
			comment.setDate(new Date());
			comment.setUser(user);
			comment.setPost(post);
			//insert new comment
			comment = commentRepo.save(comment);
			return comment;
		}
		return null;
	}
	
	@RequestMapping("/get-comments")
	public List<Comment> getPosts(@RequestParam int postId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		Post post = repo.findById(postId).get();
		List<Comment> comments = (List<Comment>) commentRepo.findAllByPostOrderByDateAsc(post);
		return comments;
	}

}
