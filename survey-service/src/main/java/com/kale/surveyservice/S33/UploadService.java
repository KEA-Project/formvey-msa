package com.kale.surveyservice.S33;


import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;

public interface UploadService {

    void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName);

    //String getFileUrl(String fileName);
   String getFileUrl(String fileName);

}