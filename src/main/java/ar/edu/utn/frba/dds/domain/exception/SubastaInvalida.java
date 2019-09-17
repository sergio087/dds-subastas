package ar.edu.utn.frba.dds.domain.exception;

public class SubastaInvalida extends RuntimeException {
    public SubastaInvalida(String motivo) {
        super(String.format("Subasta invalida porque %s.", motivo));
    }
}
