package com.apzumi.postsdataapplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ApplicationTests {

	private final String URL = "https://jsonplaceholder.typicode.com/posts";

	@Autowired
	private RestTemplate restTemplate;
	private PostDataController controller;
	private PostDataService postDataService;

	@Mock
	private PostDataDao mockPostDataDao;

	private MockRestServiceServer mockServer;
	private ObjectMapper objectMapper = new ObjectMapper();


	@BeforeEach
	public void setup() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	void getPostsFromApiSuccess() {
		PostData postData1 = new PostData(1, 1, "title", "body");
		PostData postData2 = new PostData(5, 9, "custom title", "custom body");
		PostData postData3 = new PostData(100, 100, "ęśąćż", "ęśąćż");
		ApiError status = new ApiError();
		List<PostData> postDataList = new ArrayList<>();
		postDataList.add(postData1);
		postDataList.add(postData2);
		postDataList.add(postData3);
		PostDataJsonResponse expectedResponse = new PostDataJsonResponse(postDataList, status);

		PostData[] postDataArray = new PostData[postDataList.size()];
		postDataArray = postDataList.toArray(postDataArray);

		try {
            mockServer.expect(ExpectedCount.once(), requestTo(URL))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(objectMapper.writeValueAsBytes(postDataArray)));
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

		postDataService = new PostDataService();
		postDataService.setRestTemplate(restTemplate);
		controller = new PostDataController(postDataService);

		PostDataJsonResponse actualResponse = controller.getPostsFromApi();

		assertEquals(expectedResponse.getPostDataList(), actualResponse.getPostDataList());
		assertEquals(expectedResponse.getApiError().code, actualResponse.getApiError().code);
		assertEquals(expectedResponse.getApiError().message, actualResponse.getApiError().message);
	}

	@Test
	void getPostsFromApiNotFound() {
		ApiError status = new ApiError();
		status.code = 404;
		status.message = "404 Not Found: [[]]";
		List<PostData> postDataList = new ArrayList<>();
		PostDataJsonResponse expectedResponse = new PostDataJsonResponse(postDataList, status);

		PostData[] postDataArray = new PostData[postDataList.size()];
		postDataArray = postDataList.toArray(postDataArray);

		try {
			mockServer.expect(ExpectedCount.once(), requestTo(URL))
					.andRespond(withStatus(HttpStatus.NOT_FOUND)
							.contentType(MediaType.APPLICATION_JSON)
							.body(objectMapper.writeValueAsBytes(postDataArray)));
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
		}

		postDataService = new PostDataService();
		postDataService.setRestTemplate(restTemplate);
		controller = new PostDataController(postDataService);

		PostDataJsonResponse actualResponse = controller.getPostsFromApi();

		assertEquals(expectedResponse.getPostDataList(), actualResponse.getPostDataList());
		assertEquals(expectedResponse.getApiError().code, actualResponse.getApiError().code);
		assertEquals(expectedResponse.getApiError().message, actualResponse.getApiError().message);
	}

	@Test
	void getPostsFromDaoSuccess() {

		String filterTitle = "";
		boolean skipUserId = false;

		PostData postData1 = new PostData(9999, 99, "newtitle", "newbody");
		PostData postData2 = new PostData(5, 9, "testing test ! @", ")(*&^%$#@!");
		PostData postData3 = new PostData(100, 100, "ęśąćż", "ęśąćż");
		ApiError status = new ApiError();
		List<PostData> postDataList = new ArrayList<>();

		postDataList.add(postData1);
		postDataList.add(postData2);
		postDataList.add(postData3);
		PostDataJsonResponse expectedResponse = new PostDataJsonResponse(postDataList, status);

		Mockito.when(mockPostDataDao.getPostsFromDb()).thenReturn(expectedResponse);

		postDataService = new PostDataService();
		postDataService.setPostDataDao(mockPostDataDao);
		controller = new PostDataController(postDataService);
		PostDataJsonResponse actualResponse = controller.getPostsFromDB(filterTitle, skipUserId);

		assertEquals(expectedResponse.getPostDataList(), actualResponse.getPostDataList());
		assertEquals(expectedResponse.getApiError().code, actualResponse.getApiError().code);
		assertEquals(expectedResponse.getApiError().message, actualResponse.getApiError().message);
	}

	@Test
	void getPostsFromDaoAscendingSuccess() {

		String filterTitle = "ascending";
		boolean skipUserId = false;

		PostData postData1 = new PostData(9999, 99, "newtitle", "newbody");
		PostData postData2 = new PostData(5, 9, "testing test ! @", ")(*&^%$#@!");
		PostData postData3 = new PostData(100, 100, "ęśąćż", "ęśąćż");
		PostData postData4 = new PostData(43, 12, "abcde", "abcde");
		ApiError status = new ApiError();
		List<PostData> postDataList = new ArrayList<>();

		postDataList.add(postData1);
		postDataList.add(postData2);
		postDataList.add(postData3);
		postDataList.add(postData4);
		PostDataJsonResponse expectedResponse = new PostDataJsonResponse(postDataList, status);

		Mockito.when(mockPostDataDao.getPostsFromDb()).thenReturn(expectedResponse);

		postDataService = new PostDataService();
		postDataService.setPostDataDao(mockPostDataDao);
		controller = new PostDataController(postDataService);
		PostDataJsonResponse actualResponse = controller.getPostsFromDB(filterTitle, skipUserId);

		assertEquals(expectedResponse.getPostDataList(), actualResponse.getPostDataList());
		assertEquals(expectedResponse.getApiError().code, actualResponse.getApiError().code);
		assertEquals(expectedResponse.getApiError().message, actualResponse.getApiError().message);
	}

	@Test
	void getPostsFromDaoDescendingSuccess() {

		String filterTitle = "descending";
		boolean skipUserId = false;

		PostData postData1 = new PostData(9999, 99, "newtitle", "newbody");
		PostData postData2 = new PostData(5, 9, "testing test ! @", ")(*&^%$#@!");
		PostData postData3 = new PostData(100, 100, "ęśąćż", "ęśąćż");
		PostData postData4 = new PostData(43, 12, "abcde", "abcde");
		ApiError status = new ApiError();
		List<PostData> postDataList = new ArrayList<>();

		postDataList.add(postData1);
		postDataList.add(postData2);
		postDataList.add(postData3);
		postDataList.add(postData4);
		PostDataJsonResponse expectedResponse = new PostDataJsonResponse(postDataList, status);

		Mockito.when(mockPostDataDao.getPostsFromDb()).thenReturn(expectedResponse);

		postDataService = new PostDataService();
		postDataService.setPostDataDao(mockPostDataDao);
		controller = new PostDataController(postDataService);
		PostDataJsonResponse actualResponse = controller.getPostsFromDB(filterTitle, skipUserId);

		assertEquals(expectedResponse.getPostDataList(), actualResponse.getPostDataList());
		assertEquals(expectedResponse.getApiError().code, actualResponse.getApiError().code);
		assertEquals(expectedResponse.getApiError().message, actualResponse.getApiError().message);
	}

	@Test
	void getPostsFromDaoSqlError() {

		String filterTitle = "";
		boolean skipUserId = false;

		ApiError status = new ApiError(1, "SQL Error.");
		List<PostData> postDataList = new ArrayList<>();

		PostDataJsonResponse expectedResponse = new PostDataJsonResponse(postDataList, status);

		Mockito.when(mockPostDataDao.getPostsFromDb()).thenReturn(expectedResponse);

		postDataService = new PostDataService();
		postDataService.setPostDataDao(mockPostDataDao);
		controller = new PostDataController(postDataService);
		PostDataJsonResponse actualResponse = controller.getPostsFromDB(filterTitle, skipUserId);

		assertEquals(expectedResponse.getPostDataList(), actualResponse.getPostDataList());
		assertEquals(expectedResponse.getApiError().code, actualResponse.getApiError().code);
		assertEquals(expectedResponse.getApiError().message, actualResponse.getApiError().message);
	}

	@Test
	void updateAllThePostsFromApiToDbSuccess() {

		PostData postData1 = new PostData(1, 1, "title", "body");
		PostData postData2 = new PostData(5, 9, "custom title", "custom body");
		PostData postData3 = new PostData(100, 100, "ęśąćż", "ęśąćż");
		ApiError status = new ApiError();
		List<PostData> postDataList = new ArrayList<>();
		postDataList.add(postData1);
		postDataList.add(postData2);
		postDataList.add(postData3);

		PostData[] postDataArray = new PostData[postDataList.size()];
		postDataArray = postDataList.toArray(postDataArray);

		try {
			mockServer.expect(ExpectedCount.once(), requestTo(URL))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(objectMapper.writeValueAsBytes(postDataArray)));
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
		}

		Mockito.when(mockPostDataDao.updatePost(postData1)).thenReturn(status);

		postDataService = new PostDataService();
		postDataService.setRestTemplate(restTemplate);
		postDataService.setPostDataDao(mockPostDataDao);
		controller = new PostDataController(postDataService);

		ArgumentCaptor<PostData> argument = ArgumentCaptor.forClass(PostData.class);

		controller.updatePosts();
		Mockito.verify(mockPostDataDao).updatePost(postData1);
		Mockito.verify(mockPostDataDao).updatePost(postData2);
		Mockito.verify(mockPostDataDao).updatePost(postData3);
	}

	@Test
	void deletePostSuccess() {

		int id = 10;
		ApiError status = new ApiError();
		Mockito.when(mockPostDataDao.deletePost(id)).thenReturn(status);

		postDataService = new PostDataService();
		postDataService.setRestTemplate(restTemplate);
		postDataService.setPostDataDao(mockPostDataDao);
		controller = new PostDataController(postDataService);

		ArgumentCaptor<PostData> argument = ArgumentCaptor.forClass(PostData.class);

		controller.deletePost(id);
		Mockito.verify(mockPostDataDao).deletePost(id);
	}

}
