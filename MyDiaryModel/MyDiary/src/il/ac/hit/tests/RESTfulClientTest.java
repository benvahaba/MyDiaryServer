package il.ac.hit.tests;

import org.junit.jupiter.api.Test;


class RESTfulClientTest {

    @Test
    void addUserTest() {
       RESTfulClient.addUserTest();



    }

/*    @Test
    void deleteUser() {
        RESTfulClient.deleteUser();
    }*/


    @Test
    public void userVerification() {
        RESTfulClient.userVerification();
    }

    @Test
    void newEvent() {
        RESTfulClient.newEvent();;
    }

    @Test
    void deleteEvent() {
        RESTfulClient.deleteEvent();
    }

    @Test
    void deleteEvents() {
        RESTfulClient.deleteEvents();
    }

    @Test
    void getEvent() {
        RESTfulClient.getEvent();
    }

    @Test
    void getEvents() {
        RESTfulClient.getEvents();
    }

    @Test
    void updateEvent() {
        RESTfulClient.UpdateEvent();

    }
}