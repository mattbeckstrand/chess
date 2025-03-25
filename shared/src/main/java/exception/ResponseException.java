package exception;

import com.google.gson.Gson;
import model.ErrorResponse;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {
    final private int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", statusCode));
    }

    public static ResponseException fromJson(InputStream stream) {
        InputStreamReader reader = new InputStreamReader(stream);
        ErrorResponse error = new Gson().fromJson(reader, ErrorResponse.class);
        return new ResponseException(403, error.message());
    }

    public int statusCode() {
        return statusCode;
    }
}