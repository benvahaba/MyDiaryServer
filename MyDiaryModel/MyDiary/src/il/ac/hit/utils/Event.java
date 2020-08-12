package il.ac.hit.utils;


import com.sun.istack.internal.NotNull;
import il.ac.hit.DAO.DAOException;

import java.sql.Date;
import java.sql.Time;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Event {
    /**
    *Contains an event that is suitable for Hibernate. all members are private hence we use setters and getters
    * */
    private long id;
    private String email;
    private String title;
    private String location;
    private String starts;
    private String ends;
    private Date date;
    private String note;

    //by default while working with hibernate you must have a public cot'r without param's
    public Event() {
        /**
         * default Cto'r
         *
         * */

    }

    public Event(@NotNull String email, Long id, @NotNull String title, String location, String timeStart, String timeEnd, Date date, String note) throws DAOException {
        /**
         *  overloading Cto'r.
         *
         * */
        setEmail(email);
        setId(id);
        setTitle(title);
        setLocation(location);
        setStarts(timeStart);
        setEnds(timeEnd);
        setDate(date);
        setNote(note);

    }

    public String getTitle() {
        /**gets the event title as a String object, cant be Null*/
        return title;
    }


    public void setTitle(@NotNull String title) {
        /**
         * sets the event title as a String object, cant be Null
         *
         * */
        this.title = title;
    }

    public String getLocation() {
        /**
         * returns the event location as String
         *
         * */
        return location;
    }

    public void setLocation(String location) {
        /** sets the event Location as a String*/
        this.location = location;
    }

    public String getStarts() {
        /** gets the event start time as java.sql.Time object */
        return starts;
    }

    public void setStarts(String starts) throws DAOException {
        /** sets the event start time as a String and check the format, throws a DAOException in case the time format is worng */
        if(timeFormatCheck(starts)==false)
        {
            throw new DAOException("invalid Time format at:"+starts);

        }

        /** sets the event start time as java.sql.Time object */
        this.starts = starts;
    }

    public String getEnds() {
        /** gets the event end time as java.sql.Time object */
        return ends;
    }

    public void setEnds(String ends) throws DAOException {
        /** sets the event end time as a String and check the format, throws a DAOException in case the time format is worng */
        if(timeFormatCheck(starts)==false)
        {
            throw new DAOException("invalid Time format at:"+ends);

        }
        this.ends = ends;
    }

    public long getId() {
        /**get the event id, id can not be Null.*/
        return id;
    }

    public void setId(@NotNull long id) {
        /** sets the id.*/
        this.id = id;
    }

    public Date getDate() {
        /** gets the event date as import java.sql.Date object */
        return date;
    }

    public void setDate(Date date) {
        /** sets the event date as import java.sql.Date object */
        this.date = date;
    }

    public String getNote() {
        /** gets the event note as a String object*/
        return note;
    }

    public void setNote(String note) {
        /** sets the event note as a String object*/
        this.note = note;
    }

    public String getEmail() {
        /** gets the event User email as a String object*/
        return email;

    }

    public void setEmail(@NotNull String email) {
        this.email = email;
        /** sets the event User email as a String object. cant be null.*/
        /*no need to check the email because we checked it in the "user" class//*/
        }

    @Override
    public String toString() {
        /** return all the private members as {Member's name}: {Member's value} {next member's name}: {member's value} etc*/

        return "Email: "+getEmail()+" "
        +"Title: "+getTitle()+" "
        +" Location: "+getLocation()+" "
        +" Time starts: "+getStarts().toString()+" "
        +" Time ends: "+getEnds().toString()+" "
        +" ID: "+getId()+" "
        +" Date: "+getDate().toString()+" "
        +" Note: "+note+" ";
    }
    private Boolean timeFormatCheck(String time) {

        // Regex to check valid time in 24-hour format.
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the time is empty
        // return false
        if (time == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given time
        // and regular expression.
        Matcher m = p.matcher(time);

        // Return if the time
        // matched the ReGex
        return m.matches();



    }
}

