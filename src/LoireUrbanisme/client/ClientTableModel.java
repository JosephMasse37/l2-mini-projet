package LoireUrbanisme.client;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.*;

import metiers.Client;

public class ClientTableModel extends AbstractTableModel {
    private List<Client> clients;
    private final String[] columnNames = {"ID", "Nom", "Prénom", "Âge", "Abonnement"};
    
    public ClientTableModel(List<Client> clients) {
        this.clients = new ArrayList<>(clients);
    }

    @Override
    public int getRowCount() {
        return clients.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //Get the client for the row
        Client client = clients.get(rowIndex);

        //Return the value for the column
        switch (columnIndex) {
            case 0:
                return client.getIdClient();
            case 1:
                return client.getNom();
            case 2:
                return client.getPrenom();
            case 3:
                return client.getAge();
            case 4:
                if(client.getUnAbonnement() != null) {
                    return client.getUnAbonnement().getFormule();
                } else {
                    return "Aucun";
                }
            default:
                return null;
        }
    }

    public Client getClientAt(int rowIndex) {
        return clients.get(rowIndex);
    }
    
}
