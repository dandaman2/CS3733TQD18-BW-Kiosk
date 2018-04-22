package edu.wpi.cs3733d18.teamQ.ui.Controller;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import java.util.*;
import java.util.stream.Collectors;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.getNodes;

public class AutoCompleteTextField extends JFXTextField {
    //Local variables
    //entries to autocomplete
    private final ArrayList<String> entries;

    // screenUtil object for request room searching
    ScreenUtil pUtil = new ScreenUtil();
    public ArrayList<Node> nodes;

    //popup GUI
    private ContextMenu entriesPopup;


    public AutoCompleteTextField() {
        super();
        this.entries = setUpEntries();
        this.entriesPopup = new ContextMenu();

        setListner();
    }


    /**
     * "Suggestion" specific listners
     */
    private void setListner() {
        //Add "suggestions" by changing text
        textProperty().addListener((observable, oldValue, newValue) -> {
            String enteredText = getText();
            //always hide suggestion if nothing has been entered (only "spacebars" are dissallowed in TextFieldWithLengthLimit)
            if (enteredText == null || enteredText.isEmpty()) {
                entriesPopup.hide();
            } else {
                //filter all possible suggestions depends on "Text", case insensitive
                List<String> filteredEntries = setUpEntries();
                //some suggestions are found
                if (!filteredEntries.isEmpty()) {
                    //build popup - list of "CustomMenuItem"
                    populatePopup(filteredEntries, enteredText);
                    if (!entriesPopup.isShowing()) { //optional
                        entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0); //position of popup
                    }
                    //no suggestions -> hide
                } else {
                    entriesPopup.hide();
                }
            }
        });

        //Hide always by focus-in (optional) and out
        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            entriesPopup.hide();
        });
    }


    /**
     * Populate the entry set with the given search results. Display is limited to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult, String searchReauest) {
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        //Build list as set of labels
        for (int i = 0; i < count; i++) {
            final String result = searchResult.get(i);
            //label with graphic (text flow) to highlight founded subtext in suggestions
            Label entryLabel = new Label();
            entryLabel.setText(result);
            //entryLabel.setGraphic(Styles.buildTextFlow(result, searchReauest));
            entryLabel.setPrefWidth(400);
            entryLabel.setPrefHeight(15);  //don't sure why it's changed with "graphic"
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            //if any suggestion is select set it into text and close popup
            item.setOnAction(actionEvent -> {
                setText(result);
                positionCaret(result.length());
                entriesPopup.hide();
            });
        }

        //"Refresh" context menu
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }


    /**
     * Get the existing set of autocomplete entries.
     *
     * @return The existing autocomplete entries.
     */
    public ArrayList<String> getEntries() { return entries; }


    private ArrayList<String> setUpEntries(){
        String text = this.getText();
        nodes = getNodes();

        ArrayList<String> nodeIdentification = pUtil.getNameIdNode(nodes);

        List<ExtractedResult> fuzzyFilter =  FuzzySearch.extractSorted(text , nodeIdentification);

        ArrayList<String> currentFilter = new ArrayList<String>();
        for(int i = 0; i < fuzzyFilter.size(); i++){
            ExtractedResult current = fuzzyFilter.get(i);
            System.out.println(current.getString() + " - " + current.getScore() + " for " +text);

            currentFilter.add(current.getString());
        }
        return currentFilter;
    }
}
