package il.ac.hit.restful;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import il.ac.hit.dao.DAOException;
import il.ac.hit.dao.EventDAOHibernate;
import il.ac.hit.dao.IEventDAO;
import il.ac.hit.utils.Event;
import il.ac.hit.utils.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


@Path("/event")
public class RESTEventController {
    /**
     * This class purposes is to give a web client a service with certain constrains who's used
     * to give access to a "Diary" based application on the Model side who acts as a controller.
     * this class purpose is to handle the user's Events!
     *
     * this class will help you gain access to a MySQL data base using hibernate and give functionalities like
     * 1> Saving a new event and get the event id
     * 2> saving  or update events of a registered user. events that includes:
     *           email as a String (not Null)
     *           title as a String (not Null)
     *           the event location as a String
     *           the event start time as a java.sql.Time object or a String that matches this Object type
     *           the event end time as a java.sql.Time object or a String that matches this Object type
     *           an event ID that will be over overwritten by the DAO if its a new event
     *           the event Date as a java.sql.Date object or a String that matches this Object type
     *           and a Note as a String
     * while verifying the user and the event's email
     * etc.
     *
     * this glass URI is "http://localhost:8080/mydiary/api/event/{method path name}
     *
     * methods path names:
     * new,verify,delete,get,update,get/all,delete/all
     *
     * this class communicates with the client via input stream of Json strings.
     *  fyi we use GSON jar v 2.8.6 as a tool to parse the json string
     * and returns a json as a String back to a client and a HTTP Response status codes
     * */












    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response NewEvent(InputStream inputStream) {
        /**
         *
         * Creating a new event.
         * the Client should send a JsonObject and in return will get an HTTP response status codes
         * HTTP method "POST"
         *     the jsonObjects includes two json elements: "email" and "password"
         *     and a jsonArray includes:
         *           email as a String (not Null)
         *           title as a String (not Null)
         *           the event location as a String
         *           the event start time as a java.sql.Time object or a String that matches this Object type
         *           the event end time as a java.sql.Time object or a String that matches this Object type
         *           an event ID that will be over overwritten by the DAO if its a new event
         *           the event Date as a java.sql.Date object or a String that matches this Object type
         *           and a Note as a String
         *
         *
         *     HTTP response status codes return:
         *     200 (ok) in case the user exists with the same password and the new id in the response body
         *     401 unauthorized user
         *     400 error parsing the jsonObject or invalid user email or password
         *     503 service unavailable
         *
         *     example:
         *     URL: "http://localhost:8080/MyDiary/api/user/new
         *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
         *                                Property2: "password", value: "a1b1"
         *                                Property3: "event", value: jsonArray
         * */

        JsonObject json;
        User user;
        Event event;
        try {
            json = RESTUtils.inputStreamToJson(inputStream);
            user = RESTUtils.jsonToUserConverter(json);
            event = RESTUtils.jsonToEventConverter(json);

            IEventDAO DAO = EventDAOHibernate.getInstance();

            if(DAO.userVerification(user))
            {
                Long eventID = DAO.insertEvent(user,event);
                Gson gson=new Gson();
                return Response.ok(gson.toJson(eventID)).build();//Success
            }
            else
            {
                //user not found
                //Unauthorized status
                return Response.status(401).build();

            }



        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(400).build();
        } catch (DAOException e) {
            e.printStackTrace();
            return Response.status(503).build();

        }
    }


