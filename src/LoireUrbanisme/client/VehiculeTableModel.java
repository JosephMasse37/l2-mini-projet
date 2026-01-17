package LoireUrbanisme.client;

import javax.swing.table.*;
import java.util.ArrayList;
import java.util.List;

import metiers.Vehicule;

public class VehiculeTableModel extends AbstractTableModel {
    private List<Vehicule> vehicules;
    private final String[] columnNames = {"Marque", "Modèle", "Année", "Date de mise en service", "Date de dernière révision", "Type de véhicule"};

    public VehiculeTableModel(List<Vehicule> vehicules) {
        this.vehicules = new ArrayList<>(vehicules);
    }

    @Override
    public int getRowCount() {
        return vehicules.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //Get the Vehicule for the row
        Vehicule vehicule = vehicules.get(rowIndex);

        //Return the value for the column
        switch (columnIndex) {
            case 0 :
                return vehicule.getMarque();
            case 1 :
                return vehicule.getModele();
            case 2 :
                return vehicule.getDateFabrication();
            case 3 :
                return vehicule.getDateMiseEnService();
            case 4 :
                return vehicule.getDateHeureDerniereMaintenance();
            case 5 :
                if(vehicule.getTypevehicule() != null) {
                    return vehicule.getTypevehicule().getLibelle();
                } else {
                    return "Inconnu";
                }
            default :
                return null;
        }
    }
    
}
