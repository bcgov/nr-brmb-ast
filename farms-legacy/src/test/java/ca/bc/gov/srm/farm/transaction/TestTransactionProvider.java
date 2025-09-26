/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Forests and Range.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.util.TestUtils;


/**
 * TestTransactionProvider.
 */
public final class TestTransactionProvider extends TransactionProvider {

    /**
     * Creates a new WebADETransactionProvider object.
     */
	  public TestTransactionProvider() {
    }

    /**
     * close.
     *
     * @param  transaction  The parameter value.
     */
    @Override
    public void close(final Transaction transaction) {
        if (transaction != null) {
            transaction.close();
        }
    }

    /**
     * getTransaction.
     *
     * @param   businessAction  Input parameter.
     *
     * @return  The return value.
     *
     * @throws  ProviderException  On exception.
     */
    @Override
    public Transaction getTransaction(final BusinessAction businessAction) throws ProviderException {
        return getTransaction();
    }

    /**
     * getTransaction.
     *
     * @param   businessAction  Input parameter.
     * @param   user            Input parameter.
     *
     * @return  The return value.
     *
     * @throws  ProviderException  On exception.
     */
    @Override
    public Transaction getTransaction(final BusinessAction businessAction, final User user)
        throws ProviderException {
    	return getTransaction();
    }
    
    
    
    /**
     * getTransaction.
     *
     * @return  The return value.
     *
     * @throws  ProviderException  On exception.
     */
    @SuppressWarnings("resource")
    public Transaction getTransaction() throws ProviderException {
        Transaction result = null;

        try {
            result = new WebADETransaction();
            Connection con = TestUtils.openConnection();
            result.setDatastore(con);
            return result;

        } catch (SQLException e) {
            getLog().error("SQLException on getTransaction: " + e.getMessage());
            throw new ProviderException(e);
        }

    }

    /**
     * rollback.
     *
     * @param  transaction  The parameter value.
     */
    @Override
    public void rollback(final Transaction transaction) {
        if (transaction != null) {
            transaction.rollback();
        }
    }
    
    /**
     * initialize.
     *
     * @param  resource  The parameter value.
     */
    @Override
    public void initialize(final Object resource) {
      // do nothing
    }

}
