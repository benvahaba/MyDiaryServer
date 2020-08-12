package il.ac.hit.restful;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import il.ac.hit.DAO.DAOException;
import il.ac.hit.utils.Event;
import il.ac.hit.utils.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class RESTUtils {


    public static JsonObject inputStreamToJson(InputStream inputStream) throws IOException
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


    public static User jsonToUserConverter(JsonObject jsonObject) throws IOException {

        User user=new User(jsonObject.get("email").getAsString(),jsonObject.get("password").getAsString());

        return user;

    }
    public static Event jsonToEventConverter(JsonObject jsonObject) throws IOException, DAOException {

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
                jsonArray.get(4).getAsString(),
                jsonArray.get(5).getAsString(),
                Date.valueOf(jsonArray.get(6).getAsString()),
                jsonArray.get(7).getAsString());

        return event;

    }
    public static List<Long> jsonToEventsIdListConverter(JsonObject jsonObject) throws IOException {


        List<Long> events=new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray("eventsId");

        //test
        for (int i=0;i<jsonArray.size();i++)
        {
            events.add(jsonArray.get(i).getAsLong());
        }
        return events;

    }

}
