package it.acsoftware.hyperiot.contentrepository.actions;

import it.acsoftware.hyperiot.base.action.HyperIoTActionName;

/**
 * 
 * @author Aristide Cittadino Model class that enumerate ContentRepository Actions
 *
 */
public enum ContentRepositoryAction implements HyperIoTActionName {
	
	//TO DO: add enumerations here
	ACTION_ENUM("action_enum"),
	DOCUMENT_MANAGEMENT_ACTION(Names.DOCUMENT_MANAGEMENT_ACTION);

	private String name;

     /**
	 * Role Action with the specified name.
	 * 
	 * @param name parameter that represent the ContentRepository  action
	 */
	private ContentRepositoryAction(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of ContentRepository action
	 */
	public String getName() {
		return name;
	}
	public static class Names {

		private static final String DOCUMENT_MANAGEMENT_ACTION = "DOCUMENT_MANAGAMENT_ACTION";
		private Names() {
			throw new IllegalStateException("Utility class");
		}
	}

}
