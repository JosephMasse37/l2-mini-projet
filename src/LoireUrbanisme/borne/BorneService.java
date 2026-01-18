package LoireUrbanisme.borne;

import passerelle.ArretDAO;
import passerelle.BorneDAO;
import passerelle.Connexion;
import passerelle.DAOException;
import metiers.Arret;
import metiers.Borne;

import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BorneService {

    private final BorneDAO borneDAO;
    private final ArretDAO arretDAO;

    public BorneService() throws DAOException {
        Connection c = Connexion.getConnexion();
        if (c == null) {
            throw new DAOException("Connexion à la base impossible");
        }
        borneDAO = new BorneDAO(c);
        arretDAO = new ArretDAO(c);
    }

    public int nombreBornes() throws DAOException {
        return borneDAO.findAll().size();
    }

    public int totalVoyages() throws DAOException {
        return borneDAO.findAll()
                .stream()
                .mapToInt(Borne::getNbVoyageVendu)
                .sum();
    }

    public int totalTickets() throws DAOException {
        return borneDAO.findAll()
                .stream()
                .mapToInt(Borne::getNbVentesTickets)
                .sum();
    }

    /**
     * Retourne la liste des arrêts disposant d'au moins une borne
     */
    public List<Arret> arretsAvecBorne() throws DAOException {

        Set<Integer> idsArrets = borneDAO.findAll()
                .stream()
                .map(b -> b.getArret().getIdArret())
                .collect(Collectors.toSet());


        // Arrêts correspondants
        return arretDAO.findAll()
                .stream()
                .filter(a -> idsArrets.contains(a.getIdArret()))
                .toList();
    }
}
