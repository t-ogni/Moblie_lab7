package yakovskij.lab7;

import androidx.annotation.NonNull;

public class Note {
    public String key;
    public String value;

    @NonNull
    public String toString(){
        return key + ": " + value;
    }
}
