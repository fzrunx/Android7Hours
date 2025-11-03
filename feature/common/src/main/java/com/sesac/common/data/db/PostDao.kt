package com.sesac.common.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sesac.common.data.model.post.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM post ORDER BY create_at DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM post WHERE post_id = :postId")
    fun getPostById(postId: Int): Flow<PostEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Query("DELETE FROM post WHERE post_id = :postId")
    suspend fun deletePost(postId: Int)

    @Query("SELECT * FROM post WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY create_at DESC")
    fun searchPosts(query: String): Flow<List<PostEntity>>
}
