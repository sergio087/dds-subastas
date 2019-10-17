package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.exception.EstadoSubastaInvalidoException;
import ar.edu.utn.frba.dds.domain.exception.OfertaInvalidaException;
import ar.edu.utn.frba.dds.domain.exception.SubastaInvalida;
import ar.edu.utn.frba.dds.repository.EntityObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(callSuper = true, exclude = {"ofertas", "etiquetas", "ganador", "oferente"})
@AllArgsConstructor
@NoArgsConstructor
public class Subasta extends EntityObject<Integer> {

    private String producto;
    @Transient
    private List<String> etiquetas;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private EstadoSubasta estado;
    @Transient
    private List<Oferta> ofertas;
    private Double montoMinimo;
    @Transient
    private Oferta ganador;
    @Transient
    private Usuario oferente;

    /**
     * Instanciar una subasta valida.
     */
    public static Subasta of(String producto, List<String> etiquetas, Optional<LocalDateTime> maybeInicio, LocalDateTime fin, Optional<Double> maybeMontoMinimo, Usuario oferente) {

        if (Strings.isNullOrEmpty(producto)) {
            throw new SubastaInvalida("tiene que ser un producto bien definido");
        }

        if (maybeInicio.isPresent()) {

            if (!maybeInicio.filter(i -> LocalDateTime.now().isBefore(i)).isPresent()) {
                throw new SubastaInvalida("tiene que ser una fecha inicio a futuro");
            }

            if (!maybeInicio.filter(i -> i.isBefore(fin)).isPresent()) {
                throw new SubastaInvalida("tiene que ser un rango de fechas cerrado");
            }

        } else if (LocalDateTime.now().isAfter(fin)) {
            throw new SubastaInvalida("tiene que ser una fecha de fin a futuro");
        }

        if (maybeMontoMinimo.isPresent() && !maybeMontoMinimo.filter(m -> 0D < m).isPresent()) {
            throw new SubastaInvalida("tiene que ser un monto minimo positivo");
        }

        final Subasta subasta = new Subasta(
                producto,
                etiquetas,
                maybeInicio.orElse(LocalDateTime.now()),
                fin,
                maybeInicio.map((i) -> EstadoSubasta.PENDIENTE).orElse(EstadoSubasta.INICIADA),
                Lists.newArrayList(),
                maybeMontoMinimo.orElse(0D),
                null,
                oferente
        );

        oferente.getSubastas().add(subasta);

        return subasta;
    }

    /**
     * Hacer una oferta para el producto que se subasta.
     *
     * @param monto     valor en pesos que ofrece por el producto
     * @param ofertante usuario que hace la oferta
     */
    public void ofertar(Double monto, Usuario ofertante) {

        final Oferta oferta = new Oferta(monto, ofertante);

        validarOferta(oferta);

        getOfertas().add(oferta);
    }

    /**
     * Dar por finalizada una subasta y calcula quien es el ganador entre los ofertantes
     */
    public void terminar() {
        validarTerminarSubasta();

        determinarGanador().ifPresent(this::setGanador);

        setEstado(EstadoSubasta.FINALIZADA);
    }

    /**
     * El ganador de la subasta es aquella oferta que se hizo con el mayor monto y que en caso de empate
     * se hizo antes que las otras.
     */
    private Optional<Oferta> determinarGanador() {

        return getOfertas().stream()
                .sorted(((o1, o2) -> {
                    final boolean assertion = o1.getMonto() > o2.getMonto() || (o1.getMonto().equals(o2.getMonto()) && o1.getFecha().isBefore(o2.getFecha()));
                    return assertion ? -1 : 1;
                }))
                .findFirst();
    }

    private void validarTerminarSubasta() {
        if (EstadoSubasta.PENDIENTE.equals(getEstado()) ||
                EstadoSubasta.CANCELADA.equals(getEstado()) ||
                EstadoSubasta.FINALIZADA.equals(getEstado())
        ) {
            throw new EstadoSubastaInvalidoException("no se puede finalizar una subasta " + getEstado());
        }
    }

    private void validarOferta(Oferta oferta) {
        if (!EstadoSubasta.INICIADA.equals(estado)) {
            throw new OfertaInvalidaException("la subasta no está activa");
        }

        if (oferta.getMonto() < montoMinimo) {
            throw new OfertaInvalidaException("monto de la oferta no supera minimo de la subasta");
        }

        if (getOferente().equals(oferta.getOfertante())) {
            throw new OfertaInvalidaException("el ofertante no puede ser el mismo oferente");
        }
    }
}
