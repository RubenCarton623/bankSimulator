package com.rvera.sofka.banksimulator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones
 * Convierte errores técnicos en mensajes claros y amigables
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de DTOs
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        Map<String, String> fieldErrors = new HashMap<>();
        StringBuilder mensajeGeneral = new StringBuilder("Ha ocurrido un error en la validación de datos");
        
        // Extraer información del request
        String path = request.getDescription(false).replace("uri=", "");
        String proceso = determinarProceso(path);
        
        // Procesar cada error de campo
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String campo = error.getField();
            String valorRechazado = error.getRejectedValue() != null ? 
                error.getRejectedValue().toString() : "null";
            String mensajeError = error.getDefaultMessage();
            
            // Generar mensaje amigable según el campo y tipo de error
            String mensajeAmigable = generarMensajeAmigable(campo, valorRechazado, mensajeError, proceso);
            fieldErrors.put(campo, mensajeAmigable);
        }
        
        // Si solo hay un error, usarlo como mensaje principal
        if (fieldErrors.size() == 1) {
            mensajeGeneral = new StringBuilder(fieldErrors.values().iterator().next());
        } else {
            mensajeGeneral.append(" debido a ").append(fieldErrors.size()).append(" campos inválidos. ")
                .append("Por favor revisa los detalles y corrige los valores.");
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Error de Validación")
            .message(mensajeGeneral.toString())
            .path(path)
            .details(fieldErrors)
            .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Maneja IllegalArgumentException (errores de negocio)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {
        
        String path = request.getDescription(false).replace("uri=", "");
        String proceso = determinarProceso(path);
        
        String mensaje = "Ha ocurrido un error en " + proceso + " debido a: " + ex.getMessage() + 
            ". Por favor verifica los datos e intenta de nuevo.";
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Error de Validación de Negocio")
            .message(mensaje)
            .path(path)
            .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Maneja excepciones generales no capturadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {
        
        String path = request.getDescription(false).replace("uri=", "");
        String proceso = determinarProceso(path);
        
        String mensaje = "Ha ocurrido un error inesperado en " + proceso + 
            ". Por favor contacta al administrador del sistema si el problema persiste.";
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Error Interno del Servidor")
            .message(mensaje)
            .path(path)
            .technicalDetails(ex.getMessage())
            .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    /**
     * Genera mensaje amigable según el campo y error
     */
    private String generarMensajeAmigable(String campo, String valorRechazado, 
                                          String mensajeError, String proceso) {
        
        // Casos específicos por campo
        switch (campo) {
            case "nombre":
                if (mensajeError.contains("tamaño") || mensajeError.contains("size")) {
                    return String.format("Ha ocurrido un error en %s debido a que el nombre '%s' no cumple con la longitud requerida (3-255 caracteres). Por favor ingresa un nombre válido e intenta de nuevo.", 
                        proceso, valorRechazado);
                }
                return String.format("Ha ocurrido un error en %s debido a que el nombre no puede estar vacío. Por favor ingresa un nombre válido e intenta de nuevo.", proceso);
            
            case "genero":
                return String.format("Ha ocurrido un error en %s debido a que el género '%s' no es válido. El género debe ser: 'M', 'F', 'Masculino', 'Femenino' u 'Otro'. Por favor ingresa correctamente el género e intenta de nuevo.", 
                    proceso, valorRechazado);
            
            case "edad":
                return String.format("Ha ocurrido un error en %s debido a que la edad '%s' no es válida. La edad debe ser un número positivo entre 0 y 150. Por favor ingresa una edad correcta e intenta de nuevo.", 
                    proceso, valorRechazado);
            
            case "identificacion":
                if (mensajeError.contains("Pattern") || mensajeError.contains("patrón")) {
                    return String.format("Ha ocurrido un error en %s debido a que la identificación '%s' no cumple con el formato requerido (debe contener entre 6 y 20 dígitos). Por favor ingresa una identificación válida e intenta de nuevo.", 
                        proceso, valorRechazado);
                }
                return String.format("Ha ocurrido un error en %s debido a que la identificación no puede estar vacía. Por favor ingresa una identificación válida e intenta de nuevo.", proceso);
            
            case "direccion":
                return String.format("Ha ocurrido un error en %s debido a que la dirección no puede estar vacía. Por favor ingresa una dirección válida e intenta de nuevo.", proceso);
            
            case "telefono":
                return String.format("Ha ocurrido un error en %s debido a que el teléfono '%s' no cumple con el formato requerido. Por favor ingresa un número de teléfono válido e intenta de nuevo.", 
                    proceso, valorRechazado);
            
            case "contrasena":
                return String.format("Ha ocurrido un error en %s debido a que la contraseña no cumple con los requisitos de seguridad (mínimo 6 caracteres). Por favor ingresa una contraseña válida e intenta de nuevo.", proceso);
            
            case "numeroCuenta":
                return String.format("Ha ocurrido un error en %s debido a que el número de cuenta no puede estar vacío. Por favor ingresa un número de cuenta válido e intenta de nuevo.", proceso);
            
            case "tipoCuenta":
                return String.format("Ha ocurrido un error en %s debido a que el tipo de cuenta '%s' no es válido. El tipo de cuenta debe ser: 'Ahorros' o 'Corriente'. Por favor ingresa correctamente el tipo de cuenta e intenta de nuevo.", 
                    proceso, valorRechazado);
            
            case "saldoInicial":
                return String.format("Ha ocurrido un error en %s debido a que el saldo inicial '%s' no es válido. El saldo debe ser un número positivo. Por favor ingresa un saldo válido e intenta de nuevo.", 
                    proceso, valorRechazado);
            
            case "tipoMovimiento":
                return String.format("Ha ocurrido un error en %s debido a que el tipo de movimiento '%s' no es válido. El tipo debe ser: 'Deposito' o 'Retiro'. Por favor ingresa correctamente el tipo de movimiento e intenta de nuevo.", 
                    proceso, valorRechazado);
            
            case "valor":
                return String.format("Ha ocurrido un error en %s debido a que el valor '%s' no es válido. El valor debe ser un número positivo. Por favor ingresa un valor válido e intenta de nuevo.", 
                    proceso, valorRechazado);
            
            case "fecha":
                return String.format("Ha ocurrido un error en %s debido a que la fecha no puede estar vacía o tiene un formato incorrecto. Por favor ingresa una fecha válida (formato: YYYY-MM-DD) e intenta de nuevo.", proceso);
            
            default:
                return String.format("Ha ocurrido un error en %s debido a que el campo '%s' con valor '%s' no es válido: %s. Por favor corrige este valor e intenta de nuevo.", 
                    proceso, campo, valorRechazado, mensajeError);
        }
    }
    
    /**
     * Determina el proceso según la ruta
     */
    private String determinarProceso(String path) {
        if (path.contains("/personas")) {
            if (path.matches(".*/personas/\\d+")) {
                return "la actualización de persona";
            }
            return "la creación de persona";
        } else if (path.contains("/clientes")) {
            if (path.matches(".*/clientes/\\d+")) {
                return "la actualización de cliente";
            }
            return "la creación de cliente";
        } else if (path.contains("/cuentas")) {
            if (path.matches(".*/cuentas/\\d+")) {
                return "la actualización de cuenta";
            }
            return "la creación de cuenta";
        } else if (path.contains("/movimientos")) {
            if (path.matches(".*/movimientos/\\d+")) {
                return "la actualización de movimiento";
            }
            return "la creación de movimiento";
        }
        return "el proceso";
    }
}
