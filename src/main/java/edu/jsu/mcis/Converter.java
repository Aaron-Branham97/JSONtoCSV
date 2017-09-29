package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import java.lang.Object;
import au.com.bytecode.opencsv.*;
import org.json.simple.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Converter {
	
    /*
        Consider a CSV file like the following:
        
        ID,Total,Assignment 1,Assignment 2,Exam 1
        111278,611,146,128,337
        111352,867,227,228,412
        111373,461,96,90,275
        111305,835,220,217,398
        111399,898,226,229,443
        111160,454,77,125,252
        111276,579,130,111,338
        111241,973,236,237,500
        
        The corresponding JSON file would be as follows (note the curly braces):
        
        {
				"colHeaders":["Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
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
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
		
		CSVReader reader = new CSVReader(new StringReader(csvString));
		try {
			String jsonString = "";
			List<String[]> array = reader.readAll();
			JSONObject newJSON = new JSONObject();
			JSONArray colHeaders = new JSONArray();
			JSONArray rowHeaders = new JSONArray();
			JSONArray data = new JSONArray();
			
			for (int i = 0; i < array.get(0).length; i++) {
				colHeaders.add(array.get(0)[i]);
			}
			
			for (int i = 1; i < array.size(); i++) {
				rowHeaders.add(array.get(i)[0]);
			}
			
			for (int i = 1; i < array.size(); i++) {
				data.add(Arrays.toString(Arrays.copyOfRange(array.get(i), 1 ,array.get(i).length)));
				
			}
			newJSON.put("colHeaders", colHeaders);
			newJSON.put("rowHeaders", rowHeaders);
			newJSON.put("data", data.toString().replaceAll("\"" , ""));
			
			String jsonFinal = "";
			jsonFinal = "{\n    \"colHeaders\":" + newJSON.get("colHeaders") + ",\n";
            jsonFinal += "    \"rowHeaders\":" + newJSON.get("rowHeaders") + ",\n";
			jsonFinal += "    \"data\":" + newJSON.get("data").toString().replaceAll("\"", "").replaceAll("],", "],\n            ").replaceAll(", ", ",").replace("]]", "]\n    ]") + "\n}";
			
			return jsonFinal;
			}
		catch (IOException e){};
		
		return "";
		

    }
    
    public static String jsonToCsv(String jsonString) {
		
		JSONObject jsonObject = null;
		
		try {
			JSONParser parser = new JSONParser();
			jsonObject = (JSONObject) parser.parse(jsonString);
		}
		catch (Exception e) {};
		String csvString = Converter.<String>joinArray((JSONArray) jsonObject.get("colHeaders")) + "\n";
		
		JSONArray heads = (JSONArray) jsonObject.get("rowHeaders");
		JSONArray data = (JSONArray) jsonObject.get("data");
		
		for (int i = 0; i < heads.size(); i++) {
		
			csvString += "\""+ (String) heads.get(i) + "\"," + Converter.<Long>joinArray((JSONArray) data.get(i)) + "\n";
		}
		return csvString;
    }
	
	@SuppressWarnings("unchecked")
    private static <T> String joinArray(JSONArray array) {
        String out = "";
        for (int i = 0; i < array.size(); i++) {
            out += "\"" + ((T) array.get(i)) + "\"";
            if (i < array.size() - 1) {
                out += ",";
            }
        }
        return out;
    }

}

   












