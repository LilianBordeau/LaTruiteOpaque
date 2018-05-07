package vue;

import javafx.beans.NamedArg;
import javafx.scene.image.ImageView;

/**
 * ImageView pouvant etre initialisee en une ligne en FXML : <!-- <ImageView url="fichierImage" /> -->
 */
public class SimpleImageView extends ImageView
{
    public SimpleImageView(@NamedArg("url") String url)
    {
        super(url);
    }
}
