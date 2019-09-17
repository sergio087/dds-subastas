package ar.edu.utn.frba.dds.domain.exception;

public class EstadoSubastaInvalidoException extends RuntimeException {
    public EstadoSubastaInvalidoException(String motivo) {
        super(String.format("Estado subasta invalido porque %s.", motivo));
    }
}
