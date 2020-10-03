package com.apzumi.postsdataapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostDataController {

    private PostDataService postDataService;

    @Autowired
    public PostDataController(PostDataService postDataService) {
        this.postDataService = postDataService;
    }

    /**
     * @return all the posts read from the API and ApiError status.
     */
    @GetMapping("/api/posts")
    public PostDataJsonResponse getPostsFromApi() {
        return postDataService.getPostsFromApi();
    }

    /**
     *
     * @param filterTitle - takes "ascending" or "descending" String,
     *        can be skipped to preserve the order of the posts as in the database.
     * @param skipUserId - true if you want to omit the userId from the database records.
     * @return posts read from the database as a list with the optional title filter and skipped userId.
     */
    @GetMapping("/db/posts")
    public PostDataJsonResponse getPostsFromDB(
            @RequestParam(required = false, defaultValue = "") String filterTitle,
            @RequestParam(required = false) boolean skipUserId) {
        return postDataService.getPostsFromDb(filterTitle, skipUserId);
    }

    /**
     * Used to populate empty database with the records from the API.
     */
    @GetMapping("/db/populate")
    public ApiError savePost() { return postDataService.savePostsFromApiToDb();}

    /**
     * Used to update single post in the database.
     * @param postData - PostData object to update.
     * @return - ApiError status of the operation.
     */
    @PutMapping("/db/updatepost")
    public ApiError updatePost(@RequestBody PostData postData) {
        return postDataService.updatePost(postData);
    }

    /**
     * Used to update all the database posts from the API.
     * @return - ApiError status of the operation.
     */
    @GetMapping("/db/updateposts")
    public ApiError updatePosts() {
        return postDataService.updatePostsFromApiToDb();
    }

    /**
     * Used to delete database post of the given id.
     * @param id - id of the PostData record to delete.
     * @return - ApiError status of the operation.
     */
    @DeleteMapping("/db/posts/{id}")
    public ApiError deletePost(@PathVariable int id) {
        return postDataService.deletePost(id);
    }
}
