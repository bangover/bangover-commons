package cloud.bangover.errors;

import lombok.NonNull;

/**
 * This class represents the unrecognized application exception.
 *
 * @author Dmitry Mikhaylenko
 *
 */
public final class UnexpectedErrorException extends ApplicationException {
  private static final long serialVersionUID = -263012476173962439L;

  /**
   * Create {@link UnexpectedErrorException} by a random throwable object.
   *
   * @param unexpectedError The happened error
   */
  public UnexpectedErrorException(@NonNull Throwable unexpectedError) {
    super(ErrorSeverity.INCIDENT, ErrorCode.UNRECOGNIZED_ERROR_CODE);
    initCause(unexpectedError);
  }

}
