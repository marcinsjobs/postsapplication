package com.apzumi.postsdataapplication;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Repository
public class PostDataDao implements PostDataDaoInterface {

    private final String DB_PATH = "jdbc:sqlite:" +
            System.getProperty("user.dir") + "\\src\\main\\resources\\databases\\posts.db";
    private final int QUERY_TIMEOUT = 20;
    private final String USERID_COLUMN = "userId";
    private final String ID_COLUMN = "id";
    private final String TITLE_COLUMN = "title";
    private final String BODY_COLUMN = "body";
    private static final Logger LOGGER = LoggerFactory.getLogger(PostDataDao.class);

    public PostDataDao() {
    }

    /**
     * Selects all the records from the local database
     * @return posts read from the database as a PostDataJsonResponse with an ApiError status.
     */
    @Override
    public PostDataJsonResponse getPostsFromDb() {
        Connection connection;
        ApiError status = new ApiError();
        List<PostData> postDataList = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(DB_PATH);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(QUERY_TIMEOUT);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM posts");
            while (resultSet.next()) {
                postDataList.add(new PostData(
                        resultSet.getInt(USERID_COLUMN),
                        resultSet.getInt(ID_COLUMN),
                        resultSet.getString(TITLE_COLUMN),
                        resultSet.getString(BODY_COLUMN)
                ));
            }
        }
        catch (SQLException e) {
            LOGGER.info(e.getMessage());
            status.code = e.getErrorCode();
            status.message = e.getMessage();
        }

        return new PostDataJsonResponse(postDataList, status);
    }

    /**
     * Saves PostData object as a record in a database.
     * @param postData - PostData object to be saved in a database.
     * @return - ApiError status of the operation.
     */
    @Override
    public ApiError savePost(PostData postData) {
        Connection connection;
        ApiError status = new ApiError();
        try {
            connection = DriverManager.getConnection(DB_PATH);

            PreparedStatement updatePostData = connection.prepareStatement("replace into posts values(?,?,?,?)");
            updatePostData.setInt(1, postData.getUserId());
            updatePostData.setInt(2, postData.getId());
            updatePostData.setString(3, postData.getTitle());
            updatePostData.setString(4, postData.getBody());
            updatePostData.executeUpdate();
        }
        catch (SQLException e) {
            LOGGER.info(e.getMessage());
            status.code = e.getErrorCode();
            status.message = e.getMessage();
        }

        return status;
    }

    /**
     * Updates PostData record in a database, according to the PostData object id.
     * @param postData - Object to be updated.
     * @return - ApiError status of the operation.
     */
    @Override
    public ApiError updatePost(PostData postData) {
        Connection connection;
        ApiError status = new ApiError();
        try {
            connection = DriverManager.getConnection(DB_PATH);

            PreparedStatement updatePostData = connection.prepareStatement(
                    "update posts SET " +
                            "userId= ?, title = ?, body = ?" +
                            "where id = ?"
            );

            updatePostData.setInt(1, postData.getUserId());
            updatePostData.setString(2, postData.getTitle());
            updatePostData.setString(3, postData.getBody());
            updatePostData.setInt(4, postData.getId());
            connection.close();
            throw new SQLException();
        }
        catch (SQLException e) {
            LOGGER.info(e.getMessage());
            status.code = e.getErrorCode();
            status.message = e.getMessage();
        }

        return (status);
    }

    /**
     * Deletes PostData record from the database, according to the id.
     * @param id - id of the record to be deleted.
     * @return - ApiError status of the operation.
     */
    @Override
    public ApiError deletePost(int id) {
        Connection connection;
        ApiError status = new ApiError();
        try {
            connection = DriverManager.getConnection(DB_PATH);

            PreparedStatement updatePostData = connection.prepareStatement(
                    "delete from posts where id = ?"
            );
            updatePostData.setInt(1, id);
            updatePostData.executeUpdate();
        }
        catch (SQLException e) {
            LOGGER.info(e.getMessage());
            status.code = e.getErrorCode();
            status.message = e.getMessage();
        }

        return status;
    }
}
