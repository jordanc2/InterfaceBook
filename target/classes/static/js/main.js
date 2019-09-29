$(function() {
	var offset = 0;
	var limit = 20;
	var ajaxDone = true;
	var morePages = true;
	var editId = 0;
	var commentPostId = 0;

	getPosts();
	$(window).scroll(scrolled);
	$("#add-post").click(showAddPost);
	$("#close-post").click(closeAddPost);
	$("#save-post").click(savePost);
	$("#delete-post").click(deletePost);
	$("main").on("click", ".edit-post", showEditPost);
	$("#search").bind("search", searchKey);
	$("main").on("click", ".comment-count", getComments);
	$("main").on("click", ".add-comment", showAddComment);
	$("#close-comment").click(closeAddComment);
	$("#save-comment").click(saveComment);
	$("#delete-comment").click(deleteComment);
	
	function getComments() {
		var postId = $(this).parent().parent().find(".edit-post").data("id");
		var $commentTemplate = $(this).parent().find(".comment-template");
		$.ajax({
			url : "/get-comments",
			method : "get",
			dataType : "json",
			data : {
				postId: postId
			},
			error: ajaxError,
			success: function(data) {
				buildComments(data, $commentTemplate);
			}
		});
		return false;
	}
	
	function buildComments(data, $commentTemplate) {
		$commentTemplate.parent().find(".comment").remove();
		for(var i=0; i < data.length; i++) {
			var $comment = $commentTemplate.clone();
			$comment.removeClass("comment-template");
			$comment.addClass("comment");
			$comment.find(".comment-user").text(data[i].user.name);
			$comment.find(".comment-text").text(data[i].text);
			$commentTemplate.parent().append($comment);
		}
	}
	
	function closeAddComment(){
		$("#add-comment-popup").hide();
	}
	
	function saveComment() {
		$("#add-comment-popup").hide();
		$.ajax({
			url : "/save-comment",
			method : "post",
			dataType : "json",
			data : {
				content : $("#add-comment-popup textarea").val(),
				id: editId,
				postId: commentPostId
			},
			error : ajaxError,
			success : function(data) {
				console.log(data);
				closeAddPost();
				reloadPosts();
			}
		});
	}
	
	function deleteComment() {
		
	}
	
	function showAddComment() {
		editId = 0;
		commentPostId = $(this).parent().find(".edit-post").data("id");
		var $popup = $("#add-comment-popup").detach();
		$(this).parent().parent().after($popup);
		$popup.show();
	}
	
	function deletePost() {
		$.ajax({
			url : "/delete-post",
			method : "post",
			dataType : "json",
			data : {
				id: editId
			},
			error : ajaxError,
			success : function(data) {
				console.log(data);
				closeAddPost();
				reloadPosts();
			}
		});
	}

	function savePost() {
		$.ajax({
			url : "/save-post",
			method : "post",
			dataType : "json",
			data : {
				content : $("#add-post-popup textarea").val(),
				id: editId
			},
			error : ajaxError,
			success : function(data) {
				console.log(data);
				closeAddPost();
				reloadPosts();
			}
		});
	}
	
	function reloadPosts() {
		offset = 0;
		$(".post").remove();
		getPosts();
	}
	
	function showEditPost() {
		$("#delete-post").show();
		var text = $(this).prev().text();
		$("#add-post-popup textarea").val(text);
		$("#add-post-popup").addClass("show-add-popup");
		$(window).scrollTop(0);
		var id = $(this).data("id");
		editId = id;
	}

	function showAddPost() {
		editId = 0;
		$("#add-post-popup textarea").val("");
		$("#delete-post").hide();
		$("#add-post-popup").addClass("show-add-popup");
	}

	function closeAddPost() {
		$("#add-post-popup").removeClass("show-add-popup");
	}

	function scrolled() {
		if (ajaxDone && morePages) {
			var top = $(this).scrollTop();
			var size = $("body").height();
			var height = $(this).height();
			if (size - height * 3 <= top) {
				offset += limit;
				getPosts();
			}
		}
	}
	
	function searchKey(event) {
		ajaxDone = false;
			$.ajax({
				url : "/search-posts",
				method : "post",
				dataType : "json",
				data : {
					text: $("#search").val(),
					limit: limit,
					offset: offset
				},
				error : function() {
					ajaxDone = true;
					ajaxError();
				},
				success : function(data) {
					ajaxDone = true;
					if (data.length < limit) {
						morePages = false;
					}
					showSearchResults(data);
				}
			});
	}
	
	function showSearchResults(data) {
		offset = 0;
		$(".post").remove();
		buildPosts(data);
	}

	function getPosts() {
		ajaxDone = false;
		var text = $("#search").val();
		var url = "/posts";
		var data = {
				limit: limit,
				offset: offset
		}
		if (text != "") {
			url = "/search-posts";
			data.text = text;
		}
		$.ajax({
			url : url,
			method : "get", // use get to retrieve a little info, post retrieves
							// a lot of info
			dataType : "json",
			data : data,
			error : function() {
				ajaxDone = true;
				ajaxError();
			},
			success : function(data) {
				ajaxDone = true;
				if (data.length < limit) {
					morePages = false;
				}
				buildPosts(data);
			}
		});
	}

	function ajaxError() {
		alert("ajax error!");
	}

	function buildPosts(data) {
		for (var i = 0; i < data.length; i++) {
			var $post = $("#post-template").clone();
			$post.removeAttr("id");
			$post.addClass("post");
			$post.find(".username").append(data[i].user.name);
			$post.find("time").append(data[i].date);
			$post.find(".post-text").append(data[i].content);
			if (!data[i].editable) {
				$post.find(".edit-post").hide();
			}
			$post.find(".edit-post").data("id", data[i].id);
			$post.find(".comment-count").append(data[i].commentCount + " comments");
			$("main").append($post);
		}
	}
});