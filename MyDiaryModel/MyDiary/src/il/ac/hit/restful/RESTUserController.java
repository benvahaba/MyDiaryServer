package il.ac.hit.restful;

import com.google.gson.JsonObject;
import il.ac.hit.dao.DAOException;
import il.ac.hit.dao.EventDAOHibernate;
import il.ac.hit.dao.IEventDAO;
import il.ac.hit.utils.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
@Path("/user")
public class RESTUserController{

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
         *     URL: "http://localhost:8085/MyDiary/user/newuser
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
     *     URL: "http://localhost:8085/MyDiary/user/verifyuser
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