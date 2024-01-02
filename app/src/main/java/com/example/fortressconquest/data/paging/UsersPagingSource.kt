package com.example.fortressconquest.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.fortressconquest.domain.model.User
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersPagingSource @Inject constructor(
    private val query: Query
): PagingSource<Query, User>() {

    override fun getRefreshKey(state: PagingState<Query, User>): Query? = null

    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, User> {
        return try {
            val currentQuery = (params.key ?: query).limit(params.loadSize.toLong())
            val response = currentQuery.get().await()
            val lastVisibleUser = response.documents.last()

            LoadResult.Page(
                data = response.toObjects(User::class.java),
                prevKey = null,
                nextKey = query.startAfter(lastVisibleUser)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}