package ar.edu.utn.frba.dds.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
public class Oferta {

    private Double monto;
    private LocalDateTime fecha;
    private Usuario ofertante;

    public Oferta(Double monto, Usuario ofertante) {
        this.monto = monto;
        this.ofertante = ofertante;
        this.fecha = LocalDateTime.now();
    }
}
