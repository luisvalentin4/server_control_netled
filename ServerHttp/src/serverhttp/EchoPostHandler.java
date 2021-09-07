/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverhttp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PLACOFF2015
 */
public class EchoPostHandler implements HttpHandler{
     @Override

         public void handle(HttpExchange he) throws IOException {
                 // parse request
                 Map<String, Object> parameters = new HashMap<String, Object>();
                 InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                 BufferedReader br = new BufferedReader(isr);
                 String query = br.readLine();
                 System.out.println(query);
                 parseQuery(query, parameters);

                 // send response
                 String response = "";
                 for (String key : parameters.keySet())
                          response += key + " = " + parameters.get(key) + "n";
                 he.sendResponseHeaders(200, response.length());
                 OutputStream os = he.getResponseBody();
                 os.write(response.toString().getBytes());
                 os.close();
         }
         
         public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

    if (query != null) {
                 String pairs[] = query.split("[&]");
                 for (String pair : pairs) {
                          String param[] = pair.split("[=]");
                          String key = null;
                          String value = null;
                          if (param.length > 0) {
                          key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                          }


                          if (param.length > 1) {
                                   value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                          }

                          if (parameters.containsKey(key)) {
                                   Object obj = parameters.get(key);
                                   if (obj instanceof List<?>) {
                                            List values = (List) obj;
                                            values.add(value);

                                   } else if (obj instanceof String) {
                                            List values = new ArrayList();
                                            values.add((String) obj);
                                            values.add(value);
                                            parameters.put(key, values);
                                   }
                          } else {
                                   parameters.put(key, value);
                          }

                 }

         }
    }

    
}
