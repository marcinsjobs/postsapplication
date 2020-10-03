package com.apzumi.postsdataapplication;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostDataJsonResponse {

    private List<PostData> postDataList;
    private ApiError apiError;
}
