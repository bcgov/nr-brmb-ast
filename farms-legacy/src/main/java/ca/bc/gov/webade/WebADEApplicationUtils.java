package ca.bc.gov.webade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebADEApplicationUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebADEApplicationUtils.class);

    public static Application createApplication(String appCode) throws WebADEException {
        WebADEDatastore datastore = loadDatastore(appCode);
        Application app = new WebADEDatabaseApplication(datastore);
        return app;
    }

    private static WebADEDatastore loadDatastore(String appCode) throws WebADEException {
        Class<?> datastoreClass;
        String datastoreClassName = "ca.bc.gov.webade.DefaultWebADEDatabaseDatastore";
        try {
            datastoreClass = Class.forName(datastoreClassName);
        } catch (ClassNotFoundException e) {
            logger.warn("ClassNotFoundException raised while loading datastore class.", e);
            logger.warn("Defaulting datastore class to " + DefaultWebADEDatabaseDatastore.class);
            datastoreClass = DefaultWebADEDatabaseDatastore.class;
        }
        WebADEDatastore datastore;
        try {
            datastore = (WebADEDatastore) datastoreClass.newInstance();
        } catch (InstantiationException e) {
            throw new WebADEException("InstantiationException raised "
                    + "while loading datastore class.", e);
        } catch (IllegalAccessException e) {
            throw new WebADEException("IllegalAccessException raised "
                    + "while loading datastore class.", e);
        }
        if (datastore instanceof WebADEDatabaseDatastore) {
            ((WebADEDatabaseDatastore) datastore).init(appCode);
        }
        return datastore;
    }
}
