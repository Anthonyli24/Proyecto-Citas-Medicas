package com.example.proyecto_sistema_citas.logic;

import com.example.proyecto_sistema_citas.data.HorarioRepository;
import com.example.proyecto_sistema_citas.data.MedicoRepository;
import com.example.proyecto_sistema_citas.data.RolRepository;
import com.example.proyecto_sistema_citas.data.UsuarioRepository;
import com.example.proyecto_sistema_citas.data.CitaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@org.springframework.stereotype.Service("service")
public class Service {
   @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private CitaRepository citaRepository;

    public Iterable<Usuario> usuarioFindAll(){
       return usuarioRepository.findAll();
   }

   @Transactional
    public void registrarMedico(Medico medico){
       medicoRepository.save(medico);
   }

    public Iterable<Medico> medicoFindAll(){
         return medicoRepository.findAll();
    }

    @Transactional
    public void RegistrarUsuario(Usuario usuario){
        usuarioRepository.save(usuario);
    }

    public Iterable<Rol> rolFindAll(){
        return rolRepository.findAll();
    }

    public Usuario findUsuarioById(String id){
        return usuarioRepository.findById(id).get() ;
    }

    public boolean existeUsuarioPorId(String id){
        return usuarioRepository.existsById(id);
    }

    public Medico obtenerMedicoPorId(String id) {
        return medicoRepository.findById(id).orElse(null);
    }

    public List<Medico> FiltradoMedicos(String especialidad, String localidad) {
        if ((especialidad == null || especialidad.isEmpty()) && (localidad == null || localidad.isEmpty())) {
            return (List<Medico>) medicoRepository.findAll();
        }

        if (especialidad == null) especialidad = "";
        if (localidad == null) localidad = "";

        return medicoRepository.findByEspecialidadContainingIgnoreCaseAndLocalidadContainingIgnoreCase(especialidad, localidad);
    }

    public List<Medico> FiltradoMedicosPorStatus(String status) {
        if (status == null || status.isEmpty()) {
            return (List<Medico>) medicoRepository.findAll();
        }
        return medicoRepository.findByStatusContainingIgnoreCase(status);
    }


    public void actualizarMedico(Medico medico) {
        Medico medicoExistente = medicoRepository.findById(medico.getId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + medico.getId()));

        double costoDouble = medico.getCosto().doubleValue();
        medicoExistente.setCosto(costoDouble);
        medicoExistente.setLocalidad(medico.getLocalidad());

