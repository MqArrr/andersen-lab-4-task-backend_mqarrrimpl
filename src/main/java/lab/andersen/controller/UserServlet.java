package lab.andersen.controller;

import com.google.gson.Gson;
import lab.andersen.dao.UserDaoImpl;
import lab.andersen.exception.ServiceException;
import lab.andersen.model.User;
import lab.andersen.service.UserService;
import lab.andersen.service.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl(new UserDaoImpl());
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<User> users = null;
            try {
                users = userService.findAllUsers();
            } catch (ServiceException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            out.print(gson.toJson(users));
        } else {
            User user = null;
            try {
                int id = Integer.parseInt(pathInfo.replace("/", ""));
                user = userService.findById(id);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to parse user_id to number");
                return;
            } catch (ServiceException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            out.print(gson.toJson(user));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader in = req.getReader();
        String jsonUser = in.lines().collect(Collectors.joining());
        User user = gson.fromJson(jsonUser, User.class);
        try {
            userService.create(user);
        } catch (ServiceException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader in = req.getReader();
        String jsonUser = in.lines().collect(Collectors.joining());
        User user = gson.fromJson(jsonUser, User.class);
        try {
            userService.update(user);
        } catch (ServiceException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            userService.delete(id);
        } catch (ServiceException | NumberFormatException e) {
            System.out.println("Error " + e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}