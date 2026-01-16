package LoireUrbanisme.map;

import javax.swing.event.MouseInputListener;

import metiers.Arret;
import metiers.Ligne;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.ArrayList;
import java.util.List;

public class Map extends JXMapViewer {

    private MapEvent event;
    private List<Ligne> lignesSelectionnees; // null = toutes les lignes

    public MapEvent getEvent() {
        return event;
    }

    public void setEvent(MapEvent event) {
        this.event = event;
    }

    public List<Ligne> getLignesSelectionnees() {
        return lignesSelectionnees;
    }

    public void setLignesSelectionnees(List<Ligne> lignesSelectionnees) {
        // si null => toutes les lignes
        if (lignesSelectionnees == null) {
            this.lignesSelectionnees = null;
        } else {
            // copie pour éviter les effets de bord
            this.lignesSelectionnees = new ArrayList<>(lignesSelectionnees);
        }
        repaint();
    }

    public Map() {
        init();
    }

    public void init() {
        setTileFactory(new DefaultTileFactory(
                new OSMTileFactoryInfo("", "https://b.tile.openstreetmap.fr/hot/")
        ));
        setAddressLocation(new GeoPosition(47.390034, 0.688837));
        setZoom(6);

        MouseInputListener mm = new PanMouseInputListener(this);
        addMouseListener(mm);
        addMouseMotionListener(mm);
        addMouseWheelListener(new ZoomMouseWheelListenerCenter(this));
    }

    public void afficherReseau(List<Arret> arrets, List<Ligne> lignes) {

        List<Painter<JXMapViewer>> painters = new ArrayList<>();

        painters.add(new LignePainter(lignes, lignesSelectionnees)); // lignes (filtrées)
        painters.add(new ArretPainter(arrets, lignesSelectionnees)); // arrêts au-dessus

        setOverlayPainter(new CompoundPainter<>(painters));
        repaint();
    }
}
