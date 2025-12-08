package ca.bc.gov.webade.datastore;

import java.lang.reflect.Method;

/**
 * @author : Vivid Solutions Inc.
 * @version 1.0
 */

/**
 * Provides the Web ADE database connection credentials.
 */
public final class WebADEConnection {
    /** The default pool minimum connections value. */
    public static final int DEFAULT_MIN_CONNECTIONS = 0;
    /** The default pool maximum connections value. */
    public static final int DEFAULT_MAX_CONNECTIONS = 10;
    /** The default pool connection maximum idle time value. */
    public static final int DEFAULT_MAX_IDLE_TIME = 10;
    /** The default pool connection maximum checkout time value. */
    public static final int DEFAULT_MAX_CHECKOUT_TIME = 10;
    /** The default pool connection maximum wait time value. */
    public static final int DEFAULT_MAX_WAIT_TIME = 10;
    /** The default pool sleep time value. */
    public static final int DEFAULT_MONITOR_SLEEP_TIME = 1;
    /** The default pool connections flag value. */
    public static final boolean DEFAULT_POOL_CONNECTIONS_FLAG = true;
    /** The default ping connections flag value. */
    public static final boolean DEFAULT_PING_CONNECTIONS_FLAG = false;

    private static final String DETAILS_CLASS = "ca.bc.gov.webade.WebADEConnectionDetails";
    private static final String SET_DATASTORE_METHOD = "setWebadeDatastoreImplementation";
    private static final String SET_ENVIRONMENT_METHOD = "setWebadeEnvironment";
    private static final String SET_URL_METHOD = "setWebadeJdbcUrl";
    private static final String SET_USERID_METHOD = "setWebadeUserid";
    private static final String SET_PASSWORD_METHOD = "setWebadePassword";
    private static final String SET_MIN_CONNECTIONS_METHOD = "setWebadeMinConnections";
    private static final String SET_MAX_CONNECTIONS_METHOD = "setWebadeMaxConnections";
    private static final String SET_MONITOR_SLEEP_TIME_METHOD = "setMonitorSleepTime";
    private static final String SET_PING_CONNECTIONS_FLAG_METHOD = "setPingConnectionsFlag";
    private static final String SET_POOL_CONNECTIONS_FLAG_METHOD = "setPoolConnectionsFlag";
    private static final String SET_WEBADE_MAX_CONNECTION_CHECKOUT_TIME_METHOD = "setWebadeMaxConnectionCheckoutTime";
    private static final String SET_WEBADE_MAX_CONNECTION_IDLE_TIME_METHOD = "setWebadeMaxConnectionIdleTime";
    private static final String SET_WEBADE_MAX_CONNECTION_WAIT_TIME_METHOD = "setWebadeMaxConnectionWaitTime";

    /*
     * Environment information. Current possible environments are DEV, TEST,
     * PROD
     */
    private static final String WEBADE_ENVIRONMENT = "DEV";

    /*
     * URL to connect to ADE database
     */
    private static final String WEBADE_JDBCURL = "jdbc:oracle:thin:@oradb19c.vividsolutions.com:1521:mofdev";

    /*
     * Userid to connect to ADE database
     */
    private static final String WEBADE_USERID = "PROXY_WEBADE";

    /*
     * Password to connect to ADE database
     */
    private static final String WEBADE_PASSWORD = "password";

    /*
     * Specify the minimum number of connections that can be pooled for the
     * WebADE database connection pool.
     */
    private static final int WEBADE_MIN_CONNECTIONS = DEFAULT_MIN_CONNECTIONS;

    /*
     * Specify the maximum number of connections that can be pooled for the
     * WebADE database connection pool.
     */
    private static final int WEBADE_MAX_CONNECTIONS = DEFAULT_MAX_CONNECTIONS;

    /*
     * The maximum time, in minutes, a connection should remain open while
     * available in the pool (0 if the connection should be left open
     * indefinitely).
     */
    private static final int WEBADE_MAX_CONNECTION_IDLE_TIME = DEFAULT_MAX_IDLE_TIME;

    /*
     * The maximum time, in minutes, a connection should remain checked out of
     * the pool (0 if the connection should be left open indefinitely).
     */
    private static final int WEBADE_MAX_CONNECTION_CHECKOUT_TIME = DEFAULT_MAX_CHECKOUT_TIME;

    /*
     * The maximum time, in milliseconds, a thread should block while waiting
     * for a connection from the pool (0 if the thread should block
     * indefinitely, -1 of it should never block).
     */
    private static final int WEBADE_MAX_CONNECTION_WAIT_TIME = DEFAULT_MAX_WAIT_TIME;

