package il.ac.hit.tests;

import il.ac.hit.DAO.DAOException;
import il.ac.hit.DAO.EventDAOHibernate;
import il.ac.hit.DAO.IEventDAO;
import il.ac.hit.utils.Event;
import il.ac.hit.utils.User;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Scanner;

public class DAOActivator {

    public static void test(){

        try {
            IEventDAO DAO= EventDAOHibernate.getInstance();
             User userBen= new User("vahababen@gmail.com","123");


             try {
                 if(DAO.createNewUser(userBen)) {
                     System.out.println("\n new User added: " + userBen.toString());
                 }
             }
             catch (DAOException e)
             {

                 e.printStackTrace();
             }

             Scanner scanner = new Scanner(System. in);
             scanner. nextLine();

            System.out.println("\n Validating The user: "+userBen.toString());
            try {
                DAO.userVerification(userBen);
            }
            catch (DAOException e)
            {
                e.printStackTrace();
                System.out.println("\n user DON'T exists");
            }



            System.out.println("inserting new event under the user");


            Event eventBen=new Event(
                    userBen.getEmail()
                    , 99L
                    ,"party"
                    ,"Tel Aviv"
                    , Time.valueOf("23:00:00")
                    , Time.valueOf("03:30:00")
                    , Date.valueOf("2020-6-19")
                    ,"gonna be great");


            System.out.println("\n Event: "+eventBen.toString());
            scanner.nextLine();

            System.out.println("\n inserting the event");
            DAO.insertEvent(userBen, eventBen);
            System.out.println("\n event in database");
            scanner.nextLine();
            System.out.println("\ngetting the event");
            System.out.println("\nEvent: "+DAO.getEvent(userBen,eventBen.getId()));
            scanner.nextLine();

            System.out.println("\n\nget events\n\n");
            List<Event> list = DAO.getEvents(userBen);
            for (Event event:list) {
                System.out.println("\nevent: "+event.toString());
            }
            scanner.nextLine();

            System.out.println("Deleting user");
            DAO.deleteUser(userBen);
            System.out.println("\nuser deleted");


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}


