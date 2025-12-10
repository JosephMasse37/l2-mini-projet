package tests;

import metiers.ConduitSur;
import passerelle.ConduitSurDAO;

import java.util.List;

public class TestConduitSurDAO {
    public static void main(String[] args) {
        List<ConduitSur> lesConduites = ConduitSurDAO.getConduiteUnVehicule(53);
        for(int i=0;i< lesConduites.size();i++) {
            System.out.println(lesConduites.get(i).getDateHeureConduite());
        }
    }
}
