package ca.bc.gov.srm.farm.util;

import java.lang.reflect.Proxy;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.OperationNotSupportedException;
import javax.naming.spi.InitialContextFactory;

/**
 * Stands in for Tomcat's JNDI when running tests outside the container. Registered through
 * jndi.properties on the test classpath. Serves only the datasource the application looks up,
 * using the same connection settings as TestUtils.openConnection().
 */
public class TestInitialContextFactory implements InitialContextFactory {

  static final String DATASOURCE_NAME = "java:comp/env/jdbc/farms_rest";

  @Override
  public Context getInitialContext(Hashtable<?, ?> environment) {
    return (Context) Proxy.newProxyInstance(
        Context.class.getClassLoader(),
        new Class<?>[] { Context.class },
        (proxy, method, args) -> {
          switch (method.getName()) {
            case "lookup":
              String name = String.valueOf(args[0]);
              if (DATASOURCE_NAME.equals(name)) {
                return TestUtils.getDataSource();
              }
              throw new NameNotFoundException(name + " is not bound in the test JNDI context");
            case "close":
              return null;
            case "toString":
              return TestInitialContextFactory.class.getSimpleName() + " context";
            case "hashCode":
              return System.identityHashCode(proxy);
            case "equals":
              return proxy == args[0];
            default:
              throw new OperationNotSupportedException(method.getName() + " is not supported by the test JNDI context");
          }
        });
  }
}
