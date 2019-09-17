package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.exception.EstadoSubastaInvalidoException;
import ar.edu.utn.frba.dds.domain.exception.OfertaInvalidaException;
import ar.edu.utn.frba.dds.domain.exception.SubastaInvalida;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SubastaTest {

    // --------------- Crear ---------------

    @Test
    void ofSubastaOkParaAhora() {

        // pre-condiciones
        final Usuario user1 = new Usuario("user1");

        // operacion
        final Subasta subasta = Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.empty(),
                LocalDateTime.now().plusDays(5),
                Optional.empty(),
                user1);

        // post-condiciones
        assertTrue(subasta.getOfertas().isEmpty());
        assertEquals(subasta.getEstado(), EstadoSubasta.INICIADA);
        assertTrue(Optional.ofNullable(subasta.getInicio()).isPresent());
        assertEquals(subasta.getMontoMinimo(), 0D);
        assertEquals(user1.getSubastas().size(), 1);
    }

    @Test
    void ofSubastaOkParaDespues() {

        // pre-condiciones
        final Usuario user1 = new Usuario("user1");

        // operacion
        final Subasta subasta = Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.of(LocalDateTime.now().plusDays(1)),
                LocalDateTime.now().plusDays(5),
                Optional.empty(),
                user1);

        // post-condiciones
        assertTrue(subasta.getOfertas().isEmpty());
        assertEquals(subasta.getEstado(), EstadoSubasta.PENDIENTE);
        assertEquals(subasta.getMontoMinimo(), 0D);
        assertEquals(user1.getSubastas().size(), 1);
    }

    @Test
    void ofSubastaInvalidaPorqueMontoNegativo() {

        // pre-condiciones
        final Usuario user1 = new Usuario("user1");

        // operacion
        final Executable executable = () -> Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.empty(),
                LocalDateTime.now().plusDays(5),
                Optional.of(-1D),
                user1);

        // post-condiciones
        final Throwable throwable = assertThrows(SubastaInvalida.class, executable);
        assertEquals(throwable.getMessage(), "Subasta invalida porque tiene que ser un monto minimo positivo.");
        assertEquals(user1.getSubastas().size(), 0);
    }

    @Test
    void ofSubastaInvalidaPorqueFechaFinPasado() {

        // pre-condiciones
        final Usuario user1 = new Usuario("user1");

        // operacion
        final Executable executable = () -> Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.empty(),
                LocalDateTime.now().minusDays(5),
                Optional.of(-1D),
                user1);

        // post-condiciones
        final Throwable throwable = assertThrows(SubastaInvalida.class, executable);
        assertEquals(throwable.getMessage(), "Subasta invalida porque tiene que ser una fecha de fin a futuro.");
        assertEquals(user1.getSubastas().size(), 0);
    }

    @Test
    void ofSubastaInvalidaPorqueRangoFechaIncorrecto() {

        // pre-condiciones
        final Usuario user1 = new Usuario("user1");

        // operacion
        final Executable executable = () -> Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.of(LocalDateTime.now().plusDays(5)),
                LocalDateTime.now().minusDays(5),
                Optional.of(-1D),
                user1);

        // post-condiciones
        final Throwable throwable = assertThrows(SubastaInvalida.class, executable);
        assertEquals(throwable.getMessage(), "Subasta invalida porque tiene que ser un rango de fechas cerrado.");
        assertEquals(user1.getSubastas().size(), 0);
    }


    // --------------- Ofertar ---------------

    @Test
    void ofertarConOfertaValida() {

        // pre-condiciones
        final Subasta subasta = Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.empty(),
                LocalDateTime.now().plusDays(5),
                Optional.empty(),
                new Usuario("user1"));

        final Usuario ofertante = new Usuario("user2");


        // operacion
        subasta.ofertar(15D, ofertante);


        // post-condiciones
        assertEquals(subasta.getOfertas().size(), 1);
    }

    @Test
    void ofertarConOfertaInvalidaPorqueMontoMenorAMinimo() {

        // pre-condiciones
        final Subasta subasta = Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.empty(),
                LocalDateTime.now().plusDays(5),
                Optional.of(10D),
                new Usuario("user1"));

        final Usuario ofertante = new Usuario("user2");


        // operacion
        final Executable executable = () -> subasta.ofertar(9D, ofertante);


        // post-condicion
        final OfertaInvalidaException exception = assertThrows(OfertaInvalidaException.class, executable);
        assertEquals(exception.getMessage(), "Oferta invalida porque monto de la oferta no supera minimo de la subasta.");
    }

    @Test
    void ofertarConOfertaInvalidaPorqueSubastaNoActiva() {

        // pre-condiciones
        final Subasta subasta = Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.of(LocalDateTime.now().plusDays(10)),
                LocalDateTime.now().plusDays(15),
                Optional.empty(),
                new Usuario("user1"));

        final Usuario oferentante = new Usuario("user2");


        // operacion
        final Executable executable = () -> subasta.ofertar(15D, oferentante);


        // post-condicion
        final OfertaInvalidaException exception = assertThrows(OfertaInvalidaException.class, executable);
        assertEquals(exception.getMessage(), "Oferta invalida porque la subasta no está activa.");
    }
    
    @Test
    void ofertarConOfertaInvalidaPorqueUsuarioOfertanteIgualOferente() {

        // pre-condiciones
        final Usuario user1 = new Usuario("user1");

        final Subasta subasta = Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.empty(),
                LocalDateTime.now().plusDays(15),
                Optional.of(10D),
                user1);


        // operacion
        final Executable executable = () -> subasta.ofertar(15D, user1);


        // post-condicion
        final OfertaInvalidaException exception = assertThrows(OfertaInvalidaException.class, executable);
        assertEquals(exception.getMessage(), "Oferta invalida porque el ofertante no puede ser el mismo oferente.");
    }


    // --------------- Terminar ---------------

    @Test
    void terminarSubastaOkYConGanadorPorMayorMonto() {

        // pre-condiciones
        Oferta oferta1 = mock(Oferta.class);
        when(oferta1.getMonto()).thenReturn(500D);

        Oferta oferta2 = mock(Oferta.class);
        when(oferta2.getMonto()).thenReturn(1500D);

        Oferta oferta3 = mock(Oferta.class);
        when(oferta3.getMonto()).thenReturn(300D);

        final Subasta subasta = spy(Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.empty(),
                LocalDateTime.now().plusDays(15),
                Optional.of(10D),
                new Usuario("user1")));

        when(subasta.getOfertas()).thenReturn(Lists.newArrayList(oferta1,oferta2,oferta3));

        // operacion
        subasta.terminar();

        // post-condicion
        assertNotNull(subasta.getGanador());
        assertEquals(subasta.getGanador(), oferta2);
    }

    @Test
    void terminarSubastaOkYConGanadorPorIgualMontoYFechaAnterior() {

        // pre-condiciones
        final LocalDateTime now = LocalDateTime.now();

        Oferta oferta1 = mock(Oferta.class);
        when(oferta1.getMonto()).thenReturn(500D);
        when(oferta1.getFecha()).thenReturn(now.minusDays(1));

        Oferta oferta2 = mock(Oferta.class);
        when(oferta2.getMonto()).thenReturn(500D);
        when(oferta2.getFecha()).thenReturn(now.minusDays(1));

        Oferta oferta3 = mock(Oferta.class);
        when(oferta3.getMonto()).thenReturn(500D);
        when(oferta3.getFecha()).thenReturn(now.minusDays(2));

        final Subasta subasta = spy(Subasta.of(
                "Aire Acondicionado",
                Lists.newArrayList("Electrodomestico"),
                Optional.empty(),
                now.plusDays(15),
                Optional.of(10D),
                new Usuario("user1")));

        when(subasta.getOfertas()).thenReturn(Lists.newArrayList(oferta1,oferta2,oferta3));

        // operacion
        subasta.terminar();

        // post-condicion
        assertNotNull(subasta.getGanador());
        assertEquals(subasta.getGanador(), oferta3);
    }

    @Test
    void terminarSubastaErrorPorqueSubastaFinalizada() {

        // pre-condiciones
        final Subasta subasta = mock(Subasta.class);
        when(subasta.getEstado()).thenReturn(EstadoSubasta.FINALIZADA);
        doCallRealMethod().when(subasta).terminar();

        // operacion
        final Executable executable = subasta::terminar;

        // post-condicion
        final EstadoSubastaInvalidoException exception = assertThrows(EstadoSubastaInvalidoException.class, executable);
        assertEquals(exception.getMessage(), "Estado subasta invalido porque no se puede finalizar una subasta FINALIZADA.");
    }

    @Test
    void terminarSubastaErrorPorqueSubastaPendiente() {

        // pre-condiciones
        final Subasta subasta = mock(Subasta.class);
        when(subasta.getEstado()).thenReturn(EstadoSubasta.PENDIENTE);
        doCallRealMethod().when(subasta).terminar();

        // operacion
        final Executable executable = subasta::terminar;

        // post-condicion
        final EstadoSubastaInvalidoException exception = assertThrows(EstadoSubastaInvalidoException.class, executable);
        assertEquals(exception.getMessage(), "Estado subasta invalido porque no se puede finalizar una subasta PENDIENTE.");
    }

    @Test
    void terminarSubastaErrorPorqueSubastaCancelada() {

        // pre-condiciones
        final Subasta subasta = mock(Subasta.class);
        when(subasta.getEstado()).thenReturn(EstadoSubasta.CANCELADA);
        doCallRealMethod().when(subasta).terminar();

        // operacion
        final Executable executable = subasta::terminar;

        // post-condicion
        final EstadoSubastaInvalidoException exception = assertThrows(EstadoSubastaInvalidoException.class, executable);
        assertEquals(exception.getMessage(), "Estado subasta invalido porque no se puede finalizar una subasta CANCELADA.");
    }
}