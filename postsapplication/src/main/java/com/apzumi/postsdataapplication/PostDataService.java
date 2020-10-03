package com.apzumi.postsdataapplication;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PostDataService {

    private final String URL = "https://jsonplaceholder.typicode.com/posts";
    private static final Logger LOGGER = LoggerFactory.getLogger(PostDataService.class);

    private PostDataDao postDataDao;
    private RestTemplate restTemplate;

    /**
     * Makes a get request to the API described in URL.
     * @return all the posts read from the API.
     */
    public PostDataJsonResponse getPostsFromApi() {

        List<PostData> postDataList = new ArrayList<>();
        ApiError status = new ApiError();
        try {
            ResponseEntity<PostData[]> response = restTemplate.getForEntity(URL, PostData[].class);
            postDataList = Arrays.asList(response.getBody());
        } catch (HttpStatusCodeException e) {
            LOGGER.info(e.getMessage());
            status.code = e.getStatusCode().value();
            status.message = e.getMessage();
        }
        return new PostDataJsonResponse(postDataList, status);
    }

    /**
     * Selects all the records from the local database
     * @param filterTitle - takes "ascending" or "descending" String,
     *        can be skipped to preserve the order of the posts as in the database.
     * @param skipUserId - true if you want to omit the userId from the database records.
     * @return posts read from the database as a PostDataJsonResponse with the optional title filter,
     * skipped userId and ApiError status.
     */
    public PostDataJsonResponse getPostsFromDb(
            String filterTitle,
            boolean skipUserId) {

        PostDataJsonResponse postDataJsonResponse = postDataDao.getPostsFromDb();
        List<PostData> postDataList = postDataJsonResponse.getPostDataList();

        /**
         * when the skipUserId flag is true, all the userId will be set to default value.
         * this allows JsonInclude to ignore it's value when presenting data.
         */
        postDataList
                .stream()
                .forEach(postData -> {
                    if(skipUserId) {postData.setUserId(0);}
                });

        postDataJsonResponse.setPostDataList(postDataList.stream()
                    .sorted((PostData pd1, PostData pd2) -> {
                        if (filterTitle.equals("ascending")) {
                            return pd1.getTitle().compareTo(pd2.getTitle());
                        } else if (filterTitle.equals("descending")) {
                            return pd2.getTitle().compareTo(pd1.getTitle());
                        } else
                            return 0;
                    })
                    .collect(Collectors.toList()));

        return(postDataJsonResponse);
    }

    /**
     * Saves PostData object as a record in a database.
     * @param postData - PostData object to be saved in a database.
     * @return - ApiError status of the operation.
     */
    public ApiError savePost(PostData postData) {
        return postDataDao.savePost(postData);
    }

    /**
     * Saves all the PostData objects from the URL to a database.
     * @return - ApiError status of the operation.
     */
    public ApiError savePostsFromApiToDb() {

        PostDataJsonResponse postDataJsonResponse = getPostsFromApi();
        if(postDataJsonResponse.getApiError().code == 200) {
            for (PostData postData : postDataJsonResponse.getPostDataList()) {
                postDataJsonResponse.setApiError(savePost(postData));
            }
        }

        return postDataJsonResponse.getApiError();
    }

    /**
     * Updates PostData record in a database, according to the PostData object id.
     * @param postData - Object to be updated.
     * @return - ApiError status of the operation.
     */
    public ApiError updatePost(PostData postData) {
        return postDataDao.updatePost(postData);
    }

    /**
     * Updates posts from the URL to a database. Does not add the new records.
     * @return - ApiError status of the operation.
     */
    public ApiError updatePostsFromApiToDb() {

        PostDataJsonResponse postDataJsonResponse = getPostsFromApi();
        if(postDataJsonResponse.getApiError().code == 200) {
            for (PostData postData : postDataJsonResponse.getPostDataList()) {
                postDataJsonResponse.setApiError(updatePost(postData));
            }
        }

        return postDataJsonResponse.getApiError();
    }

    /**
     * Deletes PostData record from the database, according to the id.
     * @param id - id of the record to be deleted.
     * @return - ApiError status of the operation.
     */
    public ApiError deletePost(int id) { return postDataDao.deletePost(id);}

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setPostDataDao(PostDataDao postDataDao) {
        this.postDataDao = postDataDao;
    }
}
