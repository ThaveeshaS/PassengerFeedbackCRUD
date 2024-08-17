package net.feedbackmanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.feedbackmanagement.dao.Feedback;
import net.feedbackmanagement.model.FeedbackDAO;



@WebServlet("/")
public class FeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FeedbackDAO feedbackDAO;

    @Override
    public void init() throws ServletException { 
        feedbackDAO = new FeedbackDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { 
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertFeedback(request, response);
                    break;
                case "/delete":
                    deleteFeedback(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateFeedback(request, response);
                    break;
                default:
                    listFeedback(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listFeedback(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Feedback> listFeedback = feedbackDAO.selectAllFeedbacks();
        request.setAttribute("listFeedback", listFeedback);
        RequestDispatcher dispatcher = request.getRequestDispatcher("feedback-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("feedback-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Feedback existingFeedback = feedbackDAO.selectFeedback(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("feedback-form.jsp");
        request.setAttribute("feedback", existingFeedback);
        dispatcher.forward(request, response);
    }

    private void insertFeedback(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");
        Feedback newFeedback = new Feedback(name, email, subject, message);
        feedbackDAO.insertFeedback(newFeedback);
        response.sendRedirect("list");
    }

    private void updateFeedback(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");
        Feedback updatedFeedback = new Feedback(id, name, email, subject, message);
        feedbackDAO.updateFeedback(updatedFeedback);
        response.sendRedirect("list");
    }

    private void deleteFeedback(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        feedbackDAO.deleteFeedback(id);
        response.sendRedirect("list");
    }
}