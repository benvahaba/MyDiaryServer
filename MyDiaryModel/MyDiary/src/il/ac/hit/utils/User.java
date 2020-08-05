package il.ac.hit.utils;

import com.sun.istack.internal.NotNull;

import java.io.IOException;


public class User {
    /**
     *Contains a User email and User Password.the class is suitable for Hibernate usage. all members are private hence we use setters and getters
     * */
    private String email;
    private String password;




    //by default while working with hibernate you must have a public cot'r without param's
    public User() {
        /**
         * default Cto'r
         *
         * */
    }

    public User(@NotNull String email, @NotNull String password) throws IOException {
        /**
         *  overloading Cto'r.
         *
         * */
        setEmail(email);
        setPassword(password);
    }

    public String getEmail() {

        /**returns the user email as a String Object. cant be Null*/return email;
    }

    public void setEmail(String email) throws IOException {
        /**
         * Sets the user email, the email must be bigger than 3, must contain a "@" and the "@" cant be in first or last
         * Location in the String.
         * the char's before the char @ must be Alphabetic or numbers.
         *
         * Throws a IOException
         * */


        if(email.indexOf("@")>0 && email.indexOf("@")<email.length()-1 && email.length()>3&&email.contains("@"))
        {
            String[] splitedEmail=email.split("@");
            if(isAlphaNumeric(splitedEmail[0]))
            {

                this.email=email;
            }
            else {
                throw new IOException("illegal email. AlphaNumeric");
            }

        }
        else {
            throw new IOException("illegal email");
        }


    }

    public String getPassword() {
        /**gets the user password, cant return Null.
         * */
        return password;
    }

    public void setPassword(String password) {
        /**sets the user password, cant return Null.*/

        this.password = password;
    }

    @Override
    public String toString()
    {    /** return all the private members as {Member's name}: {Member's value} {next member's name}: {member's value} etc*/
        return "\n User Email: "+ getEmail()
        +"\n User Password: "+getPassword();

    }
    private static boolean isAlphaNumeric(String s) {
        //checks if a String is Alphabetic or Numeric
        return s != null && s.matches("^[a-zA-Z0-9]*$");
    }
}

