package ar.edu.utn.frba.dds.domain;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
public class Usuario {

    @NonNull
    private String nombre;
    private List<Oferta> ofertas = Lists.newArrayList();
    private List<Subasta> subastas = Lists.newArrayList();
}
