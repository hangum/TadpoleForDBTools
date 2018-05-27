package com.hangum.tadpole.commons.util.inner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hangum.tadpole.commons.csv.CSVUtils;
import com.hangum.tadpole.commons.util.JSONUtil;

/**
 * json to table utils
 * 
 * @see https://github.com/Arkni/json-to-csv 의 JSONFlattener 클래스를 가져와서 고쳤습니다.
 * 
 * @author hangum
 *
 */
public class JSONFlattener {

	/**
     * The JSONObject type
     */
    private static final Class<?> JSON_OBJECT = JSONObject.class;
    /**
     * The JSONArray type
     */
    private static final Class<?> JSON_ARRAY = JSONArray.class;
    /**
     * The class Logger
     */
    private static final Logger logger = Logger.getLogger(JSONFlattener.class);
    
    /**
     * json data to csv
     * 
     * @param json
     * @param string 
     * @return
     */
    public static String parseJsonToCSV(String json, String strDelimiter) throws Exception {
    	List<Map<String, String>> listData = parseJson(json);
    	List<String> listHeadr = new ArrayList<String>(JSONUtil.collectHeaders(listData));
    	String[] arryHead = listHeadr.toArray(new String[listHeadr.size()]);
    	
    	List<String[]> _listContent = new ArrayList<>();
    	_listContent.add(arryHead);
    	
    	for (Map<String, String> map : listData) {
    		String[] _tmpData = new String[arryHead.length];
    		
    		for (int i=0;i<arryHead.length; i++) {
    			_tmpData[i] = map.get(arryHead[i]);
			}
    		
    		_listContent.add(_tmpData);
		}
    	
    	// define delimipter
    	char strDel;
		if("".equals(strDelimiter)) {
			strDel = ',';
		} else if(StringUtils.equalsIgnoreCase("\t", strDelimiter)) {
			strDel = (char)9;
		} else {
			strDel = strDelimiter.charAt(0);
		}
    	
    	return CSVUtils.makeDataString(_listContent, strDel);
    }

    /**
     * Parse the JSON String
     *
     * @param json
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> parseJson(String json) {
        List<Map<String, String>> flatJson = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            flatJson = new ArrayList<Map<String, String>>();
            flatJson.add(parse(jsonObject));
        } catch (JSONException je) {
            logger.info("Handle the JSON String as JSON Array");
            flatJson = handleAsArray(json);
        }

        return flatJson;
    }

    /**
     * Parse a JSON Object
     *
     * @param jsonObject
     * @return
     */
    public static Map<String, String> parse(JSONObject jsonObject) {
        Map<String, String> flatJson = new LinkedHashMap<String, String>();
        flatten(jsonObject, flatJson, "");

        return flatJson;
    }

    /**
     * Parse a JSON Array
     *
     * @param jsonArray
     * @return
     */
    public static List<Map<String, String>> parse(JSONArray jsonArray) {
        JSONObject jsonObject = null;
        List<Map<String, String>> flatJson = new ArrayList<Map<String, String>>();
        int length = jsonArray.length();

        for (int i = 0; i < length; i++) {
            jsonObject = jsonArray.getJSONObject(i);
            Map<String, String> stringMap = parse(jsonObject);
            flatJson.add(stringMap);
        }

        return flatJson;
    }

    /**
     * Handle the JSON String as Array
     *
     * @param json
     * @return
     * @throws Exception
     */
    private static List<Map<String, String>> handleAsArray(String json) {
        List<Map<String, String>> flatJson = null;

        try {
            JSONArray jsonArray = new JSONArray(json);
            flatJson = parse(jsonArray);
        } catch (Exception e) {
            // throw new Exception("Json might be malformed");
            logger.error("JSON might be malformed, Please verify that your JSON is valid");
        }

        return flatJson;
    }

    /**
     * Flatten the given JSON Object
     *
     * This method will convert the JSON object to a Map of
     * String keys and values
     *
     * @param obj
     * @param flatJson
     * @param prefix
     */
    private static void flatten(JSONObject obj, Map<String, String> flatJson, String prefix) {
        Iterator<?> iterator = obj.keys();
        String _prefix = prefix != "" ? prefix + "." : "";

        while (iterator.hasNext()) {
            String key = iterator.next().toString();

            if (obj.get(key).getClass() == JSON_OBJECT) {
                JSONObject jsonObject = (JSONObject) obj.get(key);
                flatten(jsonObject, flatJson, _prefix + key);
            } else if (obj.get(key).getClass() == JSON_ARRAY) {
                JSONArray jsonArray = (JSONArray) obj.get(key);

                if (jsonArray.length() < 1) {
                    continue;
                }

                flatten(jsonArray, flatJson, _prefix + key);
            } else {
                String value = obj.get(key).toString();

                if (value != null && !value.equals("null")) {
                    flatJson.put(_prefix + key, value);
                }
            }
        }

    }

    /**
     * Flatten the given JSON Array
     *
     * @param obj
     * @param flatJson
     * @param prefix
     */
    private static void flatten(JSONArray obj, Map<String, String> flatJson, String prefix) {
        int length = obj.length();

        for (int i = 0; i < length; i++) {
            if (obj.get(i).getClass() == JSON_ARRAY) {
                JSONArray jsonArray = (JSONArray) obj.get(i);

                // jsonArray is empty
                if (jsonArray.length() < 1) {
                    continue;
                }

                flatten(jsonArray, flatJson, prefix + "[" + i + "]");
            } else if (obj.get(i).getClass() == JSON_OBJECT) {
                JSONObject jsonObject = (JSONObject) obj.get(i);
                flatten(jsonObject, flatJson, prefix + "[" + (i + 1) + "]");
            } else {
                String value = obj.get(i).toString();

                if (value != null) {
                    flatJson.put(prefix + "[" + (i + 1) + "]", value);
                }
            }
        }
    }

}
