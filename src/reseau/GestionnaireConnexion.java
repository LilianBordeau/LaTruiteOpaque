package reseau;

import java.io.IOException;
import java.util.ArrayList;

public class GestionnaireConnexion
{
    private ArrayList<Connexion> connexions;
    
    public GestionnaireConnexion()
    {
        connexions = new ArrayList<>();
    }
    
    private synchronized void ajouterConnexion(Connexion connexion)
    {
        connexions.add(connexion);
    }
    
    public ConnexionClient creerConnexionClient()
    {
        ConnexionClient connexionClient = new ConnexionClient();
        ajouterConnexion(connexionClient);
        return connexionClient;
    }
    
    public ConnexionServeur creerConnexionServeur(int port) throws IOException
    {
        ConnexionServeur connexionServeur = new ConnexionServeur(port);
        ajouterConnexion(connexionServeur);
        return connexionServeur;
    }
    
    public void fermerToutesLesConnexions()
    {
        System.out.println("tentative de fermeture d'une connexion");
        for(Connexion connexion : connexions)
        {
            System.out.println("tentative de fermeture d'une connexion");
            connexion.close();
        }
        connexions.clear();
    }
}
