package co.edu.usco.psp.exception;

public class ArchivoNoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = -7682730939304423329L;

	public ArchivoNoEncontradoException(String mensaje) {
		super(mensaje);
	}
}
