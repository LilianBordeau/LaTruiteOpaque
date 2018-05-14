package modele;

import java.io.Serializable;
import reseau.ConnexionClient;

public class JoueurClient extends JoueurReseau implements Serializable
{
    public JoueurClient(ConnexionClient connexion)
    {
        this.connexion = connexion;
    }
}
