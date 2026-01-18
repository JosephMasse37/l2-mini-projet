package LoireUrbanisme.map;

import metiers.Arret;
import metiers.Ligne;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class ArretPainter implements Painter<JXMapViewer> {

    private final List<Arret> arrets;
    private final List<Ligne> lignesSelectionnees; // null = toutes

    public ArretPainter(List<Arret> arrets, List<Ligne> lignesSelectionnees) {
        this.arrets = arrets;
        this.lignesSelectionnees = lignesSelectionnees;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h) {

        g = (Graphics2D) g.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle viewport = map.getViewportBounds();

        for (Arret a : arrets) {

            // Filtrage par lignes sélectionnées
            if (lignesSelectionnees != null) {
                boolean estDesservi = false;
                for (Ligne ligne : lignesSelectionnees) {
                    for (Arret arretLigne : ligne.getArretsDesservis()) {
                        if (arretLigne.getIdArret() == a.getIdArret()) {
                            estDesservi = true;
                            break;
                        }
                    }
                    if (estDesservi) break;
                }
                if (!estDesservi) continue;
            }

            GeoPosition geo = new GeoPosition(a.getLatitude(), a.getLongitude());
            Point2D pt = map.getTileFactory().geoToPixel(geo, map.getZoom());

            int x = (int) (pt.getX() - viewport.getX());
            int y = (int) (pt.getY() - viewport.getY());

            g.setColor(Color.BLACK);
            g.fillOval(x - 5, y - 5, 7, 7);

            g.setColor(Color.BLACK);
            g.drawString(a.getNom(), x + 7, y - 7);
        }

        g.dispose();
    }
}
