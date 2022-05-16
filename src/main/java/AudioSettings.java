import java.io.Serializable;
import java.util.Vector;

/**
 *
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
