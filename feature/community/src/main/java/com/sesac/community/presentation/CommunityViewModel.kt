package com.sesac.community.presentation

import androidx.lifecycle.ViewModel
import com.sesac.domain.model.post.PostModel
import com.sesac.domain.usecase.post.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase,
): ViewModel() {
    private val _postListState = MutableStateFlow<List<PostModel>>(emptyList())
    val postListState: StateFlow<List<PostModel>> get() = _postListState.asStateFlow()

    suspend fun requestPostList() {
        coroutineScope {
            launch {
                getAllPostsUseCase.invoke().collect {
                    _postListState.value = it
                }
            }
        }
    }
}