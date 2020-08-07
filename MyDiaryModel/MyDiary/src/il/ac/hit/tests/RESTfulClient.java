package il.ac.hit.tests;




import com.google.gson.*;
import il.ac.hit.utils.Event;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.Time;


public class RESTfulClient {

    public static void addUserTest() {
        String tempRetString="";
        try {
            String email="vahababen@gmail.com";
            String password="a1b2";

            JsonObject jsonObject=new JsonObject();

            jsonObject.add("email", JsonParser.parseString(email) );
            jsonObject.add("password", JsonParser.parseString(password) );

            System.out.println("our json: "+ jsonObject);


            try {
                URL url = new URL("http://localhost:8080/mydiary/api/user/new");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));



                String nextLine;
                while ((nextLine =br.readLine())!=null)
                {
                    tempRetString+=nextLine;
                }
                System.out.println("\nMyDiary REST Service Invoked Successfully..\n output:"+ tempRetString+"\n");


                br.close();



            } catch (Exception e) {
                System.out.println(e);
                System.out.println("\nError while calling MyDiary REST Service: "+tempRetString);
            }


        } catch (Exception e) {
            System.out.println(e);
            System.out.println("\nError while calling MyDiary REST Service: "+tempRetString);
        }
    }
    public static void userVerification()
    {
        String string = "";
        try {
            String email="vahababen@gmail.com";
            String password="a1b2";

            String tempRetString="";

            JsonObject jsonObject=new JsonObject();

            jsonObject.add("email", JsonParser.parseString(email) );
            jsonObject.add("password", JsonParser.parseString(password) );

            System.out.println("our json: "+ jsonObject);




            try {
                URL url = new URL("http://localhost:8080/mydiary/api/user/verify");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();



                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));



                String nextLine;
                while ((nextLine =br.readLine())!=null)
                {
                    tempRetString+=nextLine;
                }
                System.out.println("\nMyDiary REST Service Invoked Successfully..\n output:"+ tempRetString+"\n");

                br.close();

            } catch (Exception e) {


                System.out.println(e);


                System.out.println("\nError while calling MyDiary REST Service: "+tempRetString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

      public static void deleteUser() {
        String string = "";
        try {
            String email="vahababen@gmail.com";
            String password="a1b2";
            String tempRetString="";

            JsonObject jsonObject=new JsonObject();

            jsonObject.add("email", JsonParser.parseString(email) );
            jsonObject.add("password", JsonParser.parseString(password) );

            System.out.println("our json: "+ jsonObject);


            try {
                URL url = new URL("http://localhost:8080/mydiary/api/user/delete");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));



                String nextLine;
                while ((nextLine =br.readLine())!=null)
                {
                    tempRetString+=nextLine;
                }
                System.out.println("\nMyDiary REST Service Invoked Successfully..\n output:"+ tempRetString+"\n");


                br.close();

            } catch (Exception e) {


                System.out.println(e);
                System.out.println("\nError while calling MyDiary REST Service: "+tempRetString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void newEvent()
    {
        String string = "";
        try {
            String email="vahababen@gmail.com";
            String password="a1b2";
            String tempRetString="";

            JsonObject jsonObject=new JsonObject();

            Event event=new Event(
                    "vahababen@gmail.com"
                    , 99L
                    ,"party"
                    ,"Tel Aviv"
                    , Time.valueOf("23:00:00")
                    , Time.valueOf("03:30:00")
                    , Date.valueOf("2020-6-19")
                    ,"gonna be great");

            JsonArray jsonArray=new JsonArray();
            jsonArray.add(event.getEmail());
            jsonArray.add(event.getId());
            jsonArray.add(event.getTitle());
            jsonArray.add(event.getLocation());
            jsonArray.add(event.getStarts().toString());
            jsonArray.add(event.getEnds().toString());
            jsonArray.add(event.getDate().toString());
            jsonArray.add(event.getNote());




            jsonObject.add("email", JsonParser.parseString(email) );
            jsonObject.add("password", JsonParser.parseString(password) );
            jsonObject.add("event",jsonArray);




            System.out.println("our json: "+ jsonObject);


            try {
                URL url = new URL("http://localhost:8080/mydiary/api/event/new");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));



                String nextLine;
                while ((nextLine =br.readLine())!=null)
                {
                    tempRetString+=nextLine;
                }
                System.out.println("\nMyDiary REST Service Invoked Successfully..\n output:"+ tempRetString+"\n");


                br.close();

            } catch (Exception e) {


                System.out.println(e);
                System.out.println("\nError while calling MyDiary REST Service: "+tempRetString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteEvent()
    {
        String string = "";
        try {
            String email="vahababen@gmail.com";
            String password="a1b2";
            String tempRetString="";

            JsonArray jsonArray=new JsonArray();
            jsonArray.add(4);

            JsonObject jsonObject=new JsonObject();


            jsonObject.add("email", JsonParser.parseString(email) );
            jsonObject.add("password", JsonParser.parseString(password) );
            jsonObject.add("eventId",jsonArray);



            try {
                URL url = new URL("http://localhost:8080/mydiary/api/user/delete");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));



                String nextLine;
                while ((nextLine =br.readLine())!=null)
                {
                    tempRetString+=nextLine;
                }
                System.out.println("\nMyDiary REST Service Invoked Successfully..\n output:"+ tempRetString+"\n");


                br.close();

            } catch (Exception e) {


                System.out.println(e);
                System.out.println("\nError while calling MyDiary REST Service: "+tempRetString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void deleteAllUsersEvents()
    {
        String string = "";
        try {
            String email="vahababen@gmail.com";
            String password="a1b2";
            String tempRetString="";


            JsonObject jsonObject=new JsonObject();


            jsonObject.add("email", JsonParser.parseString(email) );
            jsonObject.add("password", JsonParser.parseString(password) );


            try {
                URL url = new URL("http://localhost:8080/mydiary/api/event/delete/all");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));



                String nextLine;
                while ((nextLine =br.readLine())!=null)
                {
                    tempRetString+=nextLine;
                }
                System.out.println("\nMyDiary REST Service Invoked Successfully..\n output:"+ tempRetString+"\n");


                br.close();

            } catch (Exception e) {


                System.out.println(e);
                System.out.println("\nError while calling MyDiary REST Service: "+tempRetString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getEvents()
    {
        String string = "";
        try {
            String email="vahababen@gmail.com";
            String password="a1b2";
            String tempRetString="";

            JsonObject jsonObject=new JsonObject();
            JsonArray jsonArray=new JsonArray();
            jsonArray.add(JsonParser.parseString("9"));
            jsonArray.add(JsonParser.parseString("10"));

            jsonObject.add("email", JsonParser.parseString(email) );
            jsonObject.add("password", JsonParser.parseString(password) );
            jsonObject.add("eventsId",jsonArray);



            System.out.println("our json: "+ jsonObject);


            try {
                URL url = new URL("http://localhost:8080/mydiary/api/event/get");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));



                String nextLine;
                while ((nextLine =br.readLine())!=null)
                {
                    tempRetString+=nextLine;
                }
                System.out.println("\nMyDiary REST Service Invoked Successfully..\n output event: "+ tempRetString+"\n");


                br.close();

            } catch (Exception e) {


                System.out.println(e);
                System.out.println("\nError while calling MyDiary REST Service: "+tempRetString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void getAllUsersEvents()
    {
        String string = "";
        try {
            String email="vahababen@gmail.com";
            String password="a1b2";
            String tempRetString="";

            JsonObject jsonObject=new JsonObject();

            jsonObject.add("email", JsonParser.parseString(email) );
            jsonObject.add("password", JsonParser.parseString(password) );



            System.out.println("our json: "+ jsonObject);


            try {
                URL url = new URL("http://localhost:8080/mydiary/api/event/get/all");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));



                String nextLine;
                while ((nextLine =br.readLine())!=null)
                {
                    tempRetString+=nextLine;
                }
                System.out.println("\nMyDiary REST Service Invoked Successfully..\n output event: "+ tempRetString+"\n");


                br.close();

            } catch (Exception e) {


                System.out.println(e);
                System.out.println("\nError while calling MyDiary REST Service: "+tempRetString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void UpdateEvent()
    {
        String string = "";
        try {
            String email="vahababen@gmail.com";
            String password="a1b2";
            String tempRetString="";

            JsonObject jsonObject=new JsonObject();

            jsonObject.add("email", JsonParser.parseString(email) );
            jsonObject.add("password", JsonParser.parseString(password) );
            Event event=new Event(
                    "vahababen@gmail.com"
                    , 10L
                    ,"party"
                    ,"Tel Aviv"
                    , Time.valueOf("23:00:00")
                    , Time.valueOf("03:30:00")
                    , Date.valueOf("2020-6-19")
                    ,"im not gonna like it");
            JsonArray jsonArray=new JsonArray();
            jsonArray.add(event.getEmail());
            jsonArray.add(event.getId());
            jsonArray.add(event.getTitle());
            jsonArray.add(event.getLocation());
            jsonArray.add(event.getStarts().toString());
            jsonArray.add(event.getEnds().toString());
            jsonArray.add(event.getDate().toString());
            jsonArray.add(event.getNote());
            jsonObject.add("event",jsonArray);



            System.out.println("our json: "+ jsonObject);


            try {
                URL url = new URL("http://localhost:8080/mydiary/api/event/update");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));



                String nextLine;
                while ((nextLine =br.readLine())!=null)
                {
                    tempRetString+=nextLine;
                }
                System.out.println("\nMyDiary REST Service Invoked Successfully..\n output event: "+ tempRetString+"\n");


                br.close();

            } catch (Exception e) {


                System.out.println(e);
                System.out.println("\nError while calling MyDiary REST Service: "+tempRetString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
