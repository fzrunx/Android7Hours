package com.sesac.community.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.community.utils.calculateTimeAgo
import com.sesac.domain.model.Community
import com.sesac.domain.usecase.GetAllCommunityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

// Presentation Layer Model
data class Post(
    val id: Long,
    val author: String,
    val authorImage: String,
    val timeAgo: String,
    val content: String,
    val image: String?,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean,
    val category: String,
    val createdAt: Date = Date(System.currentTimeMillis())
)

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllCommunityUseCase,
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery get() = _searchQuery.asStateFlow()
    private val _activeFilter = MutableStateFlow("전체")
    val activeFilter get() = _activeFilter.asStateFlow()

    init {
        observePosts()
    }

    private fun observePosts() {
        viewModelScope.launch {
            getAllPostsUseCase()
                .map { postModels ->
                    postModels.mapIndexed { index, postModel ->
                        postModel.toPresentation(index.toLong())
                    }.sortedByDescending { it.createdAt }
                }
                .collect { posts ->
                    _posts.value = posts
                }
        }
    }

    val filteredPosts: StateFlow<List<Post>> = combine(
        _posts,
        _searchQuery,
        _activeFilter
    ) { posts, query, filter ->
        var filtered = posts

        if (filter != "전체") {
            filtered = if (filter == "인기글") {
                filtered.filter { it.likes > 100 }
            } else {
                filtered.filter { it.category == filter }
            }
        }

        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.content.contains(query, ignoreCase = true) ||
                        it.author.contains(query, ignoreCase = true)
            }
        }
        filtered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChange(filter: String) {
        _activeFilter.value = filter
    }

    fun onLikeToggle(postId: Long) {
        _posts.update { currentPosts ->
            currentPosts.map { post ->
                if (post.id == postId) {
                    post.copy(
                        isLiked = !post.isLiked,
                        likes = if (post.isLiked) post.likes - 1 else post.likes + 1
                    )
                } else {
                    post
                }
            }
        }
    }

    fun createPost(content: String, image: String?, category: String) {
        val newPost = Post(
            id = System.currentTimeMillis(),
            author = "나", // TODO: 실제 사용자 정보로 대체
            authorImage = "https://picsum.photos/seed/me/200", // TODO: 실제 사용자 프로필로 대체
            timeAgo = "방금 전",
            content = content,
            image = image.takeIf { !it.isNullOrBlank() },
            likes = 0,
            comments = 0,
            isLiked = false,
            category = category,
            createdAt = Date()
        )
        _posts.update { currentPosts ->
            (listOf(newPost) + currentPosts).sortedByDescending { it.createdAt }
        }
    }

    fun updatePost(updatedPost: Post) {
        _posts.update { currentPosts ->
            currentPosts.map { post ->
                if (post.id == updatedPost.id) {
                    updatedPost
                } else {
                    post
                }
            }.sortedByDescending { it.createdAt }
        }
    }

    fun deletePost(postId: Long) {
        _posts.update { currentPosts ->
            currentPosts.filterNot { it.id == postId }
        }
    }

    private fun Community.toPresentation(id: Long): Post = Post(
        id = id,
        author = this.userName,
        authorImage = "https://picsum.photos/seed/${this.userName}/200",
        timeAgo = calculateTimeAgo(this.create_at),
        content = this.content,
        image = this.imageResList?.firstOrNull()?.let { "https://picsum.photos/seed/$id/400/300" },
        likes = this.likes,
        comments = this.comments?.size ?: 0,
        isLiked = false,
        category = this.category,
        createdAt = this.create_at
    )

}