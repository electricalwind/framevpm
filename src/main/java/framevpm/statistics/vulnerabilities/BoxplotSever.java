package framevpm.statistics.vulnerabilities;

import de.erichseifert.gral.data.Column;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.plots.BoxPlot;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.colors.LinearGradient;
import de.erichseifert.gral.plots.colors.ScaledContinuousColorMapper;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.DataUtils;
import de.erichseifert.gral.util.GraphicsUtils;
import framevpm.statistics.Panel;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class BoxplotSever extends Panel {

    private final Map<String, List<Double>> sever;

    public BoxplotSever(Map<String, List<Double>> sever) {
        this.sever = sever;
        DataTable data = new DataTable(sever.size(), Double.class);
        int i = 0;
        int size = sever.size();
        Double[] place = new Double[size];
        String[] rel = new String[size];
        for (Map.Entry<String, List<Double>> entry : sever.entrySet()) {

            int finalI = i;
            entry.getValue().forEach(severity -> {
                Double[] arr = new Double[size];
                for (int j = 0; j < size; j++) {
                    arr[j] = null;
                    if (finalI == j) {
                        arr[j] = severity;
                    }
                    data.add(arr);
                }
            });
            place[i] = Double.valueOf(i + 1);
            rel[i] = entry.getKey();
            i++;


        }

        DataSource boxData = BoxPlot.createBoxData(data);
        BoxPlot plot = new BoxPlot(boxData);
        plot.setInsets(new Insets2D.Double(20.0, 50.0, 40.0, 20.0));



        plot.getAxisRenderer(BoxPlot.AXIS_X).setCustomTicks(
                DataUtils.map(
                        place,
                        rel
                )
        );

        Stroke stroke = new BasicStroke(2f);
        ScaledContinuousColorMapper colors =
                new LinearGradient(GraphicsUtils.deriveBrighter(COLOR1), Color.WHITE);
        colors.setRange(1.0, 3.0);

        BoxPlot.BoxWhiskerRenderer pointRenderer =
                (BoxPlot.BoxWhiskerRenderer) plot.getPointRenderers(boxData).get(0);
        pointRenderer.setWhiskerStroke(stroke);
        pointRenderer.setBoxBorderStroke(stroke);
        pointRenderer.setBoxBackground(colors);
        pointRenderer.setBoxBorderColor(COLOR1);
        pointRenderer.setWhiskerColor(COLOR1);
        pointRenderer.setCenterBarColor(COLOR1);

        plot.getNavigator().setDirection(XYPlot.XYNavigationDirection.VERTICAL);

        // Add plot to Swing component
        InteractivePanel panel = new InteractivePanel(plot);
        add(panel);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }


}
