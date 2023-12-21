package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Student;
import DAO.StudentDAO;
import utils.StudentDateTypeAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class StudentHandler implements HttpHandler {

    private final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new StudentDateTypeAdapter("dd-MM-yyyy"))
            .create();;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        exchange.getResponseHeaders().set("Access-Control-Max-Age", "3600");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            handlePreflightRequest(exchange);
        } else {
            if (path.equals("/students") && exchange.getRequestMethod().equals("GET")) {
                handleGetStudents(exchange);
            } else if (path.equals("/students") && exchange.getRequestMethod().equals("POST")) {
                handleAddStudent(exchange);
            } else if (path.matches("/students/\\d+") && exchange.getRequestMethod().equals("DELETE")) {
                handleDeleteStudent(exchange);
            } else {
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
            }
        }

    }

    private void handleGetStudents(HttpExchange exchange) throws IOException {
        List<Student> studentList = StudentDAO.getAllStudents();
        String jsonResponse = gson.toJson(studentList);
        sendResponse(exchange, jsonResponse, 200);
    }

    private void handleAddStudent(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Student newStudent = null;
        int id = 0;
        try{
            newStudent = gson.fromJson(requestBody, Student.class);
            id = StudentDAO.addStudent(newStudent);
        }
        catch (Exception ex){
            sendResponse(exchange, ex.getMessage(), 404);
        }
        String jsonResponse = "Student added successfully with unique number: " + id;
        sendResponse(exchange, jsonResponse, 201);
    }

    private void handleDeleteStudent(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(parts[2]);
        if (StudentDAO.deleteStudent(id)) {
            sendResponse(exchange, "Student deleted successfully", 204);
        } else {
            sendResponse(exchange, "Student not found", 404);
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
        exchange.close();
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }


    private void handlePreflightRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        exchange.getResponseHeaders().set("Access-Control-Max-Age", "3600");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        exchange.sendResponseHeaders(204, -1);
    }
}
