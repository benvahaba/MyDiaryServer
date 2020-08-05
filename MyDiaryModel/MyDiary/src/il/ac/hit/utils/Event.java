package il.ac.hit.utils;


import com.sun.istack.internal.NotNull;

import java.sql.Date;
import java.sql.Time;


public class Event {
    /**
    *Contains an event that is suitable for Hibernate. all members are private hence we use setters and getters
    * */
    private long id;
    private String email;
    private String title;
    private String location;
    private Time starts;
    private Time ends;
    private Date date;
    private String note;

    //by default while working with hibernate you must have a public cot'r without param's
    public Event() {
        /**
         * default Cto'r
         *
         * */

    }

    public Event(@NotNull String email, Long id, @NotNull String title, String location, Time timeStart, Time timeEnd, Date date, String note) {
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

    public Time getStarts() {
        /** gets the event start time as java.sql.Time object */
        return starts;
    }

    public void setStarts(Time starts) {
        /** sets the event start time as java.sql.Time object */
        this.starts = starts;
    }

    public Time getEnds() {
        /** gets the event end time as java.sql.Time object */
        return ends;
    }

    public void setEnds(Time ends) {
        /** gets the event end time as java.sql.Time object */
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
}

