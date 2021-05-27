package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

public class Handlers {
    static class RoutHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<h1>Server starts successfully if you see this message</h1>";
            //rCode - http код ответа
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.flush();
            os.close();
        }
    }
    static class EchoGetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //parse request
            Map<String, Object> parameters = new HashMap<>();
            URI requestedURI = exchange.getRequestURI();
            String query = requestedURI.getRawQuery();
            parseQueue(query, parameters);
            //send response
            String response = "";
            for(String key : parameters.keySet())
                response += key + " = " + parameters.get(key) + "\n";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }
    public static void parseQueue(String query, Map<String, Object> parameteres) throws UnsupportedEncodingException {
        if( query != null) {
            String pairs [] = query.split("&");
            for(String pair : pairs) {
                String param[] = pair.split("=");
                String key = null;
                String value = null;
                if(param.length > 0)
                    key = URLDecoder.decode(param[0],System.getProperty("file.encoding"));
                if(param.length > 1)
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
            if(parameteres.containsKey(key)) {
                    Object obj = parameteres.get(key);
                    if(obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if(obj instanceof String) {
                        List<String> values = new ArrayList<>();
                        values.add((String) obj);
                        values.add(value);
                        parameteres.put(key, values);
                    }
            } else {
                parameteres.put(key, value);
            }
            }
        }
    }

}
