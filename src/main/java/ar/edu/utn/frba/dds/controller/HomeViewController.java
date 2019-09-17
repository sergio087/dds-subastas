package ar.edu.utn.frba.dds.controller;

import com.google.common.collect.Maps;
import spark.ModelAndView;
import spark.TemplateViewRoute;

class HomeViewController {

    static final TemplateViewRoute home = (request, response) -> {
        return new ModelAndView(Maps.newHashMap(),"home.hbs");
    };
}
