package com.rohanbojja.xlsxjson.services;

import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class XlsxToJsonService {
    @Autowired
    Gson gson;

    public List<String> convert(String inputFile, String outputDirectory, Boolean prettyPrinting) {
        ArrayList<String> convertedFiles = new ArrayList<>();
        try {
            FileInputStream xlsxFile = new FileInputStream(inputFile); // C:/test/input.xlsx
            XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile);
            int numberOfSheets = workbook.getNumberOfSheets();

            //Iterate through each excel sheet
            for (int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++) {
                List<String> jsonSheet = new ArrayList<>();
                XSSFSheet sheet = workbook.getSheetAt(sheetNumber);
                String jsonFileName = sheet.getSheetName();
                Iterator<Row> rows = sheet.iterator();

                //Get the first row and add the entires to the headers list
                ArrayList<String> headers = new ArrayList<>();
                if (rows.hasNext()) {
                    Row row = rows.next();
                    Iterator<Cell> cells = row.cellIterator();
                    while (cells.hasNext()) {
                        Cell cell = cells.next();
                        String header = cell.getStringCellValue();
                        if (header.length() > 0) {
                            headers.add(header);
                        }
                    }
                }

                //Iterating through the Excel sheet's rows
                while (rows.hasNext()) {
                    Map<String, String> excelEntry = new HashMap<>(); //excelEntry contains a single row of the sheet
                    Row row = rows.next();
                    Iterator<Cell> cells = row.cellIterator();
                    while (cells.hasNext()) {
                        Cell cell = cells.next();
                        switch (cell.getCellType()) {
                            case STRING:
                                String cellValue = cell.getStringCellValue();
                                if (cellValue.length() > 0) {
                                    excelEntry.put(headers.get(cell.getColumnIndex()), cellValue);
                                }

                                break;
                            case NUMERIC:
                                String cellValue2 = Double.valueOf(cell.getNumericCellValue()).toString();
                                if (cellValue2.length() > 0) {
                                    excelEntry.put(headers.get(cell.getColumnIndex()), cellValue2);
                                }
                                break;
                        }
                    }
                    String jsonEntry;
                    //Converting to Json using GSON
                    if (prettyPrinting) {
                        Gson prettyGson = gson.newBuilder().setPrettyPrinting().create();
                        jsonEntry = prettyGson.toJson(excelEntry);
                    } else {
                        jsonEntry = gson.toJson(excelEntry);
                    }
                    //Add rows to build up the sheet
                    jsonSheet.add(jsonEntry); //The sheet
                }
                //Write to file here

                //Check if directory exists

                File directory = new File(outputDirectory);
                if (!directory.exists()) {
                    Boolean createdDirectory = directory.mkdir();
                }
                String outputFile = outputDirectory + "/" + jsonFileName + ".json";
                convertedFiles.add(jsonFileName + ".json");
                File file = new File(outputFile);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonSheet.toString());
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFiles;
    }
}
