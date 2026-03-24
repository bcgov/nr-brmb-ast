package ca.bc.gov.farms.common.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * Common controller class. Implement any common methods here
 * By default we define basic response entity calls as a utility
 * for creating them manually
 *
 * This is an ideal place to enforce eTag and other header creation
 */

@Slf4j
public abstract class CommonController {
    protected CommonController(String name) {
        log.debug("{} Controller Initialized", name);
    }

    /** RESPONSE UTILITIES **/

    /**
     * 200 OK
     *
     * @param <T>
     * @param entities
     * @return
     */
    protected final <T> ResponseEntity<T> ok(T entities) {
        return ResponseEntity.ok().body(entities);
    }

    /**
     * 201 Created (please use this on POST/Creates, not 200 OK)
     *
     * @param <T>
     * @param entity
     * @return
     */
    @SuppressWarnings("null")
    protected final <T> ResponseEntity<T> created(T entity) {
        return ResponseEntity.created(null).body(entity);
    }

    /**
     * 404 Not Found
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> notFound() {
        return ResponseEntity.notFound().build();
    }

    /**
     * 202 Accepted
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> accepted() {
        return ResponseEntity.accepted().build();
    }

    /**
     * 400 Bad Request
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> badRequest() {
        return ResponseEntity.badRequest().build();
    }

    /**
     * 500 Internal Server Error
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> internalServerError() {
        return ResponseEntity.internalServerError().build();
    }

    /**
     * 403 Forbidden
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> forbidden() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     * 401 Unauthorized
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> unauthorized() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * 409 Conflict
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> conflict() {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * 412 Precondition failed
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> preconditionFailed() {
        return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * 428 Precondition Required
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> preconditionRequired() {
        return new ResponseEntity<>(HttpStatus.PRECONDITION_REQUIRED);
    }

    /**
     * 308 Permanent Redirect
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> redirect() {
        return new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT);
    }

    /**
     * 301 Moved Permanently
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> moved() {
        return new ResponseEntity<>(HttpStatus.MOVED_PERMANENTLY);
    }

    /**
     * 418 I'm a teapot
     *
     * @param <T>
     * @return
     */
    protected final <T> ResponseEntity<T> teapot() {
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
}
