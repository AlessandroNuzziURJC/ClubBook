package clubbook.backend.responses;

/**
 * Contains a collection of response messages used throughout the application.
 * These messages can be used to inform users about the status of various operations,
 * such as registration, login, event management, and more.
 */
public class ResponseMessages {

    public static final String ASSISTANCE_REGISTERED_CORRECT = "Asistencia registrada correctamente";
    public static final String SEASON_NOT_STARTED = "La temporada no ha comenzado";
    public static final String OK = "";
    public static final String CORRECT_REGISTER = "Se ha registrado correctamente";
    public static final String INCORRECT_REGISTER = "No se ha podido registrar el usuario";
    public static final String CORRECT_LOG_IN = "Inicio de sesión correcto";
    public static final String NEW_EVENT_REGISTERED_CORRECT = "Registro de evento correcto";
    public static final String EDIT_EVENT_REGISTERED_CORRECT = "Edición de evento correcto";
    public static final String NO_FUTURE_EVENTS = "No hay eventos previstos";
    public static final String EVENT_DELETED_SUCCESS = "Evento eliminado correctamente";
    public static final String ATTENDANCE_UPDATED = "Asistencia actualizada";
    public static final String UNABLE_TO_DELETE = "El usuario está enlazado a alguna clase como <rol>, " +
            "impidiendo su eliminación. Retírelo de la clase para proceder a su eliminación.";
}
