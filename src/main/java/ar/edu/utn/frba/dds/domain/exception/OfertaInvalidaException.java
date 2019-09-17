package ar.edu.utn.frba.dds.domain.exception;

public class OfertaInvalidaException extends RuntimeException {

    public OfertaInvalidaException(String motivo) {
        super(String.format("Oferta invalida porque %s.", motivo));
    }
}
