package com.rohanbojja.xlsxjson.models;

import java.util.List;

public class Response {
    List<String> file_names;
    String result_directory;
    Boolean prettyPrinting;
    String errorMessage;

    public Response(List<String> file_names, String result_directory, Boolean prettyPrinting, String errorMessage) {
        this.file_names = file_names;
        this.result_directory = result_directory;
        this.prettyPrinting = prettyPrinting;
        this.errorMessage = errorMessage;
    }

    public List<String> getFile_names() {
        return file_names;
    }

    public void setFile_names(List<String> file_names) {
        this.file_names = file_names;
    }

    public String getResult_directory() {
        return result_directory;
    }

    public void setResult_directory(String result_directory) {
        this.result_directory = result_directory;
    }

    public Boolean getPrettyPrinting() {
        return prettyPrinting;
    }

    public void setPrettyPrinting(Boolean prettyPrinting) {
        this.prettyPrinting = prettyPrinting;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
