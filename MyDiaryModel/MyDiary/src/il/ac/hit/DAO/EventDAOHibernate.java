package il.ac.hit.DAO;



import com.sun.istack.internal.NotNull;
import il.ac.hit.utils.Event;
import il.ac.hit.utils.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;


import java.util.List;

public class EventDAOHibernate implements IEventDAO {
    /**
     * Implements IEventDAO interface
     * this class is specified to work with Hibernate as the JDBC
     *
     * In our project we use this class to connect to MySQL using Hibernate as a Singleton based class
     * to add/remove/verify user's and add/remove/update events.
     *
     *
     *
     * */
    private SessionFactory factory;
    private static EventDAOHibernate instance=null;


    private EventDAOHibernate() throws DAOException
    {
        //creating a session with the DataBase
        //Notice! private Ctor!!
        factory= new AnnotationConfiguration().configure().buildSessionFactory();

    }
    public static EventDAOHibernate getInstance() throws DAOException {
        /**A static method that is base on the Singleton Design pattern
         * checks if you have an instance to the class, if you do it returns the instance and if not it creates one
         * Using the private Ctor
         * */
        if(EventDAOHibernate.instance==null)
        {
            instance=new EventDAOHibernate();
        }
        return EventDAOHibernate.instance;
    }





    @Override
    public Long insertEvent(User user, Event event) throws DAOException {
        /**
         * 1> checks if the verified user email equals to the event user email
         * if they are equal, pushes it to the DataBase
         * if there is a problem with the DataBase/Hibernate we throw a new DAOExeption
         * if the emails dont match, throws a DAOException
         * if all goes well, returns the new event id
         * */


        Session session = null;

        if(verifyUserEmailAndEventEmail(user.getEmail(),event.getEmail())) {
            try {
                session = factory.openSession();
                session.beginTransaction();
                session.save(event);
                session.getTransaction().commit();
                event.setId((Long) session.getIdentifier(event));
                return event.getId();

            } catch (HibernateException e) {
                e.printStackTrace();
                throw new DAOException("couldnt insert event", e);
            } finally {
                session.close();
            }
        }
        else
        {
            throw new DAOException("user email and event email dont match. user email: "+user.getEmail()+" and event email: "+event.getEmail());
        }

    }




    @Override
    public void deleteEvent(User user,Event event) throws DAOException {
        /**
         * 1> checks if the verified user email equals to the event user email
         * if they are equal, tries to delete the event
         * if event dont exists throws a DAOException
         * if there is a problem with the DataBase/Hibernate we throw a new DAOExeption
         * if the emails dont match, throws a DAOException
         * */
        Session session = null;

        if(verifyUserEmailAndEventEmail(user.getEmail(),event.getEmail())) {
            try {
                session = factory.openSession();
                session.beginTransaction();
                session.delete(event);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                throw new DAOException("couldnt delete event", e);
            } finally {
                assert session != null;
                session.close();
            }
        }
        else
        {
            throw new DAOException("user email and event email dont match. user email: "+user.getEmail()+" and event email: "+event.getEmail());

        }

    }


