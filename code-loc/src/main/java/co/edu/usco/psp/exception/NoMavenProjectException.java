package co.edu.usco.psp.exception;

public class NoMavenProjectException extends RuntimeException {

	private static final long serialVersionUID = 1135144809441816032L;

	public NoMavenProjectException(String mensaje) {
		super(mensaje);
	}
}
