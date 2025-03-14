import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;

public class Histogramm extends JFrame {
    // Definitions des attributs
    String titreGraphique;
    String nomAxis;
    String nomOrdonnes;
    JPanel chartPanel;
    String[][] tableauValeurs;

    // On define le constructeur
    public Histogramm(String titre, String abs, String ord, String[][] valeurs) {
        // On definie les information importante
        String titreGraphique = titre;
        String nomAxis = abs;
        String nomOrdonnes = ord;
        tableauValeurs = valeurs;

        // Nous créons un Dataset
        DefaultCategoryDataset donnees = new DefaultCategoryDataset();

        // On rajoute au dataSet les informations du tableau avec :
        // [0] les ordonnées
        // [1] les abcisses
        for (int i = 0; i < tableauValeurs[0].length; i++) {
            donnees.addValue(Double.parseDouble(tableauValeurs[0][i]), "Revenus", tableauValeurs[1][i]);
        }

        // On crée notre graphique
        JFreeChart graph = ChartFactory.createBarChart(titreGraphique,
                nomAxis, nomOrdonnes, donnees, PlotOrientation.VERTICAL, true, true, false);

        // On definie le plot
        CategoryPlot plot = graph.getCategoryPlot();

        // On rajoute 100 euros anotre range pour une meilleure observation
        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setRange(yAxis.getLowerBound(), yAxis.getUpperBound() + 100);

        // Avec ce code on colorie les barres en noir
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLACK);
        plot.setRenderer(renderer);

        // Puis on crée un label avec la valeur exacte des revenus
        CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
        renderer.setItemLabelGenerator(generator);
        renderer.setItemLabelsVisible(true);

        // on rajoute le graphique á notre panel
        chartPanel = new ChartPanel(graph);
        chartPanel.setPreferredSize(new java.awt.Dimension(190, 50));

    }
}