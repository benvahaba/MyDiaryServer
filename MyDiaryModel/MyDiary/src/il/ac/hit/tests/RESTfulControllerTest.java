package il.ac.hit.tests;
/*
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import sun.misc.BASE64Decoder;



server side authentication:


@Path("/order-inventory")
public class RESTfulControllerTest {

    @GET
    @Path("/order/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getUserById(@PathParam("orderId") Integer orderId,
                              @HeaderParam("authorization") String authString){

        if(!isUserAuthenticated(authString)){
            return "{\"error\":\"User not authenticated\"}";
        }
        Order ord = new Order();
        ord.setCustmer("Java2Novice");
        ord.setAddress("Bangalore");
        ord.setAmount("$2000");
        return ord;
    }

    private boolean isUserAuthenticated(String authString){

        String decodedAuth = "";
        // Header is in the format "Basic 5tyc0uiDat4"
        // We need to extract data before decoding it back to original string
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        // Decode the data back to original string
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        decodedAuth = new String(bytes);
        System.out.println(decodedAuth);

        *//**
         * here you include your logic to validate user authentication.
         * it can be using ldap, or token exchange mechanism or your
         * custom authentication mechanism.
         *//*
        // your validation code goes here....

        return true;
    }


}*/
/*
client side authentication

package com.java2novice.rest.client;

        import sun.misc.BASE64Encoder;

        import com.sun.jersey.api.client.Client;
        import com.sun.jersey.api.client.ClientResponse;
        import com.sun.jersey.api.client.WebResource;

public class JersyGetClient {

    public static void main(String a[]){

        String url = "http://localhost:8080/RestfulWebServices/order-inventory/order/1016";
        String name = "java2novice";
        String password = "Simple4u!";
        String authString = name + ":" + password;
        String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
        System.out.println("Base64 encoded auth string: " + authStringEnc);
        Client restClient = Client.create();
        WebResource webResource = restClient.resource(url);
        ClientResponse resp = webResource.accept("application/json")
                .header("Authorization", "Basic " + authStringEnc)
                .get(ClientResponse.class);
        if(resp.getStatus() != 200){
            System.err.println("Unable to connect to the server");
        }
        String output = resp.getEntity(String.class);
        System.out.println("response: "+output);
    }
}*/
