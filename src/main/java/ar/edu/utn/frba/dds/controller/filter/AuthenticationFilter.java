package ar.edu.utn.frba.dds.controller.filter;

import ar.edu.utn.frba.dds.domain.Usuario;
import ar.edu.utn.frba.dds.external.UsuarioService;
import com.google.common.base.Strings;
import org.eclipse.jetty.http.HttpStatus;
import spark.Filter;

import java.util.Optional;

import static spark.Spark.halt;

public class AuthenticationFilter {

    private static UsuarioService usuarioService = new UsuarioService();

    public static Filter authenticate = (request, response) -> {

        final String xUser = request.headers("X-User");

        if (Strings.isNullOrEmpty(xUser)) {
            halt(HttpStatus.UNAUTHORIZED_401);
        }

        final Optional<Usuario> maybeUsuario = usuarioService.fetchUsuario(xUser);

        if (maybeUsuario.isPresent()) {
            request.session().attribute("user", maybeUsuario.get());
        } else {
            halt(HttpStatus.UNAUTHORIZED_401);
        }
    };
}
