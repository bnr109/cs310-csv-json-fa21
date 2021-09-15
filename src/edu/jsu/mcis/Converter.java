package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String jsonString = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll(); // Reads all data into list
            Iterator<String[]> iterator = full.iterator(); // Iterates through each piece
            
            JSONArray columnHeadings = new JSONArray(); // Container for column headings
            JSONArray rowHeadings = new JSONArray(); // container for row headings
            JSONArray data = new JSONArray();
            JSONObject jsonObject = new JSONObject(); // Container for individual records
            String[] rows;
            //String jsonString = "";
           
            columnHeadings.addAll(Arrays.asList(iterator.next())); // gets header row
            jsonObject.put("colHeaders", columnHeadings);
            while (iterator.hasNext()) { // Iterate through all records
                JSONArray row = new JSONArray(); //Container for rows
                rows = iterator.next();
                rowHeadings.add(rows[0]); // row headers
                for (int i = 1; i < rows.length; ++i) {
                    row.add(Integer.parseInt(rows[i]));
                }
                data.add(row);
            }
            jsonObject.put("rowHeaders", rowHeadings);
            jsonObject.put("data", data);
            
            jsonString = JSONValue.toJSONString(jsonObject);
            //System.out.println(jsonString);
            
        }        
        catch(Exception e) { e.printStackTrace(); }
        
        return jsonString.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");
           
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            JSONArray columnHeadings = (JSONArray) jsonObject.get("colHeaders");// get column header array
            JSONArray rowHeadings = (JSONArray) jsonObject.get("rowHeaders"); // get row header array
            JSONArray data = (JSONArray) jsonObject.get("data"); // get data array

           //JSONObject records = new JSONObject();
           ArrayList<String[]> csvData = new ArrayList<>(); //container for all elements
           
           // Header Row
           
           String[] colHeaders = new String[columnHeadings.size()];
           
           for (int i = 0; i < columnHeadings.size(); ++i) {
               colHeaders[i] = (String)columnHeadings.get(i);
           }
           
           csvData.add(colHeaders);
           
           // Data Rows
           
           for (int i = 0; i < rowHeadings.size(); ++i) {
               
               JSONArray dataRow = (JSONArray)data.get(i);
               String[] row = new String[dataRow.size() + 1];
               row[0] = (String)rowHeadings.get(i);
               
               for (int j = 0; j < dataRow.size(); ++j) {
                   row[j+1] = String.valueOf(dataRow.get(j));
               }
               
               csvData.add(row);
               
           }
           
            
            /*// Create Lists
            List<String> colHeadList = new ArrayList<String>();
                for(int i = 0; i < columnHeadings.size(); i++){
                    colHeadList.add((String)columnHeadings.get(i));
                }
            List<String> rowHeadList = new ArrayList<String>();
                for(int i = 0; i < rowHeadings.size(); i++){
                    rowHeadList.add((String)rowHeadings.get(i));
                }
            
                //String.valueOf()
            List<String> dataList = new ArrayList<String>();
                for (int i = 0; i < data.size(); i++) {
                    dataList.add((String)data.get(i));
                }
                
            // Create Arrays
            int size = colHeadList.size();
            String[] colHeadArray
            = colHeadList.toArray(new String[size]);
            
            int size1 = rowHeadList.size();
            String[] rowHeadArray
            = rowHeadList.toArray(new String[size1]);
            
            int size2 = data.size(); //array for data column
            String[] dataArray
            = new String[size2];
            
            String[] rowData; // array for the data within the rows
            
            // Iterate through column headings, add to the array
            for (int i = 0; i < columnHeadings.size(); ++i) { 
                colHeadArray[i] = (String) columnHeadings.get(i);
                }
            
            // Iterate through row headings and data, add to their arrays
            for (int i = 0; i < rowHeadings.size(); ++i){
                rowHeadArray[i] = (String) rowHeadings.get(i);
                //dataArray[i] = (String) data.get(i);
            }
            

            //csvWriter.writeNext(colHeadArray);
            
            for (int i = 0; i < dataArray.length; ++i){
                String[] rowElements = dataArray[i].split(",");
                rowData = new String[rowElements.length+1];
                rowData[0] = rowHeadArray[i];
                for(int j = 0; i < rowHeadArray.length; ++j){
                    rowData[j] = rowHeadArray[j];
                }
                csvWriter.writeNext(rowData);
            }
            //for(String s: colHeadArray)
            //System.out.println(colHeadArray);*/
            
           csvWriter.writeAll(csvData);
           results = writer.toString();
           
        }
        
        catch(Exception e) { e.printStackTrace(); }
        
        return results.trim();
        
    }

}