        medicoRepository.save(medicoExistente);
    }

    @Transactional
    public void aceptarMedico(String id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + id));

        medico.setStatus("Aprobado");
        medicoRepository.save(medico);
    }

    public List<Horario> obtenerHorariosPorMedico(String medicoId) {
        return horarioRepository.findByMedicoId(medicoId);
    }

    public void agregarHorario(String medicoId, String dia, LocalTime horaInicio, LocalTime horaFin) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
            throw new IllegalArgumentException("La hora de finalización debe ser mayor a la de inicio.");
        }

        List<Horario> horariosExistentes = horarioRepository.findByMedicoIdAndDia(medicoId, dia);
        if (!horariosExistentes.isEmpty()) {
            throw new IllegalArgumentException("El médico ya tiene un horario asignado para el día " + dia);
        }

        Horario nuevoHorario = new Horario();
        nuevoHorario.setMedico(medico);
        nuevoHorario.setDia(dia);
        nuevoHorario.setHoraInicio(horaInicio);
        nuevoHorario.setHoraFin(horaFin);

        horarioRepository.save(nuevoHorario);
    }

    public void eliminarHorario(String id, String dia) {
        Optional<Horario> horario = horarioRepository.findById(id);
        if (horario.isPresent() && horario.get().getDia().equalsIgnoreCase(dia)) {
            horarioRepository.delete(horario.get());
        }
    }

    public void agendarCita(Cita cita){
        citaRepository.save(cita);
    }

    public List<Cita> obtenerCitasPorUsuario(String usuarioId) {
        return citaRepository.findByUsuarioId(usuarioId);
    }


    public Cita obtenerCitaPorId(String id){
        return citaRepository.findById(id).get();
    }

    public void actualizarCita(Cita cita) {
        Cita citaExistente = citaRepository.findById(String.valueOf(cita.getId()))
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + cita.getId()));

        citaExistente.setStatus(cita.getStatus());
        citaExistente.setMedico(cita.getMedico());
        citaExistente.setUsuario(cita.getUsuario());
        citaExistente.setFecha(cita.getFecha());
        citaExistente.setHora(cita.getHora());

        citaRepository.save(citaExistente);
    }


    public Iterable<Cita> obtenerCitasPorMedico(String id) {
        return citaRepository.findByMedicoId(id);
    }

    public Iterable<Cita> FiltradoCitas(String status, String doctor, String id) {
        // Si ni status ni doctor se proporcionan, solo filtra por la ID del médico.
        if ((status == null || status.isEmpty()) && (doctor == null || doctor.isEmpty())) {
            return citaRepository.findByMedicoId(id); // Filtra solo por la ID del médico.
        }

        // Si el estado no está vacío, filtra por estado y la ID del médico.
        if (status != null && !status.isEmpty()) {
            return citaRepository.findByStatusAndMedicoId(status, id); // Filtra por estado y ID del médico.
        }

        // Si el nombre del doctor no está vacío, intenta filtrar por el nombre del doctor y la ID del médico.
        if (doctor != null && !doctor.isEmpty()) {
            // Verificar si el doctor existe en la base de datos
            if (doctorExisteEnBaseDeDatos(doctor)) {
                return citaRepository.findByMedicoUsuarioNombreContainingIgnoreCaseAndMedicoId(doctor, id); // Filtra por nombre del doctor y ID.
            } else {
                return new ArrayList<>(); // Si el doctor no existe, devuelve una lista vacía.
            }
        }

        // Si solo la ID del médico está presente, filtra solo por la ID del médico.
        return citaRepository.findByMedicoId(id);
    }



    private boolean pacienteExisteEnBaseDeDatos(String paciente) {
        Usuario pacienteUsuario = usuarioRepository.findByNombreContainingIgnoreCase(paciente);
        return pacienteUsuario != null;
    }


    private boolean doctorExisteEnBaseDeDatos(String doctor) {
        Medico medico = medicoRepository.findByUsuarioNombreContainingIgnoreCase(doctor);
        return medico != null;
    }


    public Iterable<Cita> FiltradoCitasPaciente(String status, String paciente, String id) {
        // Si no se proporciona ni estado ni paciente, filtra solo por la ID del usuario.
        if ((status == null || status.isEmpty()) && (paciente == null || paciente.isEmpty())) {
            return citaRepository.findByUsuarioId(id); // Filtra solo por la ID del usuario (en lugar de paciente).
        }

        // Si se proporciona estado, filtra por estado y ID del usuario.
        if (status != null && !status.isEmpty()) {
            return citaRepository.findByStatusAndUsuarioId(status, id); // Filtra por estado y ID del usuario.
        }

        // Si se proporciona nombre de paciente, verifica si existe y filtra por nombre del usuario y ID del usuario.
        if (paciente != null && !paciente.isEmpty()) {
            if (pacienteExisteEnBaseDeDatos(paciente)) {
                return citaRepository.findByUsuarioNombreContainingIgnoreCaseAndUsuarioId(paciente, id); // Filtra por nombre del paciente (usuario) y ID.
            } else {
                return new ArrayList<>(); // Si el paciente no existe, devuelve una lista vacía.
            }
        }

        // Si solo la ID del usuario está presente, filtra solo por la ID del usuario.
        return citaRepository.findByUsuarioId(id);
    }

}
