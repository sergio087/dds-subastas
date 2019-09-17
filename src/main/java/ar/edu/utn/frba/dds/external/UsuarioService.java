package ar.edu.utn.frba.dds.external;

import ar.edu.utn.frba.dds.domain.Usuario;
import com.google.common.collect.ImmutableSet;

import java.util.Optional;

public class UsuarioService {

    private static final ImmutableSet<Usuario> usuarios = ImmutableSet.of(
            new Usuario("user1"),
            new Usuario("user2"),
            new Usuario("user3")
    );

    public Optional<Usuario> fetchUsuario(String nombre) {
        return usuarios.stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }
}
