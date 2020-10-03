package com.apzumi.postsdataapplication;

public interface PostDataDaoInterface {

    PostDataJsonResponse getPostsFromDb();
    ApiError savePost(PostData postData);
    ApiError updatePost(PostData postData);
    ApiError deletePost(int id);
}
