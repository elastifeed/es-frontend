package de.htw.saar.frontend.master;

import de.htw.saar.frontend.controller.NavigationController;

public class MasterController
{
    /**
     * Initialize the Navigation Controller
     */
    private NavigationController navigationController = new NavigationController();

    /**
     * populate the navigation controller to access navigation of sub controller
     * @return
     */
    public NavigationController getNavigationController() {
        return navigationController;
    }
}
