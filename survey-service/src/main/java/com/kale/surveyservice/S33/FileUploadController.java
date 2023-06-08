package com.kale.surveyservice.S33;

import com.kale.surveyservice.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/survey-service/images")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @ResponseBody
    @PostMapping("/upload")
    //public String uploadImage(@RequestPart MultipartFile file) {
    public BaseResponse<String> uploadImage(@RequestPart MultipartFile file) {

        String result = fileUploadService.uploadImage(file);
        return new BaseResponse<>(result);

    }

}