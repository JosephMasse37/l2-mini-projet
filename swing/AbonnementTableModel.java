package swing;

import javax.swing.table.*;
import java.util.ArrayList;
import java.util.List;

import metiers.Abonnement;

public class AbonnementTableModel extends AbstractTableModel {
    private List<Abonnement> abonnements;
    private final String[] columnNames = {"Formule", "Prix", "Durée", "Nombre de Clients"};

    public AbonnementTableModel(List<Abonnement> abonnements) {
        this.abonnements = new ArrayList<>(abonnements);
    }

    @Override
    public int getRowCount() {
        return abonnements.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //Get the Abonnement for the row
        Abonnement abonnement = abonnements.get(rowIndex);

        //Return the value for the column
        switch (columnIndex) {
            case 0 : 
                return abonnement.getFormule();
            case 1 :
                return abonnement.getPrix();
            case 2 :
                return abonnement.getDuree();
            case 3 :
                return abonnement.getListeClients().size();
            default :
                return null;
        }
    }
    
}
