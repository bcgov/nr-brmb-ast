package ca.bc.gov.webade;

import java.io.Serializable;

/**
 * Contains all information about an application role. This information is read
 * from the WebADE Directory.
 */
public final class Role implements Serializable {
    private static final long serialVersionUID = 5403173385611157722L;
    private String name;
    private Action[] actions = null;

    /**
     * Creates a new Role object with the same settings as the given role.
     * 
     * @param role
     */
    public Role(Role role) {
        this.name = role.name;
        this.actions = new Action[role.actions.length];
        System.arraycopy(role.actions, 0, this.actions, 0, role.actions.length);
    }

    /**
     * Create a role with all associated security information.
     * 
     * @param name
     *            The application-unique name of the role.
     * @param actions
     *            The set of actions assigned to this role.
     */
    public Role(String name, Action[] actions) {
        this.name = name;
        this.actions = (actions == null) ? new Action[0] : actions;
    }

    /**
     * Return Role Name
     * 
     * @return The application-unique name of the role.
     */
    public String getName() {
        return name;
    }

    /**
     * get the names of the actions mapped to this role. If no actions were set,
     * just return null.
     * 
     * @return The set of actions assigned to this role.
     */
    public Action[] getActions() {
        return actions;
    }

    /**
     * Returns true if the given action is found in this role.
     * @param action The target action to find.
     * @return True if found in this role's actions.
     */
    public boolean hasAction(Action action) {
        boolean hasAction = false;
        for (int i = 0; i < this.actions.length; i++) {
            Action current = this.actions[i];
            if (current.equals(action)) {
                hasAction = true;
            }
        }
        return hasAction;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        return (obj instanceof Role && ((Role)obj).getName().equals(getName()));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
        return getName().hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return getName();
    }
}