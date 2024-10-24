package fr.apside.formation.web.handler;

import fr.apside.formation.exception.UserGlobalException;
import fr.apside.formation.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.MediaType.TEXT_PLAIN;

@ControllerAdvice
public class UserExceptionWrapper extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserExceptionWrapper.class);

    @ExceptionHandler(UserGlobalException.class)
    public ResponseEntity<String> UserGlobalExceptionWrapper(UserGlobalException e) {
        LOGGER.error("Erreur global utilisateur : {}", e.getMessage());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Exception: ", e);
        }
        LOGGER.info("Réponse, envoi message 404");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(TEXT_PLAIN);
        return new ResponseEntity<>(e.getMessage(), httpHeaders, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> UserNotFoundExceptionWrapper(UserNotFoundException e) {
        LOGGER.error("Traitement de l'erreur, utilisateur non trouvé : {}", e.getMessage());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Exception: ", e);
        }
        LOGGER.info("Utilisateur non trouvé, envoi message 404");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(TEXT_PLAIN);
        return new ResponseEntity<>(e.getMessage(), httpHeaders, HttpStatus.NOT_FOUND);
    }

}
