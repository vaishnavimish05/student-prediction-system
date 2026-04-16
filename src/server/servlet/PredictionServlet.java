package server.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import server.model.Student;
import server.service.PredictionService;
import shared.PredictionResult;
import shared.StudentData;
import java.io.IOException;
import java.util.List;
@WebServlet(urlPatterns = {"/api/predict", "/api/students", "/api/health", "/api/history", "/api/insights"})
public class PredictionServlet extends HttpServlet {

    private final PredictionService service = new PredictionService();
    private final ObjectMapper mapper;

    public PredictionServlet() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json;charset=UTF-8");
        String path = req.getRequestURI();
        
        if (path.contains("/api/health")) {
            resp.getWriter().write("{\"status\":\"Student Prediction Server is running!\"}");
        } else if (path.contains("/api/students")) {
            List<Student> students = service.getAllStudents();
            mapper.writeValue(resp.getWriter(), students);
        } else if (path.contains("/api/history")) {
            var history = service.getPredictionHistory();
            mapper.writeValue(resp.getWriter(), history);
        } else if (path.contains("/api/insights")) {
            var insights = service.getHistoricalInsights();
            mapper.writeValue(resp.getWriter(), insights);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Not found\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json;charset=UTF-8");
        String path = req.getRequestURI();

        if (!path.contains("/api/predict")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Endpoint not found\"}");
            return;
        }

        try {
            StringBuilder jsonBody = new StringBuilder();
            String line;
            java.io.BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
            System.out.println("[SERVLET] Received JSON: " + jsonBody.toString());
            
            StudentData studentData = mapper.readValue(jsonBody.toString(), StudentData.class);
            if (studentData.getName() == null || studentData.getName().isBlank()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Name is required\"}");
                return;
            }

            System.out.println("[SERVLET] POST /api/predict for: " + studentData.getName());
            PredictionResult result = service.predict(studentData);
            mapper.writeValue(resp.getWriter(), result);

        } catch (com.fasterxml.jackson.core.JsonParseException e) {
            System.err.println("[SERVLET] JSON Parse Error: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid JSON format: " + e.getMessage() + "\"}");
        } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
            System.err.println("[SERVLET] JSON Mapping Error: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid data format: " + e.getOriginalMessage() + "\"}");
        } catch (Exception e) {
            System.err.println("[SERVLET] Unexpected Error: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin",  "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
