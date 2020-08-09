package com.rohanbojja.xlsxjson.controllers;

import com.rohanbojja.xlsxjson.models.Request;
import com.rohanbojja.xlsxjson.models.Response;
import com.rohanbojja.xlsxjson.services.XlsxToJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ToJsonController {

    @Autowired
    XlsxToJsonService xlsxToJsonService;

    @PostMapping(value = "/convert", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> convert(@RequestBody final Request request) {
        String filePath = request.getFile_path();
        String fileName = request.getFile_name();
        Boolean prettyPrinting = request.getPrettyPrinting();
        String outputDirectory = request.getResult_directory();
        List<String> outputFiles = xlsxToJsonService.convert(filePath + "/" + fileName, outputDirectory, prettyPrinting);
        Response response = new Response(outputFiles, outputDirectory, prettyPrinting);
        ResponseEntity<Response> responseEntity;
        if (outputFiles.size() > 0) {

            responseEntity = ResponseEntity.ok()
                    .body(response);
        } else {
            responseEntity = ResponseEntity.badRequest()
                    .build();
        }
        return responseEntity;
    }
}
