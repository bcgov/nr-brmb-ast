/**
 * @(#)WebADEConnection.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.dbpool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jross
 */
public class WrapperConnection implements Connection {
	
	private static final Logger log = LoggerFactory.getLogger(WrapperConnection.class);
    private Connection conn;
    private StackTraceElement[] callerStacktrace;

    /**
     * Creates a wrapper <code>Connection</code> object around the actual
     * Connection object, to allow the garbage collector to clean up the
     * connection object if the developer does not call <code>close()</code>
     * in the application's code.
     * 
     * @param conn
     *            The Opened connection open.
     * @param callerStacktrace
     *            The stacetrace of the caller, used when the connection is not
     *            properly closed.
     */
    WrapperConnection(Connection conn, StackTraceElement[] callerStacktrace) {
        this.conn = conn;
        this.callerStacktrace = callerStacktrace;
    }

    /**
     * @return The underlying Connection instance.
     */
    public Connection getWrappedConnection() {
        return this.conn;
    }

    /**
     * @see java.sql.Connection#createStatement()
     */
    @Override
	public Statement createStatement() throws SQLException {
        Statement stmt = this.conn.createStatement();

        log.trace("Connection: " + this.hashCode() + " returning Statement: " + stmt.hashCode());
        return stmt;
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    @Override
	public PreparedStatement prepareStatement(String arg0) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement(arg0);
        log.trace("Connection: " + this.hashCode() + " returning Statement: " + stmt.hashCode());
        return stmt;
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String)
     */
    @Override
	public CallableStatement prepareCall(String arg0) throws SQLException {
        CallableStatement stmt = this.conn.prepareCall(arg0);
        log.trace("Connection: " + this.hashCode() + " returning Statement: " + stmt.hashCode());
        return stmt;
    }

    /**
     * @see java.sql.Connection#nativeSQL(java.lang.String)
     */
    @Override
	public String nativeSQL(String arg0) throws SQLException {
        return this.conn.nativeSQL(arg0);
    }

    /**
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    @Override
	public void setAutoCommit(boolean arg0) throws SQLException {
        this.conn.setAutoCommit(arg0);
    }

    /**
     * @see java.sql.Connection#getAutoCommit()
     */
    @Override
	public boolean getAutoCommit() throws SQLException {
        return this.conn.getAutoCommit();
    }

    /**
     * @see java.sql.Connection#commit()
     */
    @Override
	public void commit() throws SQLException {
        this.conn.commit();
    }

    /**
     * @see java.sql.Connection#rollback()
     */
    @Override
	public void rollback() throws SQLException {
        this.conn.rollback();
    }

    /**
     * @see java.sql.Connection#close()
     */
    @Override
	public void close() throws SQLException {
        this.conn.close();
    }

    /**
     * @see java.sql.Connection#isClosed()
     */
    @Override
	public boolean isClosed() throws SQLException {
        return this.conn.isClosed();
    }

    /**
     * @see java.sql.Connection#getMetaData()
     */
    @Override
	public DatabaseMetaData getMetaData() throws SQLException {
        return this.conn.getMetaData();
    }

    /**
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    @Override
	public void setReadOnly(boolean arg0) throws SQLException {
        this.conn.setReadOnly(arg0);
    }

    /**
     * @see java.sql.Connection#isReadOnly()
     */
    @Override
	public boolean isReadOnly() throws SQLException {
        return this.conn.isReadOnly();
    }

    /**
     * @see java.sql.Connection#setCatalog(java.lang.String)
     */
    @Override
	public void setCatalog(String arg0) throws SQLException {
        this.conn.setCatalog(arg0);
    }

    /**
     * @see java.sql.Connection#getCatalog()
     */
    @Override
	public String getCatalog() throws SQLException {
        return this.conn.getCatalog();
    }

    /**
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    @Override
	public void setTransactionIsolation(int arg0) throws SQLException {
        this.conn.setTransactionIsolation(arg0);
    }

    /**
     * @see java.sql.Connection#getTransactionIsolation()
     */
    @Override
	public int getTransactionIsolation() throws SQLException {
        return this.conn.getTransactionIsolation();
    }

    /**
     * @see java.sql.Connection#getWarnings()
     */
    @Override
	public SQLWarning getWarnings() throws SQLException {
        return this.conn.getWarnings();
    }

    /**
     * @see java.sql.Connection#clearWarnings()
     */
    @Override
	public void clearWarnings() throws SQLException {
        this.conn.clearWarnings();
    }

    /**
     * @see java.sql.Connection#createStatement(int, int)
     */
    @Override
	public Statement createStatement(int arg0, int arg1) throws SQLException {
        Statement stmt = this.conn.createStatement(arg0, arg1);
        log.trace("Connection: " + this.hashCode() + " returning Statement: " + stmt.hashCode());
        return stmt;
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
     */
    @Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement(arg0, arg1, arg2);
        log.trace("Connection: " + this.hashCode() + " returning Statement: " + stmt.hashCode());
        return stmt;
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     */
    @Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2) throws SQLException {
        CallableStatement stmt = this.conn.prepareCall(arg0, arg1, arg2);
        log.trace("Connection: " + this.hashCode() + " returning Statement: " + stmt.hashCode());
        return stmt;
    }

