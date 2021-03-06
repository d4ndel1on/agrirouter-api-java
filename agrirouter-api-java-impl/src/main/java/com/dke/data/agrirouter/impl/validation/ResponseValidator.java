package com.dke.data.agrirouter.impl.validation;

import com.dke.data.agrirouter.api.exception.ForbiddenRequestException;
import com.dke.data.agrirouter.api.exception.InvalidUrlForRequestException;
import com.dke.data.agrirouter.api.exception.UnauthorizedRequestException;
import com.dke.data.agrirouter.api.exception.UnexpectedHttpStatusException;
import com.gargoylesoftware.htmlunit.WebResponse;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ObjectArrayMessage;

/** Validation of the response, encapsulated using an interface. */
public interface ResponseValidator {

  Logger LOGGER = LogManager.getLogger();

  /**
   * Will assert, that the response status is valid. If there will be an 404 or 401 a business
   * exception will rise.
   *
   * @param response The current response.
   * @param exceptedHttpStatus The expected HTTP status.
   */
  default void assertResponseStatusIsValid(Response response, int exceptedHttpStatus) {
    LOGGER.debug("Validating response.");
    LOGGER.trace(new ObjectArrayMessage(response, exceptedHttpStatus));
    if (response.getStatus() == HttpStatus.SC_NOT_FOUND) {
      throw new InvalidUrlForRequestException();
    }
    if (response.getStatus() == HttpStatus.SC_UNAUTHORIZED) {
      throw new UnauthorizedRequestException();
    }
    if (response.getStatus() == HttpStatus.SC_FORBIDDEN) {
      throw new ForbiddenRequestException();
    }
    if (response.getStatus() != exceptedHttpStatus) {
      throw new UnexpectedHttpStatusException(response.getStatus(), exceptedHttpStatus);
    }
  }

  /**
   * Will assert, that the response status is valid. If there will be an 404 or 401 a business
   * exception will rise.
   *
   * @param response The current response.
   * @param exceptedHttpStatus The expected HTTP status.
   */
  default void assertResponseStatusIsValid(WebResponse response, int exceptedHttpStatus) {
    LOGGER.debug("Validating response.");
    LOGGER.trace(new ObjectArrayMessage(response, exceptedHttpStatus));
    if (response.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
      throw new InvalidUrlForRequestException();
    }
    if (response.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
      throw new UnauthorizedRequestException();
    }
    if (response.getStatusCode() == HttpStatus.SC_FORBIDDEN) {
      throw new ForbiddenRequestException();
    }
    if (response.getStatusCode() != exceptedHttpStatus) {
      throw new UnexpectedHttpStatusException(response.getStatusCode(), exceptedHttpStatus);
    }
  }
}
