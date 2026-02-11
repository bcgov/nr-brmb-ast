package ca.bc.gov.webade.developer.j2ee;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.client.utils.DateUtils;

/**
 * Request Wrapper used by the Developer Filter to append netegrity headers to
 * the request.
 * 
 * @author Vivid Solutions Inc
 */
class DeveloperRequestWrapper extends HttpServletRequestWrapper {
	
	private static final Logger logger = LoggerFactory.getLogger(DeveloperRequestWrapper.class);

    private Map<String,String> headers;

    public DeveloperRequestWrapper(HttpServletRequest request, Map<String,String> headers) {
        super(request);
        this.headers = headers;
    }

    /**
     * Overrides getHeader in request to enable addition of netegrity headers.
     */
    @Override
	public String getHeader(String name) {
        String header = super.getHeader(name);
        if (name != null) {
            if (header == null || header.equals("")) {
            	header = this.headers.get(name);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getHeader(" + name + ") = " + header);
        }
        return header;
    }

    /**
     * Overrides getDateHeader in request to enable addition of netegrity headers.
     */
    @Override
	public long getDateHeader(String name) {
    	long result = -1;
    	
    	String header = super.getHeader(name);
    	if(name != null) {
    		if(header == null || header.equals("")) {
    			header = this.headers.get(name);
    			if(header!=null) {
    				Date date = DateUtils.parseDate(header);
    				if(date==null) {
    					throw new IllegalArgumentException("Failed to parse header '"+name+"' value as a date: "+header);
    				}

    				{
    					result = date.getTime();
    				}
    			}
    		} else {
    			result = super.getDateHeader(name);
    		}
    	}
        if (logger.isDebugEnabled()) {
            logger.debug("getDateHeader(" + name + ") = " + result);
        }
    	
        return result;
    }

    /**
     * The default behavior of this method is to return getHeaders(String name)
     * on the wrapped request object.
     */
    @Override
	public Enumeration<String> getHeaders(String name) {
    	Enumeration<String> result = null;
    	
    	result = super.getHeaders(name);
    	if(name != null) {
    		if(!result.hasMoreElements()) {
    			String header = this.headers.get(name);
    			if(header!=null) {
    				Vector<String> vector = new Vector<String>();
    				vector.add(header);
    				result = vector.elements();
    			}
    		}
    	}
        if (logger.isDebugEnabled()) {
            logger.debug("getHeaders(" + name + ") = " + result);
        }
    	
        return result;
    }

    /**
     * The default behavior of this method is to return getHeaderNames()
     * on the wrapped request object.
     */
    @Override
	public Enumeration<String> getHeaderNames() {
    	Enumeration<String> result = null;
    	
    	Set<String> values = new HashSet<String>();
    	for(Enumeration<String> iter = super.getHeaderNames();iter.hasMoreElements();) {
    		values.add(iter.nextElement());
    	}
    	
    	for(String name:this.headers.keySet()) {
    		values.add(name);
    	}
    	
    	result = new Vector<String>(values).elements();
        if (logger.isDebugEnabled()) {
            logger.debug("getHeaderNames() = " + result);
        }
    	
        return result;
    }

    /**
     * The default behavior of this method is to return getIntHeader(String name)
     * on the wrapped request object.
     */
    @Override
	public int getIntHeader(String name) {
    	int result = -1;
    	
    	String header = super.getHeader(name);
    	if(name != null) {
    		if(header == null || header.equals("")) {
    			header = this.headers.get(name);
    			if(header!=null) {
    				
    				result = Integer.parseInt(header);
    			}
    		} else {
    			result = super.getIntHeader(name);
    		}
    	}
        if (logger.isDebugEnabled()) {
            logger.debug("getDateHeader(" + name + ") = " + result);
        }
    	
        return result;
    }

}