    /**
     * @see java.sql.Connection#getTypeMap()
     */
    @Override
	public Map<String,Class<?>> getTypeMap() throws SQLException {
        return this.conn.getTypeMap();
    }

    /**
     * @see java.sql.Connection#setTypeMap(java.util.Map)
     */
    @Override
	public void setTypeMap(Map<String,Class<?>> arg0) throws SQLException {
        this.conn.setTypeMap(arg0);
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     */
    @Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3)
            throws SQLException {
        return this.conn.prepareCall(arg0, arg1, arg2, arg3);
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int,
     *      int)
     */
    @Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3)
            throws SQLException {
        return this.conn.prepareStatement(arg0, arg1, arg2, arg3);
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     */
    @Override
	public PreparedStatement prepareStatement(String arg0, int arg1) throws SQLException {
        return this.conn.prepareStatement(arg0, arg1);
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     */
    @Override
	public PreparedStatement prepareStatement(String arg0, int[] arg1) throws SQLException {
        return this.conn.prepareStatement(arg0, arg1);
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String,
     *      java.lang.String[])
     */
    @Override
	public PreparedStatement prepareStatement(String arg0, String[] arg1) throws SQLException {
        return this.conn.prepareStatement(arg0, arg1);
    }

    /**
     * @see java.sql.Connection#setHoldability(int)
     */
    @Override
	public void setHoldability(int arg0) throws SQLException {
        this.conn.setHoldability(arg0);
    }

    /**
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    @Override
	public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException {
        return this.conn.createStatement(arg0, arg1, arg2);
    }

    /**
     * @see java.sql.Connection#getHoldability()
     */
    @Override
	public int getHoldability() throws SQLException {
        return this.conn.getHoldability();
    }

    /**
     * @see java.sql.Connection#setSavepoint()
     */
    @Override
	public Savepoint setSavepoint() throws SQLException {
        /*
         * Commented out to allow compilation with the Oracle classes jar for
         * Java 1.2
         */
        // return this.conn.setSavepoint();
        throw new UnsupportedOperationException("This method is not currently supported by WebADE");
    }

    /**
     * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     */
    @Override
	public void releaseSavepoint(Savepoint arg0) throws SQLException {
        /*
         * Commented out to allow compilation with the Oracle classes jar for
         * Java 1.2
         */
        // this.conn.releaseSavepoint(arg0);
        throw new UnsupportedOperationException("This method is not currently supported by WebADE");
    }

    /**
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    @Override
	public void rollback(Savepoint arg0) throws SQLException {
        /*
         * Commented out to allow compilation with the Oracle classes jar for
         * Java 1.2
         */
        // this.conn.rollback(arg0);
        throw new UnsupportedOperationException("This method is not currently supported by WebADE");
    }

    /**
     * @see java.sql.Connection#setSavepoint(java.lang.String)
     */
    @Override
	public Savepoint setSavepoint(String arg0) throws SQLException {
        /*
         * Commented out to allow compilation with the Oracle classes jar for
         * Java 1.2
         */
        // return this.conn.setSavepoint(arg0);
        throw new UnsupportedOperationException("This method is not currently supported by WebADE");
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object arg0) {
        return this.conn.equals(arg0);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
        return this.conn.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return this.conn.toString();
    }

    /**
     * @see java.lang.Object#finalize()
     */
    @Override
	protected void finalize() throws Throwable {
        if (!this.conn.isClosed()) {
            String message = "Connection #" + this.conn.hashCode()
                    + " closed by garbage collector.  Connection check-out call stack trace follows:\n";
            message += createCallerStacktrace(this.callerStacktrace);
            log.warn(message);
            this.conn.close();
        } else {
            log.trace("Connection #" + this.conn.hashCode() + " closed properly");
        }
    }

    private String createCallerStacktrace(StackTraceElement[] callerStacktrace) {
        String message = "";
        for (int x = 0; x < callerStacktrace.length; x++) {
            String lineNumber = (callerStacktrace[x].getLineNumber() > 0) ? ""
                    + callerStacktrace[x].getLineNumber() : "Native Method";
            String lineMessage = "\tat " + callerStacktrace[x].getClassName() + "."
                    + callerStacktrace[x].getMethodName() + "(" + callerStacktrace[x].getFileName()
                    + ":" + lineNumber + ")\n";
            message += lineMessage;
        }
        return message;
    }

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.conn.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.conn.isWrapperFor(iface);
	}

	@Override
	public Clob createClob() throws SQLException {
		return this.conn.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return this.conn.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return this.conn.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return this.conn.createSQLXML();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return this.conn.isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		this.conn.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		this.conn.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return this.conn.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return this.conn.getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return this.conn.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return this.conn.createStruct(typeName, attributes);
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		this.conn.setSchema(schema);
	}

	@Override
	public String getSchema() throws SQLException {
		return this.conn.getSchema();
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		this.conn.abort(executor);
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		this.conn.setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return this.conn.getNetworkTimeout();
	}
}