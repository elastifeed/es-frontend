package de.htw.saar.frontend.master;

public class MasterController
{
    /**
     * auto generates the path to a view
     * the view should be located in view/NAME_OF_CONTROLLER/NAME_OF_VIEW
     * @param name name of the view
     * @param controller the controller object
     * @return returns a path to the view
     */
    public String view(String name, Object controller)
    {
        return "/view/" + controller.getClass().getSimpleName().toLowerCase().replace("controller","") + "/" + name + ".xhtml";
    }

    /**
     * auto generates the path to a view
     * the view should be located in view/NAME_OF_CONTROLLER/NAME_OF_VIEW
     * @param name name of the view
     * @param controller name of the controller
     * @return returns a path to the view
     * @return
     */
    public String view(String name, String controller)
    {
        return "/view/" + controller.toLowerCase() + "/" + name + ".xhtml";
    }
}
