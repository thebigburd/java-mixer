import java.io.Serializable;
import java.util.Vector;

/**
 *  AudioSettings Serialisable Object
 *  Stores the settings the user has selected for Audio Format.
 *
 * @version 1.00
 * @author Richard Lam
 */
public class AudioSettings  implements Serializable {

    // Saved Settings
    Vector savedSettings = new Vector<String>(5);

    public void setSavedSettings(Vector savedSettings) {
        this.savedSettings = savedSettings;
    }

    public Vector getSavedSettings(){
        return savedSettings;
    }
}
