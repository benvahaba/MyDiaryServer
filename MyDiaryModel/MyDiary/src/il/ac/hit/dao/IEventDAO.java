package il.ac.hit.dao;


import com.sun.istack.internal.NotNull;
import il.ac.hit.utils.Event;
import il.ac.hit.utils.User;


import java.util.List;

public interface IEventDAO {
    /**an Interface thats help us override methods that are used to communicate with our Data Base
     * each method may Throw a DAOException
     *
     * */

    void insertEvent(User user, Event event) throws DAOException;//done
    void deleteEvent(User user, Event event) throws DAOException;//done
    void deleteEvents(User user, List<Event> eventsList) throws DAOException;//done
    Event getEvent(User user, long eventId) throws DAOException;//done
    List<Event> getEvents(User user) throws DAOException;//done
    boolean createNewUser(User user)throws DAOException;//done
    boolean deleteUser(User user)throws DAOException;//done
    Boolean userVerification(User user) throws DAOException;//done
    void updateEvent(User user, Event event) throws DAOException;//done
    boolean verifyUserEmailAndEventEmail(@NotNull String userEmail, @NotNull String event) throws DAOException;

}
