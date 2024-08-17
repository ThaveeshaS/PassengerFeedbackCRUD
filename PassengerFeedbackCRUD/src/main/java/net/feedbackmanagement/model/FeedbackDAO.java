package net.feedbackmanagement.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.feedbackmanagement.dao.Feedback;

public class FeedbackDAO {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/demo2?useSSL=false";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "0712568275";

    private static final String INSERT_FEEDBACK_SQL = "INSERT INTO feedbacks (name, email, subject, message) VALUES (?, ?, ?, ?)";
    private static final String SELECT_FEEDBACK_BY_ID_SQL = "SELECT id, name, email, subject, message FROM feedbacks WHERE id = ?";
    private static final String SELECT_ALL_FEEDBACKS_SQL = "SELECT id, name, email, subject, message FROM feedbacks";
    private static final String DELETE_FEEDBACK_SQL = "DELETE FROM feedbacks WHERE id = ?";
    private static final String UPDATE_FEEDBACK_SQL = "UPDATE feedbacks SET name = ?, email = ?, subject = ?, message = ? WHERE id = ?";

    public FeedbackDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to load MySQL JDBC driver.");
        }
    }

    public void insertFeedback(Feedback feedback) {
        
        if (feedback.getName() == null || feedback.getName().isEmpty() ||
            feedback.getEmail() == null || feedback.getEmail().isEmpty() ||
            feedback.getSubject() == null || feedback.getSubject().isEmpty() ||
            feedback.getMessage() == null || feedback.getMessage().isEmpty()) {
            System.err.println("Error: Name, email, subject, and message must be provided.");
            return;
        }

      
        if (!isValidEmail(feedback.getEmail())) {
            System.err.println("Error: Invalid email format.");
            return;
        }

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FEEDBACK_SQL)) {
            preparedStatement.setString(1, feedback.getName());
            preparedStatement.setString(2, feedback.getEmail());
            preparedStatement.setString(3, feedback.getSubject());
            preparedStatement.setString(4, feedback.getMessage());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public Feedback selectFeedback(int id) {
        Feedback feedback = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FEEDBACK_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    feedback = extractFeedbackFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return feedback;
    }

    public List<Feedback> selectAllFeedbacks() {
        List<Feedback> feedbacks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FEEDBACKS_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                feedbacks.add(extractFeedbackFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return feedbacks;
    }

    public boolean deleteFeedback(int id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_FEEDBACK_SQL)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
            return false;
        }
    }

    public boolean updateFeedback(Feedback feedback) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_FEEDBACK_SQL)) {
            statement.setString(1, feedback.getName());
            statement.setString(2, feedback.getEmail());
            statement.setString(3, feedback.getSubject());
            statement.setString(4, feedback.getMessage());
            statement.setInt(5, feedback.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
            return false;
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    private Feedback extractFeedbackFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String subject = resultSet.getString("subject");
        String message = resultSet.getString("message");
        return new Feedback(id, name, email, subject, message);
    }

    private boolean isValidEmail(String email) {
        
        return email.contains("@") && email.contains(".");
    }

    private void printSQLException(SQLException ex) {
        ex.printStackTrace();
        System.err.println("SQLState: " + ex.getSQLState());
        System.err.println("Error Code: " + ex.getErrorCode());
        System.err.println("Message: " + ex.getMessage());
        Throwable t = ex.getCause();
        while (t != null) {
            System.out.println("Cause: " + t);
            t = t.getCause();
        }
    }
}
