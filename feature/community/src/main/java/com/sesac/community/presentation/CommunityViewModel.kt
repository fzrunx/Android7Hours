package com.sesac.community.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

// 1. 데이터 모델
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
    val category: String
)

// 2. 초기 데이터 소스
object DataSource {
    val initialPosts = listOf(
        Post(1, "멍멍이집사", "https://...", "2시간 전", "오늘 한강공원에서 산책하다가 만난 친구들! ...", "https://...", 124, 18, false, "산책후기"),
        Post(2, "강아지사랑", "https://...", "5시간 전", "서울숲 산책로 추천해요! ...", "https://...", 89, 12, true, "정보공유"),
        Post(3, "댕댕이랑", "https://...", "1일 전", "올림픽공원에서 만난 귀여운 친구 🥰 ...", "https://...", 203, 34, false, "산책후기"),
        Post(4, "산책러버", "https://...", "2일 전", "오늘의 산책 코스! ...", "https://...", 156, 21, true, "질문")
    )
}

// 3. ViewModel
class CommunityViewModel : ViewModel() {

    // --- 상태 (State) ---

    // 원본 게시물 리스트
    private val _posts = MutableStateFlow(DataSource.initialPosts)

    // 검색어
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 활성 필터
    private val _activeFilter = MutableStateFlow("전체")
    val activeFilter = _activeFilter.asStateFlow()

    // 검색 및 필터링이 적용된 최종 게시물 리스트 (React의 getFilteredPosts)
    val filteredPosts: StateFlow<List<Post>> = combine(
        _posts,
        _searchQuery,
        _activeFilter
    ) { posts, query, filter ->
        var filtered = posts

        // 카테고리 필터
        if (filter != "전체") {
            filtered = if (filter == "인기글") {
                filtered.filter { it.likes > 100 }
            } else {
                filtered.filter { it.category == filter }
            }
        }

        // 검색어 필터
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
        initialValue = DataSource.initialPosts
    )

    // --- 이벤트 핸들러 (Actions) ---

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChange(filter: String) {
        _activeFilter.value = filter
    }

    // React의 handleLike
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

    // React의 handleCreatePost
    fun createPost(content: String, image: String?, category: String) {
        val newPost = Post(
            id = System.currentTimeMillis(),
            author = "나", // TODO: 실제 사용자 정보로 대체
            authorImage = "https://...", // TODO: 실제 사용자 프로필로 대체
            timeAgo = "방금 전",
            content = content,
            image = image.takeIf { !it.isNullOrBlank() },
            likes = 0,
            comments = 0,
            isLiked = false,
            category = category
        )
        _posts.update { currentPosts ->
            listOf(newPost) + currentPosts
        }
    }

    // React의 handleUpdatePost
    fun updatePost(updatedPost: Post) {
        _posts.update { currentPosts ->
            currentPosts.map { post ->
                if (post.id == updatedPost.id) {
                    updatedPost
                } else {
                    post
                }
            }
        }
    }

    // React의 handleDeletePost
    fun deletePost(postId: Long) {
        _posts.update { currentPosts ->
            currentPosts.filterNot { it.id == postId }
        }
    }
}