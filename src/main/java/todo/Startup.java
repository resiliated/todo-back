package todo;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import org.jboss.logging.Logger;
import todo.model.User;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import javax.transaction.Transactional;


@Singleton
public class Startup {

    private static final Logger LOG = Logger.getLogger(Startup.class);

    void onStart(@Observes StartupEvent ev) {
        LOG.info("The application is starting with profile " + ProfileManager.getActiveProfile());
        LOG.info("Hello world");
    }

    @Transactional
    public void loadData(@Observes StartupEvent evt) {
        // reset and load all test users
        //User.deleteAll();
        if(User.findByUserName("admin") == null){
            User.add("admin", "admin", "admin");
        }
        //
        //User.add("user", "user", "user");
    }
}
