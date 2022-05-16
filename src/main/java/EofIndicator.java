import java.io.Serializable;

/**
 *  Class to indicate when the end of the input stream is during deserialisation.
 *  Prevents false EoF errors.
 */
public class EofIndicator  implements Serializable {
}
