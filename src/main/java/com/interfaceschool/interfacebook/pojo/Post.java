package com.interfaceschool.interfacebook.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="posts")
@SecondaryTable(name = "post_comments")
public class Post {
	@Id
	@Column(name="post_id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="post_user_id", nullable=false)
	private User user;

	@Column(name="post_date")
	private Date date;
	
	@Column(name="post_content")
	private String content;
	
	@Transient
	private boolean editable;
	
	@Column(name="comment_count", table="post_comments", insertable=false, updatable=false)
	private Integer commentCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
}
