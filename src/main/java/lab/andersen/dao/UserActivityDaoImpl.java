package lab.andersen.dao;

import lab.andersen.exception.DaoException;
import lab.andersen.exception.UserActivityNotFoundException;
import lab.andersen.model.UserActivity;
import lab.andersen.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserActivityDaoImpl implements UserActivityDao {

    private static final String FIND_ALL = "SELECT id, user_id, description, date_time FROM users_activities;";
    private static final String FIND_ALL_WITH_USERNAMES =
            "SELECT a.id, a.user_id, a.description, a.date_time, u.name as username, u.surname as surname " +
                    "FROM users_activities as a left join users as u on u.id = a.user_id;";
    private static final String FIND_BY_ID = "SELECT id, user_id, description, date_time FROM users_activities WHERE id = ?";
    private static final String CREATE_USER_ACTIVITY = "INSERT INTO users_activities(user_id, description) VALUES (?, ?)";
    private static final String UPDATE_USER_ACTIVITY =
            "UPDATE users_activities SET user_id = ?, description = ?, date_time = ? WHERE id = ?;";
    private static final String DELETE_USER_ACTIVITY = "DELETE FROM users_activities WHERE id = ?";

    @Override
    public List<UserActivity> findAll() throws DaoException {
        List<UserActivity> acitivities = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserActivity userActivity = new UserActivity(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("date_time")
                );
                acitivities.add(userActivity);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return acitivities;
    }

    @Override
    public List<UserActivity> findAllAddUsername() throws DaoException {
        List<UserActivity> acitivities = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_WITH_USERNAMES)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserActivity userActivity = new UserActivity(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("date_time"),
                        resultSet.getString("username") + " " + resultSet.getString("surname")
                );
                acitivities.add(userActivity);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return acitivities;
    }

    @Override
    public Optional<UserActivity> findById(int id) throws DaoException {
        UserActivity userActivity = null;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userActivity = new UserActivity(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("date_time")
                );
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return Optional.ofNullable(userActivity);
    }

    @Override
    public void create(UserActivity entity) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(CREATE_USER_ACTIVITY)) {
            statement.setInt(1, entity.getUserId());
            statement.setString(2, entity.getDescription());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(UserActivity entity) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_ACTIVITY)) {
            if(findById(entity.getId()).isPresent()) {
                statement.setInt(1, entity.getUserId());
                statement.setString(2, entity.getDescription());
                statement.setTimestamp(3, entity.getDateTime());
                statement.setInt(4, entity.getId());
                statement.executeUpdate();
                connection.commit();
            } else {
                throw new UserActivityNotFoundException("user activity with id=%d doesn't exist".formatted(entity.getId()));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(int id) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_ACTIVITY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

}
