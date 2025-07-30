package Controller.Controllers;

import Service.IService;

public class Controller {
    IService service;

    public IService getService() {
        return service;
    }

    public void setService(IService service) {
        this.service = service;
    }

    public static void displayExceptions(Exception exception) {
        Utils.displayErrors(exception.getMessage());
    }
}
