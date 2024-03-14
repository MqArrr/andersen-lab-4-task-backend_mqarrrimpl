package lab.andersen.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lab.andersen.dao.UserActivityDaoImpl;
import lab.andersen.exception.ServiceException;
import lab.andersen.model.UserActivity;
import lab.andersen.service.UserActivityServiceImpl;
import lab.andersen.util.TimestampDeserializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/users-activities/*")
public class UserActivityServlet extends HttpServlet {

    private final UserActivityServiceImpl userActivityService = new UserActivityServiceImpl(new UserActivityDaoImpl());
    private final Gson gson;

    {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new TimestampDeserializer())
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || pathInfo.equals("/")) {
            List<UserActivity> activities = null;
            try {
                activities = userActivityService.findAllUsersActivitiesWithUsernames();
            } catch (ServiceException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            out.print(gson.toJson(activities));
        } else {
            UserActivity userActivity = null;
            try {
                int id = Integer.parseInt(pathInfo.replace("/", ""));
                userActivity = userActivityService.findById(id);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to parse user activity id to number");
                return;
            } catch (ServiceException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            out.print(gson.toJson(userActivity));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader in = req.getReader();
        String jsonUserActivity = in.lines().collect(Collectors.joining());

        try {
            UserActivity userActivity = gson.fromJson(jsonUserActivity, UserActivity.class);
            userActivityService.create(userActivity);
        } catch (ServiceException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(e);
        }
        catch (Exception e){
            System.out.println(e);
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader in = req.getReader();
        String jsonUserActivity = in.lines().collect(Collectors.joining());
        try {
            UserActivity userActivity = gson.fromJson(jsonUserActivity, UserActivity.class);
            userActivityService.update(userActivity);
        } catch (ServiceException e) {
            System.out.println(e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            userActivityService.delete(id);
        } catch (ServiceException | NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
