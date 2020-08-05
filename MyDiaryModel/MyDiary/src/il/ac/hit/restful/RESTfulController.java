package il.ac.hit.restful;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import il.ac.hit.dao.DAOException;
import il.ac.hit.dao.EventDAOHibernate;
import il.ac.hit.dao.IEventDAO;
import il.ac.hit.utils.Event;
import il.ac.hit.utils.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;
import java.util.List;


@Path("/user")
public class RESTfulController {
    /**
     * This class purposes is to give a web client a service with certain constrains who's used
     * to give access to a "Diary" based application on the Model side who acts as a controller.
     *
     * this class will help you gain access to a MySQL data base using hibernate and give functionalities like
     * 1> Saving a new user by his email as @NotNull String and password as a @NotNull String
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
     * this glass URI is "http://localhost:8085/MyDiary/user/{method path name}
     *
     * methods path names:
     * newuser,verifyuser,newevent,deleteevent,deleteevents,getevent,getevents,updateevent,
     *
     * this class communicates with the client via input stream of Json strings.
     *  fyi we use GSON jar v 2.8.6 as a tool to parse the json string
     * and returns a json as a String back to a client and a HTTP Response status codes
     * */




    @Path("/newuser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response CreateNewUser(InputStream inputStream) {
        /**
         *
         * Creating a new user, the Client should send a JsonObject and in return will get an HTTP response status codes
         * HTTP method "POST"
         *     the jsonObjects includes two json elements: "email" and "password"
         *
         *     HTTP response status codes return:
         *     200 (ok) in case the new user was added
         *     418 in case the there is an existing user with the same name
         *     400 error parsing the jsonObject or invalid user email or password
         *
         *     example:
         *     URL: "http://localhost:8085/MyDiary/user/newuser
         *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
         *                                Property2: "password", value: "a1b1"
         *
         * */
        Response response = null;
        JsonObject json;

        try {

            json=inputStreamToJson(inputStream);
            User newUser = jsonToUserConverter(json);

            IEventDAO DAO = EventDAOHibernate.getInstance();
            if (DAO.createNewUser(newUser)) {
                // return HTTP response 200 in case of success
                response = Response.status(200).build();
            } else {
                // return HTTP response 418 in case we coudlnt create a new user because of a Hibernate problem or user allready Exists
                response = Response.status(418).entity("could'nt create new user, allready exists").build();

            }
        } catch (IOException IOe) {
            response = Response.status(400).entity("Invalid email or password.").build();

        } catch (Exception e) {

            response = Response.status(400).entity("Error Parsing.").build();
        }
        return response;
    }
/*

for future use!

    @Path("/deleteuserandevents")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response DeleteUser(InputStream inputStream)
    {
        Response response=null;
        JsonObject json;

        User user;

        try
        {
            json = inputStreamToJson(inputStream);
            user=jsonToUserConverter(json);

        } catch (Exception e) {
            e.printStackTrace();
            return  Response.status(400).entity("Error Parsing.").build();
        }


        try {
            IEventDAO DAO = EventDAOHibernate.getInstance();


            if(DAO.userVerification(user))
            {
                DAO.deleteUser(user);
                // return HTTP response 200 in case of success
                response= Response.status(200).build();

            }
            else
            {
                // return HTTP response 406 in case the user doesnt exists
                response= Response.status(406).entity("no such user in DataBase").build();

            }

        } catch (DAOException e) {
            e.printStackTrace();
            //problems with Hibernate or DB
            return Response.status(503).entity(e.getMessage()).build();
        }
        finally {
            return response;
        }
    }
*/
    @Path("/verifyuser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response userVerification(InputStream inputStream)
    /**
     *
     * Creating verifies a user.
     * the Client should send a JsonObject and in return will get an HTTP response status codes
     * HTTP method "POST"
     *     the jsonObjects includes two json elements: "email" and "password"
     *
     *     HTTP response status codes return:
     *     200 (ok) in case the user exists with the same password
     *     401 unauthorized user
     *     400 error parsing the jsonObject or invalid user email or password
     *     503 service unavailable
     *
     *     example:
     *     URL: "http://localhost:8085/MyDiary/user/verifyuser
     *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
     *                                Property2: "password", value: "a1b1"
     * */
    {

        Response response=null;
        JsonObject json;

        User user;

        try
        {
            json = inputStreamToJson(inputStream);
            user=jsonToUserConverter(json);

        } catch (Exception e) {
            e.printStackTrace();
            return  Response.status(400).entity("Error Parsing.").build();
        }


        try {
            IEventDAO DAO = EventDAOHibernate.getInstance();
            if(DAO.userVerification(user))
            {
                //ok
                response=Response.status(200).build();
            }
            else
            {
                //Unauthorized 401
                response=Response.status(401,"reason").build();

            }



        } catch (DAOException e) {
            e.printStackTrace();
            response=Response.status(503).build();

        }
        finally {
            return response;
        }


    }


    @Path("/newevent")
    @POST
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
         *     200 (ok) in case the user exists with the same password
         *     401 unauthorized user
         *     400 error parsing the jsonObject or invalid user email or password
         *     503 service unavailable
         *
         *     example:
         *     URL: "http://localhost:8085/MyDiary/user/newevent
         *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
         *                                Property2: "password", value: "a1b1"
         *                                Property3: "event", value: jsonArray
         * */

        JsonObject json;
        User user;
        Event event;
        try {
            json = inputStreamToJson(inputStream);
            user = jsonToUserConverter(json);
            event=jsonToEventConverter(json);

            IEventDAO DAO = EventDAOHibernate.getInstance();

            if(DAO.userVerification(user))
            {
                DAO.insertEvent(user,event);
                return Response.status(200).build();//Success
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


    @Path("/deleteevent")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response DeleteEvent(InputStream inputStream) {
        /**
         *
         * Deleting Existing event.
         * the Client should send a JsonObject and in return will get an HTTP response status codes
         * HTTP method "POST"
         *     the jsonObjects includes three json elements: "email" , "password" and an Event object as a JsonArray
         *
         *
         *     HTTP response status codes return:
         *     200 (ok) in case the user exists with the same password
         *     401 unauthorized user
         *     400 error parsing the jsonObject or invalid user email or password
         *     503 service unavailable
         *
         *     example:
         *     URL: "http://localhost:8085/MyDiary/user/deleteevent
         *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
         *                                Property2: "password", value: "a1b1"
         *                                Property3: "eventId", "56"
         * */
        JsonObject json;
        User user;
        Event event;

        try {
            json = inputStreamToJson(inputStream);
            user = jsonToUserConverter(json);
            event = jsonToEventConverter(json);

            IEventDAO DAO = EventDAOHibernate.getInstance();

            if(DAO.userVerification(user))
            {
                DAO.deleteEvent(user,event);
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

    @Path("/deleteevents")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    /**
     *
     * Deleting Existing events.
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
     *     URL: "http://localhost:8085/MyDiary/user/deleteevents
     *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
     *                                Property2: "password", value: "a1b1"
     * */
    public Response deleteEvents(InputStream inputStream)
    {
        User user;
        try {
            JsonObject jsonObject=inputStreamToJson(inputStream);
            user=jsonToUserConverter(jsonObject);

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



    @Path("/getevent")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvent(InputStream inputStream)
    {
        /**
         *
         * get Existing event.
         * the Client should send a JsonObject and in return will get an HTTP response status codes
         * this method will return the user's event in Data base as a json string
         * HTTP method "POST"
         *     the jsonObjects that in the Request body includes three json elements: "email" , "password" and "eventId"
         *
         *
         *     HTTP response status codes return:
         *     200 (ok) in case the user exists with the same password
         *     401 unauthorized user
         *     400 error parsing the jsonObject or invalid user email or password
         *     503 service unavailable
         *
         *     example:
         *     URL: "http://localhost:8085/MyDiary/user/getevent
         *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
         *                                Property2: "password", value: "a1b1"
         *                                Property3: "eventId", value: "56"
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
        Long eventId;
        Event event;
        try {
             jsonObject=inputStreamToJson(inputStream);
             user=jsonToUserConverter(jsonObject);
             eventId=jsonObject.get("eventId").getAsLong();

             IEventDAO DAO=EventDAOHibernate.getInstance();
             if(DAO.userVerification(user))
             {
                 event = DAO.getEvent(user,eventId);
                 Gson gson=new Gson();

                 return Response.ok(gson.toJson(event)).build();
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


    @Path("/getevents")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvents(InputStream inputStream)
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
         *     URL: "http://localhost:8085/MyDiary/user/getevents
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
            jsonObject=inputStreamToJson(inputStream);
            user=jsonToUserConverter(jsonObject);

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

    @Path("/updateevent")
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
         *     the jsonObjects that in the Request body includes three json elements: "email", "password" and "eventId"
         *
         *
         *     HTTP response status codes return:
         *     200 (ok) in case the user exists with the same password
         *     401 unauthorized user
         *     400 error parsing the jsonObject or invalid user email or password
         *     503 service unavailable
         *
         *     example:
         *     URL: "http://localhost:8085/MyDiary/user/updateevent
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
            jsonObject=inputStreamToJson(inputStream);
            user=jsonToUserConverter(jsonObject);
            event=jsonToEventConverter(jsonObject);




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


    private JsonObject inputStreamToJson(InputStream inputStream) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        String jsonString = "";
        while ((line = in.readLine()) != null) {
            jsonString += line;
        }


        JsonObject convertedObject = new Gson().fromJson(jsonString, JsonObject.class);
        return convertedObject;


    }


    private User jsonToUserConverter(JsonObject jsonObject) throws IOException {

        User user=new User(jsonObject.get("email").getAsString(),jsonObject.get("password").getAsString());

        return user;

    }
    private Event jsonToEventConverter(JsonObject jsonObject) throws IOException {

        //throws IOException if incorrect input

        // public Event(@NotNull String email, Long id, @NotNull String title, String location, Time timeStart, Time timeEnd, Date date, String note)
        //                                 0         1                   2                3             4              5            6              7
        JsonArray jsonArray = jsonObject.getAsJsonArray("event");

        //test
        Event event = new Event(
                jsonArray.get(0).getAsString(),
                jsonArray.get(1).getAsLong(),
                jsonArray.get(2).getAsString(),
                jsonArray.get(3).getAsString(),
                Time.valueOf(jsonArray.get(4).getAsString()),
                Time.valueOf(jsonArray.get(5).getAsString()),
                Date.valueOf(jsonArray.get(6).getAsString()),
                jsonArray.get(7).getAsString());

        return event;

    }
}
