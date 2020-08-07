package il.ac.hit.restful;

import com.google.gson.JsonObject;
import il.ac.hit.DAO.DAOException;
import il.ac.hit.DAO.EventDAOHibernate;
import il.ac.hit.DAO.IEventDAO;
import il.ac.hit.utils.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
@Path("/user")
public class RESTUserController{
    /**
     * This class purposes is to give a web client a service with certain constrains who's used
     * to give access to a "Diary" based application on the Model side who acts as a controller.
     * this class purpose is to handle the users!
     *
     * this class will help you gain access to a MySQL data base using hibernate and give functionalities like
     * 1> Saving a new user by his email as @NotNull String and password as a @NotNull String
     * 2> verify a user by his email as @NotNull String and password as a @NotNull String
     * 3> delete a user and all of his events by getting the user's email as @NotNull String and password as a @NotNull String
     *
     * this glass URI is "http://localhost:8080/mydiary/api/user/{method path name}
     *
     * methods path names:
     * new,verify,delete
     *
     * this class communicates with the client via input stream of Json strings.
     *  fyi we use GSON jar v 2.8.6 as a tool to parse the json string
     * and returns a json as a String back to a client and a HTTP Response status codes
     * */


    @Path("/new")
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
         *     URL: "http://localhost:8080/mydiary/api/user/new
         *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
         *                                Property2: "password", value: "a1b1"
         *
         * */
        JsonObject json;

        try {

            json=RESTUtils.inputStreamToJson(inputStream);
            User newUser = RESTUtils.jsonToUserConverter(json);

            IEventDAO DAO = EventDAOHibernate.getInstance();
            if (DAO.createNewUser(newUser)) {
                // return HTTP response 201 in case of success. user created
                return Response.status(201).build();
            } else {
                // return HTTP response 418 in case we coudlnt create a new user because of a Hibernate problem or user allready Exists
                return Response.status(200).entity("could'nt create new user, allready exists").build();

            }
        } catch (IOException IOe) {
            return Response.status(400).entity("Invalid email or password.").build();

        } catch (Exception e) {

            return Response.status(400).entity("Error Parsing.").build();
        }

    }
    @Path("verify")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userVerification(InputStream inputStream)
    /**
     *
     * verifies a user.
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
     *     URL: "http://localhost:8080/mydiary/api/user/verify
     *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
     *                                Property2: "password", value: "a1b1"
     * */
    {


        JsonObject json;

        try {
        json=RESTUtils.inputStreamToJson(inputStream);
        User newUser = RESTUtils.jsonToUserConverter(json);

        IEventDAO DAO = EventDAOHibernate.getInstance();

        if (DAO.userVerification(new User(newUser.getEmail(),newUser.getPassword())))
        {
                //ok
            return Response.status(200).build();
        }
        else
            {
                //Unauthorized 401
                return Response.status(401, "reason").build();
            }
        }catch (IOException IOe)
        {
            IOe.printStackTrace();
            return Response.status(400).build();


        } catch (DAOException  e) {
            e.printStackTrace();
            return Response.status(503).build();

        }

    }
    @Path("/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(InputStream inputStream)
    /**
     *
     * delete a user and all of his events/
     * the Client should send a JsonObject and in return will get an HTTP response status codes
     * HTTP method "POST"
     *     the jsonObjects includes two json elements: "email" and "password"
     *
     *     HTTP response status codes return:
     *     200 (ok) in case the user exists with the same password
     *     406 no such user in data base
     *     400 error parsing the jsonObject or invalid user email or password
     *     503 service unavailable
     *
     *     example:
     *     URL: "http://localhost:8080/mydiary/api/user/delete
     *     POST body: JsonObject with Property1: "email", value:"vahababen@gmail.com"
     *                                Property2: "password", value: "a1b1"
     * */


    {
        Response response=null;
        JsonObject json;

        User user;

        try
        {
            json = RESTUtils.inputStreamToJson(inputStream);
            user = RESTUtils.jsonToUserConverter(json);


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


}