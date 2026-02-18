/**
 * @(#)Action.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade;

import java.io.Serializable;

/**
 * @author jross
 */
public final class Action implements Serializable {
    private static final long serialVersionUID = -1510229675214284701L;
    private String name;
    private boolean isPrivileged;

    /**
     * Creates a non-priviledged action using the given action name string.
     * 
     * @param name
     *            The action name.
     */
    public Action(String name) {
        this(name, false);
    }

    /**
     * Creates an action using the given action name string and flag indicating
     * whether the action is privileged.
     * 
     * @param name
     *            The action name.
     * @param isPrivileged
     *            Flag indicating whether the action is privileged.
     */
    public Action(String name, boolean isPrivileged) {
        this.name = name;
        this.isPrivileged = isPrivileged;
    }

    /**
     * @return Returns the isPriviledged flag.
     */
    public boolean isPriviledged() {
        return isPrivileged;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        return (obj instanceof Action && getName() != null && getName().equalsIgnoreCase(((Action)obj).getName()));
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return getName();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
        return (getName() != null) ? getName().hashCode() : super.hashCode();
    }
}