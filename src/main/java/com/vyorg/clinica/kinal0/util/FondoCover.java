package main.java.com.vyorg.clinica.kinal0.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public class FondoCover {

    public static void aplicar(ImageView imageView, Region contenedor) {
        Image imagen = imageView.getImage();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        DoubleBinding escala = Bindings.createDoubleBinding(() -> {
            double escalaAncho = contenedor.getWidth() / imagen.getWidth();
            double escalaAlto = contenedor.getHeight() / imagen.getHeight();
            return Math.max(escalaAncho, escalaAlto);
        }, contenedor.widthProperty(), contenedor.heightProperty());

        imageView.fitWidthProperty().bind(imagen.widthProperty().multiply(escala));
        imageView.fitHeightProperty().bind(imagen.heightProperty().multiply(escala));

        Rectangle recorte = new Rectangle();
        recorte.widthProperty().bind(contenedor.widthProperty());
        recorte.heightProperty().bind(contenedor.heightProperty());
        contenedor.setClip(recorte);
    }
}