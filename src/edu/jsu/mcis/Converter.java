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
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE
            
            String[] headings = iterator.next();           
                      
            LinkedHashMap<String, JSONArray> jsonObject = new LinkedHashMap<>();
            String[] record;
            
            JSONArray rowHeaders = new JSONArray();
            JSONArray colHeaders = new JSONArray();
            JSONArray data; //will hold each line
            JSONArray allData = new JSONArray(); //Will hold all data in all lines
            
            for(int i = 0; i < headings.length; i++) //store all the colHeadings
            {
                colHeaders.add(headings[i]);
            }
            
            while (iterator.hasNext()) { // Iterate through all records                
                data = new JSONArray(); //initialize temporary data array               
                
                record = iterator.next();// Get next record of data                
                
                rowHeaders.add(record[0]);
                for(int i = 1; i < headings.length; i++)
                {                    
                    data.add(Integer.parseInt(record[i]));
                }
                allData.add(data);      
               }
            
            jsonObject.put("rowHeaders", rowHeaders);
            jsonObject.put("data", allData);
            jsonObject.put("colHeaders", colHeaders);            
            
            results = JSONValue.toJSONString(jsonObject);

        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            // INSERT YOUR CODE HERE
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            JSONArray colHeadersJSON = (JSONArray)jsonObject.get("colHeaders");
            JSONArray rowHeadersJSON = (JSONArray)jsonObject.get("rowHeaders");
            JSONArray dataJSON = (JSONArray)jsonObject.get("data");
            
            String colHeadersCSV[] = new String[5]; //will hold each column header (since they are all in a line they can be put into their own array)
            
            for(int i = 0; i < colHeadersJSON.size(); i++)
            {
                colHeadersCSV[i] = (String)colHeadersJSON.get(i); //Store each value into an array as csvWriter can only write using Array
            }            
            csvWriter.writeNext(colHeadersCSV); //Write the first line of headers
            
            String lineCSV[] = new String[5]; //will hold each line that will be parsed through
            
            for(int i = 0; i < rowHeadersJSON.size(); i++)
            {
                JSONArray currentData = (JSONArray)dataJSON.get(i); //Stores each line of data into another array 
                lineCSV[0] = (String)rowHeadersJSON.get(i);         //which will then be used to parse through each index of each line of data
                
                for(int k = 0; k < currentData.size(); k++)
                {
                    lineCSV[k+1] = Long.toString((long)currentData.get(k)); //fill the rest of the current line with the data 
                }                                                           //(its originally a long so you have to cast it to a String for it to be stored)
                csvWriter.writeNext(lineCSV); //write the next line (rowHeader and data)
            }
            results = writer.toString(); //after all the lines have been written store the results into a string
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}