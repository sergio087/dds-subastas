package ar.edu.utn.frba.dds.controller;

import spark.ModelAndView;
import spark.TemplateViewRoute;

class HomeViewController {

    static final TemplateViewRoute home = (request, response) -> {
        return new ModelAndView(request.session().attribute("user"),"home.hbs");
    };
}
