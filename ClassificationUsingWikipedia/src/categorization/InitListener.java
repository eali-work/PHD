package categorization;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;

/**
 * Application Lifecycle Listener implementation class InitListener
 *
 */
public class InitListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public InitListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
        ServletContext sc = event.getServletContext();
        String host = sc.getInitParameter("host");
        String database = sc.getInitParameter("database");
        String username = sc.getInitParameter("username");
        String password = sc.getInitParameter("password");
        
        DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setHost(host);
        dbConfig.setDatabase(database);
        dbConfig.setUser(username);
        dbConfig.setPassword(password);
        dbConfig.setLanguage(Language.english);

        Wikipedia wiki = null;
        try {
			wiki = new Wikipedia(dbConfig);
		} catch (WikiInitializationException e) {
			e.printStackTrace();
		}
		
        sc.setAttribute("wiki", wiki);
    
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