    /*
     * The wait time, in minutes, the monitor thread will wait between
     * connection pool checks.
     */
    private static final int WEBADE_MONITOR_SLEEP_TIME = DEFAULT_MONITOR_SLEEP_TIME;

    /*
     * A flag indicating whether or not to pool database connections.
     */
    private static final boolean WEBADE_POOL_CONNECTIONS_FLAG = DEFAULT_POOL_CONNECTIONS_FLAG;

    /*
     * A flag indicating whether or not to test every database connection before
     * allowing it to be checked out. Acceptable values are true or false.
     */
    private static final boolean WEBADE_PING_CONNECTIONS_FLAG = DEFAULT_PING_CONNECTIONS_FLAG;

    /*
     * WebADE Datastore implementation to use to connect to the WebADE database.
     */
    private static final String WEBADE_DATASTORE_IMPLEMENTATION = "ca.bc.gov.webade.DefaultWebADEDatabaseDatastore";

    /**
     * Loads the given ca.bc.gov.webade.WebADEConnectionDetails details object
     * with the WebADE database connection information.
     *
     * @param loader
     *            The class loader used to load the WebADEConnectionDetails
     *            Class object.
     * @param detailsObject
     *            The details object to populate.
     * @throws Exception
     *             Thrown if the reflection calls cause errors, or the user
     *             passed in an object that is not an instance of
     *             WebADEConnectionDetails.
     */
    public static final void loadWebADEConnectionDetails(ClassLoader loader, Object detailsObject)
            throws Exception {
        Class<?> detailsClass = loader.loadClass(DETAILS_CLASS);
        if (detailsClass.isInstance(detailsObject)) {
            Method method;
            method = detailsClass.getMethod(SET_DATASTORE_METHOD, new Class[] {String.class});
            method.invoke(detailsObject, new Object[] {WEBADE_DATASTORE_IMPLEMENTATION});
            method = detailsClass.getMethod(SET_ENVIRONMENT_METHOD, new Class[] {String.class});
            method.invoke(detailsObject, new Object[] {WEBADE_ENVIRONMENT});
            method = detailsClass.getMethod(SET_URL_METHOD, new Class[] {String.class});
            method.invoke(detailsObject, new Object[] {WEBADE_JDBCURL});
            method = detailsClass.getMethod(SET_USERID_METHOD, new Class[] {String.class});
            method.invoke(detailsObject, new Object[] {WEBADE_USERID});
            method = detailsClass.getMethod(SET_PASSWORD_METHOD, new Class[] {String.class});
            method.invoke(detailsObject, new Object[] {WEBADE_PASSWORD});

            method = detailsClass.getMethod(SET_MIN_CONNECTIONS_METHOD, new Class[] {int.class});
            method.invoke(detailsObject, new Object[] {new Integer(WEBADE_MIN_CONNECTIONS)});
            method = detailsClass.getMethod(SET_MAX_CONNECTIONS_METHOD, new Class[] {int.class});
            method.invoke(detailsObject, new Object[] {new Integer(WEBADE_MAX_CONNECTIONS)});
            method = detailsClass.getMethod(SET_MONITOR_SLEEP_TIME_METHOD, new Class[] {int.class});
            method.invoke(detailsObject, new Object[] {new Integer(WEBADE_MONITOR_SLEEP_TIME)});
            method = detailsClass.getMethod(SET_PING_CONNECTIONS_FLAG_METHOD, new Class[] {boolean.class});
            method.invoke(detailsObject, new Object[] {new Boolean(WEBADE_PING_CONNECTIONS_FLAG)});
            method = detailsClass.getMethod(SET_POOL_CONNECTIONS_FLAG_METHOD, new Class[] {boolean.class});
            method.invoke(detailsObject, new Object[] {new Boolean(WEBADE_POOL_CONNECTIONS_FLAG)});
            method = detailsClass.getMethod(SET_WEBADE_MAX_CONNECTION_CHECKOUT_TIME_METHOD, new Class[] {int.class});
            method.invoke(detailsObject, new Object[] {new Integer(WEBADE_MAX_CONNECTION_CHECKOUT_TIME)});
            method = detailsClass.getMethod(SET_WEBADE_MAX_CONNECTION_IDLE_TIME_METHOD, new Class[] {int.class});
            method.invoke(detailsObject, new Object[] {new Integer(WEBADE_MAX_CONNECTION_IDLE_TIME)});
            method = detailsClass.getMethod(SET_WEBADE_MAX_CONNECTION_WAIT_TIME_METHOD, new Class[] {int.class});
            method.invoke(detailsObject, new Object[] {new Integer(WEBADE_MAX_CONNECTION_WAIT_TIME)});

        } else {
            throw new Exception("Provided details object is not an instance of '" + DETAILS_CLASS
                    + "'");
        }
    }
}