package codeset.hazelcast.codegen;

/**
 * Indicate if something went wrong during codegen.
 * 
 * @author ingemar.svensson
 *
 */
@SuppressWarnings("serial")
public class CodegenException extends RuntimeException {

    public CodegenException() {
        super();
    }

    public CodegenException(String message) {
        super(message);
    }

    public CodegenException(Throwable throwable) {
        super(throwable);
    }

    public CodegenException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
