package com.sexed.androidapp.app;

import com.sexed.androidapp.model.Media;
import com.sexed.androidapp.model.Post;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts/{id}")
    Call<Post> getPostById(@Path("id") int postId);

    @GET("media/{featured_media}")
    Call<Media> getPostThumbnail(@Path("featured_media") int media);


}
