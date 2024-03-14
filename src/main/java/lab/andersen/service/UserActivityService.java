package lab.andersen.service;

import lab.andersen.exception.ServiceException;
import lab.andersen.model.UserActivity;

import java.util.List;

public interface UserActivityService {

    List<UserActivity> findAllUsersActivities() throws ServiceException;
    List<UserActivity> findAllUsersActivitiesWithUsernames() throws ServiceException;

    UserActivity findById(int id) throws ServiceException;

    void create(UserActivity userActivity) throws ServiceException;

    void update(UserActivity userActivity) throws ServiceException;

    void delete(int id) throws ServiceException;
}