    @Override
    public void deleteEvents(@NotNull User user, @NotNull List<Event> eventsList) throws DAOException {
        /**
         * 1>  for each event, checks if the verified user email equals to the event user email
         * if they are equal, tries to delete the event
         * if event dont exists throws a DAOException
         * if there is a problem with the DataBase/Hibernate we throw a new DAOExeption
         * if the emails dont match, throws a DAOException
         * */

        Session session = null;
        String exceptionLog = "coudlnt match user email with event email at event: 's:\n ";
        boolean errorOccurred = false;


        for (Event event : eventsList) {
            if (!verifyUserEmailAndEventEmail(user.getEmail(), event.getEmail())) {
                exceptionLog = exceptionLog + event.toString() + ",";
                errorOccurred = true;

            }
        }
        if (errorOccurred) {
            throw new DAOException(exceptionLog);
        }

        try {
            if (eventsList.size() > 0) {
                session = factory.openSession();
                session.beginTransaction();
                for (Event event : eventsList) {
                    session.delete(event);
                }
                session.getTransaction().commit();
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new DAOException("couldnt delete some or all of the events, problem with Hibernate", e);

        } finally {
            session.close();
        }
    }




    @Override
    public Event getEvent(User user, long eventId) throws DAOException {
        /**
         * Queries the DataBase for the event using the user email and the event id
         * if event exists it returns the event as an il.ac.hit.utils.Event
         * if not, returns Null
         *
         * Hibernate errors cause a Throw of a DAOException
         *
         *
         *
         * */
        Session session = null;
        String query;
        List list=null;
        try {
            session = factory.openSession();
            session.beginTransaction();

            query="from Event where email = :userEmail AND id = :userId";


            list = session.createQuery(query)
                    .setParameter("userEmail",user.getEmail())
                    .setParameter("userId",eventId)
                    .list();
            if(list.size()==1)
            {
                return (Event) list.get(0);

            }
            else
            {
                return null;

            }

        }
        catch (HibernateException e) {
            e.printStackTrace();
            throw new DAOException("problem with DAO",e);

        } finally {
            session.close();
        }
    }

    @Override
    public List<Event> getEvents(User user) throws DAOException {
        /**
         * gets all events from DataBase
         * returns null if there are no events
         * may throw DAOException*/

        Session session = null;
        String query;


        List list=null;
        try {
            session = factory.openSession();
            session.beginTransaction();
            query="from Event where email = :userEmail";
            list = session.createQuery(query)
                    .setParameter("userEmail",user.getEmail())
                    .list();

            if(list.isEmpty())
            {
               return null;
            }

            return list;

        }
        catch (HibernateException e) {
            e.printStackTrace();
            throw new DAOException("problem fetching the events from DAO",e);

        } finally {
            session.close();
        }
    }
    @Override
    /*
    *
    *
    * */
    public boolean deleteUser(User user) throws DAOException {
        /**
         * Delete all the user events and then Deletes the user for DataBase
         * May throw a hibernate exception in case it coudlnt delete the events and before deleting the user
         * return true if succeeded and false otherwise
         * may throw a DAOException
         *
         * */
        Session session = null;
        Boolean Success=false;

        try {
            List<Event> userEvents = getEvents(user);
            if (userEvents != null) {
                deleteEvents(user, userEvents);
            }

            session = factory.openSession();
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();

            Success = true;

        }
        catch (HibernateException e)
        {
           e.printStackTrace();
           throw new DAOException("couldnt delete user but deleted his events");

        }
        catch (DAOException e)
        {
            //re throwing
            e.printStackTrace();
            throw new DAOException("coudlnt delete user and events",e);
        }


        return Success;
    }

    @Override
    public boolean createNewUser(User user) throws DAOException {
        /**
         * creating a new user in database.
         * if DataBase all ready have a user with the same email returns false,
         * otherwise return true
         *
         * may throw a DAOException if Error occurred in Hibernate
         *
         *
         *
         * */
        Session session = null;
        Boolean userCreated = false;

        if (userVerification(user)==false) {
            try {
                session = factory.openSession();
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
                userCreated = true;

            } catch (HibernateException He) {
                He.printStackTrace();
                throw new DAOException("DB problem, couldn't create new user even though it does not exists", He);
            } finally {
                session.close();
            }
        }
        return userCreated;
    }


    @Override
    public Boolean userVerification(User user) throws DAOException {
        /**
         * Verifies if the user exists in DataBase
         * return true or false accordingly.
         * */

        // checks if user exists, if he does true, if not false, exception in any other case
        Session session = null;
        List list=null;
        Boolean exists=false;

        try {
            session = factory.openSession();
            session.beginTransaction();



            String queryQuestion =("from User where email = :userEmail AND password = :userPassword");
            list=session.createQuery(queryQuestion)
                    .setParameter("userEmail",user.getEmail())
                    .setParameter("userPassword",user.getPassword()).list();

            if(!list.isEmpty())
            {
                exists = true;

            }



        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            throw new DAOException("problem connecting to Hibernate",e);

        } finally {
            assert session != null;
            session.close();

        }
        return exists;
    }

    @Override
    public void updateEvent(User user, Event event) throws DAOException {
        /**
         * updates an existing event in DataBase.
         * Also verifies the user information and checks if user email and event email matches
         * May throw a DAOException
         * */
        Session session = null;

        userVerification(user);
      if(verifyUserEmailAndEventEmail(user.getEmail(),event.getEmail())) {
          try {
              session = factory.openSession();
              session.beginTransaction();
              session.update(event);
              session.getTransaction().commit();

          } catch (HibernateException e) {
              e.printStackTrace();
              throw new DAOException("couldnt update event", e);
          } finally {
              session.close();
          }
      }
      else
      {

          throw new DAOException("user email and event email are different");

      }

    }

    @Override
    public boolean verifyUserEmailAndEventEmail(@NotNull String userEmail, @NotNull String eventEmail) throws DAOException
    {
        /** checks if user email and event email are equals*/
        boolean usersAndEventEmailEquals=false;
        if(userEmail.equals(eventEmail))
        {
            usersAndEventEmailEquals=true;

        }
        return usersAndEventEmailEquals;
    }
}

