package il.ac.hit.dao;
public class DAOException extends Exception
{
    /**
     * extends java.lang.Exception.
     * contains two Cto'rs
     * one with a message and the other a message and a throwable object*/
    public DAOException(String message) {
        /**gets a String message and sends it to Exception Ctor*/
        super(message); }

    public DAOException(String message, Throwable cause) {
        /**gets a String message and a Throwable Object and sends it to Exception Ctor*/
        super(message, cause);}



}
