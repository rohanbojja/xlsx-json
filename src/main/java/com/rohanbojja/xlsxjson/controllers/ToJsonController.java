package com.rohanbojja.xlsxjson.controllers;


import com.google.gson.Gson;
import com.rohanbojja.xlsxjson.models.Request;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonBuilderCustomizer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

@RestController
public class ToJsonController {

    @Autowired
    Gson gson;

    @PostMapping(value = "/convert", consumes = "application/json", produces = "application/json")
    public String convert(@RequestBody final Request request) {
        String filePath = request.getFile_path();
        String fileName = request.getFile_name();
        List <String> jsonSheet = new ArrayList<>(); //jsonSheet == Excel Sheet
        Map<String,String> excelEntry  = new HashMap<>(); // excelEntry == Excel Row
        ArrayList<String> headers = new ArrayList<>();
        try {
            FileInputStream xlsxFile = new FileInputStream(filePath + "/" + fileName); // C:/test/input.xlsx
            XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile);
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++) {
                jsonSheet = new ArrayList<>();
                XSSFSheet sheet = workbook.getSheetAt(sheetNumber);
                String jsonFileName = sheet.getSheetName();
                Iterator<Row> rows = sheet.iterator();
                //Iterating through the Excel sheet's rows
                if(rows.hasNext()) {
                    Row row = rows.next();
                    Iterator<Cell> cells = row.cellIterator();
                    while (cells.hasNext()) {
                        Cell cell = cells.next();
                        String header = cell.getStringCellValue();
                        if(header.length()>0){
                            headers.add(header);
                        }
                    }
                }

                while (rows.hasNext()) {
                    Row row = rows.next();
                    excelEntry =  new HashMap<>();
                    Iterator<Cell> cells = row.cellIterator();
                    while (cells.hasNext()) {
                        Cell cell = cells.next();
                        switch (cell.getCellType()){
                            case STRING:
                                String cellValue = cell.getStringCellValue();
                                if(cellValue.length()>0){
                                    excelEntry.put(headers.get(cell.getColumnIndex()),cellValue);
                                }

                                break;
                            case NUMERIC:
                                String cellValue2 = Double.valueOf(cell.getNumericCellValue()).toString();
                                if(cellValue2.length()>0){
                                    excelEntry.put(headers.get(cell.getColumnIndex()),cellValue2);
                                }
                                break;
                        }
                    }
                    //Json entry contains a single sheet row
                    //Convert to Json using GSON
                    String jsonEntry = gson.toJson(excelEntry);
                    //Add rows to build up the sheet
                    jsonSheet.add(jsonEntry);// Collection of rows
                }
                //Write to file here

                //Check if directory exists

                File directory = new File(request.getResult_directory().toString());
                if(!directory.exists()){
                    Boolean createdDirectory = directory.mkdir();
                }
                String outputFile = request.getResult_directory()+"/"+jsonFileName+".json";
                File file = new File(outputFile);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonSheet.toString());
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return jsonSheet.toString();
    }
}
