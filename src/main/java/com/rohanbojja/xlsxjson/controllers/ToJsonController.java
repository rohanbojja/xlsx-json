package com.rohanbojja.xlsxjson.controllers;

import com.rohanbojja.xlsxjson.models.Request;
import com.rohanbojja.xlsxjson.models.Response;
import com.rohanbojja.xlsxjson.services.XlsxToJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
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
        ResponseEntity<Response> responseEntity;
        List<String> outputFiles;
        Response response;
        try {
            outputFiles = xlsxToJsonService.convert(filePath + "/" + fileName, outputDirectory, prettyPrinting);
            response = new Response(outputFiles, outputDirectory, prettyPrinting,"");
            responseEntity = ResponseEntity.ok()
                    .body(response);
        } catch (IOException e) {
            e.printStackTrace();
            outputFiles = new ArrayList<>();
            response = new Response(outputFiles, outputDirectory, prettyPrinting,e.getMessage());
            responseEntity = ResponseEntity.badRequest()
                    .body(response);
        }
        return responseEntity;
    }
}
