package il.ac.hit.restful;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

//Defines the base URI for all resource URIs.
@ApplicationPath("/")

public class MyApplication extends Application{
    /** This java class declares root resource and provider classes   */
    @Override
    public Set<Class<?>> getClasses() {
        /**The method returns a non-empty collection with classes, that must be included in the published JAX-RS application
         */
         HashSet h = new HashSet<Class<?>>();
        h.add( RESTfulController.class );
        return h;
    }
}