    @Path("/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteEvents(InputStream inputStream) {
        /**
         *
         * Deleting Existing event or events.
         * the Client should send a JsonObject and in return will get an HTTP response status codes
         * HTTP method "POST"
         *     the jsonObjects includes three json elements: "email" , "password" and a list of event id's as a JsonArray
         *     with the keyword "eventsId"
         *
         *
         *     HTTP response status codes return:
         *     200 (ok) in case the user exists with the same password
         *     401 unauthorized user
         *     400 error parsing the jsonObject or invalid user email or password
         *     503 service unavailable
         *
         *     example:
         *     URL: "http://localhost:8080/mydiary/api/user/delete
         *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
         *                                Property2: "password", value: "a1b1"
         *                                Property3: "eventsId", JsonArray of event's id's
         * */
        JsonObject json;
        User user;
        List<Long> eventsIdList;
        List<Event> eventsList= new ArrayList<>();


        try {
            json = RESTUtils.inputStreamToJson(inputStream);
            user = RESTUtils.jsonToUserConverter(json);
            eventsIdList=RESTUtils.jsonToEventsIdListConverter(json);


            IEventDAO DAO = EventDAOHibernate.getInstance();

            if(DAO.userVerification(user))
            {
                for (Long id:eventsIdList) {
                    eventsList.add(DAO.getEvent(user,id));
                }
                DAO.deleteEvents(user,eventsList);
                return Response.status(200).build();
            }
            {
                return Response.status(401).build();
            }


        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(400).build();
        } catch (DAOException e) {
            e.printStackTrace();
            return Response.status(503).build();
        }

    }

