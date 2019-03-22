package GUI.Palettes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import java.util.Comparator;

/**
 * To be used in code masterpiece
 * PaletteElements extend HBoxes and contain a Text element for the index and a node of type R for the corresponding
 * contents.
 * @param <R> Type of node that is the content of each Palette element e.g. Rectangle, ImageView, etc.
 * Author: Ryan Culhane
 * Code Masterpiece: I decided to include this class in my code masterpiece because it demonstrates effective use of
 * generics.  I orginally had an abstract PaletteElemet class with subclasses for TurtlePaletteElement and
 * ColorPaletteElemet to handle the differences in the contents of the palettes but I realized that a lot
 * of the code was very similar ad could be better handle using generics so I refactored what was a
 * hierarcy cotaining three classes into one short class that uses generics to handle the different
 * possible uses of palettes.
 */
public class PaletteElement<R extends Node> extends HBox {

    private static final int NO_PADDING = 0;
    private static final int MEDIUM_PADDING = 5;
    private static final String INDEX_DISPLAY_FORMATTING = ": ";
    private int myIndex;
    private R myContent;

    /**
     * PaletteElement constructor
     * @param index index of the palette element
     * @param content display content of the palette element
     */
    public PaletteElement(int index, R content){
        setFormatting();
        myIndex = index;
        myContent = content;
        getChildren().add(new Text(myIndex + INDEX_DISPLAY_FORMATTING));
        getChildren().add(myContent);
    }

    private void setFormatting() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(NO_PADDING, MEDIUM_PADDING, NO_PADDING, MEDIUM_PADDING));
    }

    int getMyIndex(){
        return myIndex;
    }

    R getMyDisplay(){
        return myContent;
    }

}
