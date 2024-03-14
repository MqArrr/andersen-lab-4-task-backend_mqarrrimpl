package lab.andersen.service;

import lab.andersen.dao.UserActivityDao;
import lab.andersen.exception.DaoException;
import lab.andersen.exception.ServiceException;
import lab.andersen.exception.UserActivityNotFoundException;
import lab.andersen.model.UserActivity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {

    private final UserActivityDao userActivityDao;

    @Override
    public List<UserActivity> findAllUsersActivities() throws ServiceException {
        try {
            return userActivityDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<UserActivity> findAllUsersActivitiesWithUsernames() throws ServiceException {
        try {
            return userActivityDao.findAllAddUsername();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public UserActivity findById(int id) throws ServiceException {
        try {
            Optional<UserActivity> optionalUserActivity = userActivityDao.findById(id);
            if (optionalUserActivity.isPresent()) {
                return optionalUserActivity.get();
            } else {
                throw new UserActivityNotFoundException("user activity with id=%d doesn't exist".formatted(id));
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void create(UserActivity userActivity) throws ServiceException {
        try {
            userActivityDao.create(userActivity);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(UserActivity userActivity) throws ServiceException {
        try {
            userActivityDao.update(userActivity);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            userActivityDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
