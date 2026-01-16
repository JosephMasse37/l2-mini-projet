package LoireUrbanisme.map;

import metiers.Arret;
import metiers.Ligne;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class LignePainter implements Painter<JXMapViewer> {

    private final List<Ligne> lignes;
    private final List<Ligne> lignesSelectionnees; // null = toutes

    public LignePainter(List<Ligne> lignes, List<Ligne> lignesSelectionnees) {
        this.lignes = lignes;
        this.lignesSelectionnees = lignesSelectionnees;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h) {

        g = (Graphics2D) g.create();
        g.setStroke(new BasicStroke(3));
        Rectangle viewport = map.getViewportBounds();

        for (Ligne ligne : lignes) {

            // Filtrage selon les lignes sélectionnées
            if (lignesSelectionnees != null && !lignesSelectionnees.contains(ligne)) {
                continue;
            }

            List<Arret> arrets = ligne.getArretsDesservis();
            if (arrets.size() < 2) continue;

            g.setColor(new Color(Integer.parseInt(ligne.getCouleur(), 16)));

            for (int i = 0; i < arrets.size() - 1; i++) {

                Arret a1 = arrets.get(i);
                Arret a2 = arrets.get(i + 1);

                Point2D p1 = map.getTileFactory().geoToPixel(
                        new GeoPosition(a1.getLatitude(), a1.getLongitude()), map.getZoom());

                Point2D p2 = map.getTileFactory().geoToPixel(
                        new GeoPosition(a2.getLatitude(), a2.getLongitude()), map.getZoom());

                int x1 = (int) (p1.getX() - viewport.getX());
                int y1 = (int) (p1.getY() - viewport.getY());
                int x2 = (int) (p2.getX() - viewport.getX());
                int y2 = (int) (p2.getY() - viewport.getY());

                g.drawLine(x1, y1, x2, y2);
            }
        }

        g.dispose();
    }
}
