package il.ac.hit.tests;

import org.junit.jupiter.api.Test;


class RESTfulClientTest {

    @Test
    void addUserTest() {
       RESTfulClient.addUserTest();



    }


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
    void getEvents() {
        RESTfulClient.getEvents();
    }

    @Test
    void getAllUsersEvents() {
        RESTfulClient.getAllUsersEvents();
    }

    @Test
    void updateEvent() {
        RESTfulClient.UpdateEvent();

    }

    @Test
    void deleteUser() {
        RESTfulClient.deleteUser();
    }


    @Test
    void deleteAllUsersEvents() {
        RESTfulClient.deleteAllUsersEvents();
    }
}