    @Path("/delete/all")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    /**
     *
     * Deleting all existing user's events.
     * the Client should send a JsonObject and in return will get an HTTP response status codes
     * this method will delete all the user's events in Data base
     * HTTP method "POST"
     *     the jsonObjects includes two json elements: "email" and "password"
     *
     *
     *     HTTP response status codes return:
     *     200 (ok) in case the user exists with the same password
     *     401 unauthorized user
     *     400 error parsing the jsonObject or invalid user email or password
     *     503 service unavailable
     *
     *     example:
     *     URL: "http://localhost:8080/mydiary/api/user/delete/all
     *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
     *                                Property2: "password", value: "a1b1"
     * */
    public Response deleteAllEvents(InputStream inputStream)
    {
        User user;
        try {
            JsonObject jsonObject=RESTUtils.inputStreamToJson(inputStream);
            user=RESTUtils.jsonToUserConverter(jsonObject);

            IEventDAO DAO=EventDAOHibernate.getInstance();

            if(DAO.userVerification(user))
            {
               List<Event> eventsList = DAO.getEvents(user);
               DAO.deleteEvents(user,eventsList);
               return Response.status(200).build();
            }
            else
            {
                return Response.status(401).build();
            }

        } catch (IOException  e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
        catch (DAOException De)
        {
            De.printStackTrace();
            return Response.status(503).build();
        }
    }



    @Path("/get")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvents(InputStream inputStream)
    {
        /**
         *
         * get Existing event or events.
         * the Client should send a JsonObject and in return will get an HTTP response status codes
         * this method will return the user's event/s in Data base as a json string
         * HTTP method "POST"
         *     the jsonObjects that in the Request body includes three json elements: "email" , "password" and "eventId"
         *     while the eventId is a List of event's id's as a json array with the keyword "eventsId"
         *
         *
         *     HTTP response status codes return:
         *     200 (ok) in case the user exists with the same password
         *     401 unauthorized user
         *     400 error parsing the jsonObject or invalid user email or password
         *     503 service unavailable
         *
         *     example:
         *     URL: "http://localhost:8080/mydiary/api/user/get
         *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
         *                                Property2: "password", value: "a1b1"
         *                                Property3: "eventsId", JsonArray of event's id's
         *
         *     if the user is verified and the event is legit, the Response will include a jsonObject with a jsonArray
         *     as following:
         *           email as a String (not Null)
         *           title as a String (not Null)
         *           the event location as a String
         *           the event start time as a java.sql.Time object or a String that matches this Object type
         *           the event end time as a java.sql.Time object or a String that matches this Object type
         *           an event ID that will be over overwritten by the DAO if its a new event
         *           the event Date as a java.sql.Date object or a String that matches this Object type
         *           and a Note as a String
         * */


        JsonObject jsonObject;
        User user;
        List<Long> eventsIdList;
        List<Event> eventsList=new ArrayList<>();
        try {
             jsonObject=RESTUtils.inputStreamToJson(inputStream);
             user=RESTUtils.jsonToUserConverter(jsonObject);
             eventsIdList=RESTUtils.jsonToEventsIdListConverter(jsonObject);


             IEventDAO DAO=EventDAOHibernate.getInstance();
             if(DAO.userVerification(user))
             {
                 for (Long id:eventsIdList)
                 {
                     eventsList.add(DAO.getEvent(user,id));
                 }

                 //TODO check
                 Gson gson=new Gson();

                 return Response.ok(gson.toJson(eventsList)).build();
             }
             else
             {
                 return Response.status(401).build();


             }




        } catch (IOException  e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
        catch (DAOException De)
        {
            De.printStackTrace();
            return Response.status(503).build();
        }


    }


    @Path("/get/all")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsersEvents(InputStream inputStream)
    {
        /**
         *
         * get Existing events.
         * the Client should send a JsonObject and in return will get an HTTP response status codes
         * this method will return the user's events in Data base as a json string
         * HTTP method "POST"
         *     the jsonObjects that in the Request body includes two json elements: "email" and "password".
         *
         *
         *     HTTP response status codes return:
         *     200 (ok) in case the user exists with the same password
         *     401 unauthorized user
         *     400 error parsing the jsonObject or invalid user email or password
         *     503 service unavailable
         *
         *     example:
         *     URL: "http://localhost:8080/mydiary/api/user/get/all
         *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
         *                                Property2: "password", value: "a1b1"
         *
         *     if the user is verified and the event is legit, the Response will include a jsonObject with a JsonArray
         *     as following:
         *           email as a String (not Null)
         *           title as a String (not Null)
         *           the event location as a String
         *           the event start time as a java.sql.Time object or a String that matches this Object type
         *           the event end time as a java.sql.Time object or a String that matches this Object type
         *           an event ID that will be over overwritten by the DAO if its a new event
         *           the event Date as a java.sql.Date object or a String that matches this Object type
         *           and a Note as a String
         * */
        JsonObject jsonObject;
        User user;
        List<Event> events;
        try {
            jsonObject=RESTUtils.inputStreamToJson(inputStream);
            user=RESTUtils.jsonToUserConverter(jsonObject);

            IEventDAO DAO=EventDAOHibernate.getInstance();
            if(DAO.userVerification(user))
            {
                events = DAO.getEvents(user);
                Gson gson=new Gson();

                return Response.ok(gson.toJson(events)).build();
            }
            else
            {
                return Response.status(401).build();


            }

        } catch (IOException  e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
        catch (DAOException De)
        {
            De.printStackTrace();
            return Response.status(503).build();
        }


    }

    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response UpdateEvents(InputStream inputStream)
    {
        /**
         *
         * updates Existing event.
         * the Client should send a JsonObject and in return will get an HTTP response status codes
         * this method will return a response status code 200 (ok) if event was updated successfully
         *
         * HTTP method "POST"
         *     the jsonObjects that in the Request body includes three json elements: "email", "password" and "event"
         *
         *
         *     HTTP response status codes return:
         *     200 (ok) in case the user exists with the same password
         *     401 unauthorized user
         *     400 error parsing the jsonObject or invalid user email or password
         *     503 service unavailable
         *
         *     example:
         *     URL: "http://localhost:8080/mydiary/api/event/update
         *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
         *                                Property2: "password", value: "a1b1"
         *                                Property3 "event", value: JsonArray
         *
         *     if the user is verified and the event is legit, the Response will will be an HTTP Request status code 200 (ok)
         * */
        JsonObject jsonObject;
        User user;
        Event event;

        try {
            jsonObject=RESTUtils.inputStreamToJson(inputStream);
            user=RESTUtils.jsonToUserConverter(jsonObject);
            event=RESTUtils.jsonToEventConverter(jsonObject);




            IEventDAO DAO=EventDAOHibernate.getInstance();
            if(DAO.userVerification(user))
            {
                DAO.updateEvent(user,event);
                return Response.ok().build();
            }
            else
            {
                return Response.status(401).build();
            }

        } catch (IOException  e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
        catch (DAOException De)
        {
            De.printStackTrace();
            return Response.status(503).build();
        }


    }



}
