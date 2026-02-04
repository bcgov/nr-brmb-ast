package ca.bc.gov.webade;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.util.JsonUtils;
import ca.bc.gov.webade.json.JsonAction;
import ca.bc.gov.webade.json.JsonApplicationConfiguration;
import ca.bc.gov.webade.json.JsonApplicationPreference;
import ca.bc.gov.webade.json.JsonRole;
import ca.bc.gov.webade.preferences.DefaultWebADEPreference;
import ca.bc.gov.webade.preferences.DefaultWebADEPreferenceSet;
import ca.bc.gov.webade.preferences.DefaultWebADEPreferences;
import ca.bc.gov.webade.preferences.WebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreferenceSet;
import ca.bc.gov.webade.preferences.WebADEPreferenceTypeFactory;
import ca.bc.gov.webade.preferences.WebADEPreferences;
import ca.bc.gov.webade.user.UserCredentials;

public abstract class WebADEDatabaseDatastore implements WebADEDatastore, Serializable {

    private JsonApplicationConfiguration applicationConfiguration = new JsonApplicationConfiguration();

    private String appCode;
    private ArrayList<Role> roles;

    public WebADEDatabaseDatastore() {
        try (InputStream is = WebADEDatabaseDatastore.class
                .getResourceAsStream("/config/applicationConfiguration.json")) {
            if (is != null) {
                applicationConfiguration = JsonUtils.getJsonObjectMapper()
                        .readValue(is, JsonApplicationConfiguration.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load applicationConfiguration.json", e);
        }
    }

    void init(String applicationCode) throws WebADEException {
        this.appCode = applicationCode.trim().toUpperCase();

        this.roles = null; // Reset the roles cache.
    }

    @Override
    public WebADEPreferences getWebADEApplicationPreferences()
            throws WebADEException {
        List<JsonApplicationPreference> applicationPreferences = applicationConfiguration.getApplicationPreferences();
        WebADEPreferences preferences = new DefaultWebADEPreferences(WebADEPreferenceTypeFactory.APPLICATION);

        // loop through the application preferences and populate the WebADEPreferences
        // object
        for (JsonApplicationPreference applicationPreference : applicationPreferences) {

            // extract the relevant information from the applicationPreference object
            String preferenceSubType = applicationPreference.getSubTypeCode();
            String preferenceSetName = applicationPreference.getSetName();
            String prefName = applicationPreference.getName();
            WebADEPreference preference = new DefaultWebADEPreference(prefName);
            String prefValue = applicationPreference.getValue();
            preference.setPreferenceValue(prefValue);

            // add the preference to the appropriate preference set in the WebADEPreferences
            // object
            WebADEPreferenceSet prefSet = preferences.getPreferenceSet(preferenceSubType, preferenceSetName);
            if (prefSet == null) {
                prefSet = new DefaultWebADEPreferenceSet(preferenceSetName);
                preferences.addPreferenceSet(preferenceSubType, prefSet);
            }
            prefSet.addPreference(preference);
        }

        return preferences;
    }

    @Override
    public WebADEPreferences getWebADEUserPreferences(
            UserCredentials userCredentials) throws WebADEException {
        WebADEPreferences preferences = new DefaultWebADEPreferences(WebADEPreferenceTypeFactory.APPLICATION);
        return preferences;
    }

    @Override
    public WebADEPreferences getWebADEGlobalPreferences()
            throws WebADEException {
        WebADEPreferences preferences = new DefaultWebADEPreferences(WebADEPreferenceTypeFactory.GLOBAL);
        return preferences;
    }

    WebADEPreferences getWebADEPreferences() throws WebADEException {
        WebADEPreferences preferences = new DefaultWebADEPreferences(WebADEPreferenceTypeFactory.WEBADE);
        return preferences;
    }

    @Override
    public final String getApplicationCode() {
        return this.appCode;
    }

    @Override
    public final Role[] getApplicationRoles() throws WebADEException {
        List<JsonAction> jsonActions = applicationConfiguration.getActions();
        Map<String, Action> actionMap = new HashMap<>();

        // loop through the jsonActions and populate the actionMap
        for (JsonAction jsonAction : jsonActions) {
            String actionName = jsonAction.getName();
            Boolean isPriviledged = jsonAction.getPrivilegedInd();
            Action currentAction = new Action(actionName, isPriviledged);
            actionMap.put(actionName, currentAction);
        }

        List<JsonRole> jsonRoles = applicationConfiguration.getRoles();
        this.roles = new ArrayList<Role>();

        // loop through the jsonRoles and populate the roles list
        for (JsonRole jsonRole : jsonRoles) {
            String roleName = jsonRole.getName();
            List<Action> actions = new ArrayList<>();
            for (String actionName : jsonRole.getActionNames()) {
                if (actionMap.containsKey(actionName)) {
                    Action currentAction = actionMap.get(actionName);
                    actions.add(currentAction);
                }
            }
            Role role = new Role(roleName, actions.toArray(new Action[actions.size()]));
            this.roles.add(role);
        }

        return this.roles.toArray(new Role[this.roles.size()]);
    }
}
