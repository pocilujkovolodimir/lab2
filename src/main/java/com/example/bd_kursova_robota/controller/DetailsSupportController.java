package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.HelloApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailsSupportController extends BaseController {

    private static final int DETAILS_ANIMATION_DURATION = 250;

    @FXML Node panelDetails;
    @FXML Node panelRoot;
    @FXML StackPane panelDetailsContent;
    @FXML Button buttonDetailsOk;
    @FXML Button buttonDetailsCancel;
    @FXML Label detailsTitle;

    private double screenWidth = 0;
    private String detailsFxml = null;
    private DetailsController detailsController = null;
    private boolean detailsVisible = false;

    private double getScreenWidth(){
        if (screenWidth == 0){
            screenWidth = panelRoot.getBoundsInLocal().getWidth();
        }
        return screenWidth;
    }

    @FXML
    public void onDetailsCancel(){
        onDetailsCancel(null);
    }

    public void onDetailsCancel(Runnable onEnd){
        if (!detailsVisible) return;
        detailsVisible = false;
        if (getDialogListener() != null) getDialogListener().onTitleVisibilityChanged(true);
        BaseController.getPageTransition(panelDetails, DETAILS_ANIMATION_DURATION, 1d, 0d, 0, getScreenWidth(), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        BaseController.getPageTransition(panelRoot, DETAILS_ANIMATION_DURATION, 0d, 1d, -getScreenWidth(), 0, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                panelDetails.setVisible(false);
                panelDetailsContent.getChildren().clear();
                if (onEnd != null){
                    Platform.runLater(onEnd);
                }
            }
        });
    }

    @FXML
    public void onDetailsDone(){
        ArrayList<DoneBlock> doneBlocks = detailsController.getDoneBlocks();
        if (doneBlocks != null && doneBlocks.size() > 0){
            DoneBlock doneBlock = doneBlocks.get(0);
            showPopupMessage(doneBlock.getMessage(), doneBlock.getNode());
            return;
        }
        onDetailsCancel(()->{
            int requestCode = detailsController.getRequestCode();
            if (requestCode == -1) {
                onDetailsResult(detailsFxml, detailsController.getResult());
            }
            else{
                onDetailsResult(detailsFxml, detailsController.getResult(), requestCode);
            }
            detailsController = null;
            detailsFxml = null;
        });
    }

    public void openDetails(String fxml, HashMap<Integer, Object> arguments){
        openDetails(fxml, arguments, -1);
    }

    public void openDetails(String fxml, HashMap<Integer, Object> arguments, int requestCode){
        if (detailsVisible) return;
        try {
            detailsVisible = true;
            if (getDialogListener() != null) getDialogListener().onTitleVisibilityChanged(false);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
            Node node = fxmlLoader.load();
            detailsFxml = fxml;
            detailsController = fxmlLoader.getController();
            detailsController.setOkButtonBridge(new DetailsController.OKButtonBridge() {
                @Override
                public void setOKEnabled(boolean enabled) {
                    buttonDetailsOk.setDisable(!enabled);
                }
            });
            detailsController.setRequestCode(requestCode);
            detailsController.onApplyArguments(arguments);
            panelDetailsContent.getChildren().add(node);
            buttonDetailsOk.setVisible(detailsController.canReturnResult());
            detailsTitle.setText(detailsController.getTitle());
            panelDetails.setVisible(true);
            BaseController.getPageTransition(panelDetails, DETAILS_ANIMATION_DURATION, 0d, 1d, getScreenWidth(), 0, null);
            BaseController.getPageTransition(panelRoot, DETAILS_ANIMATION_DURATION, 1d, 0d, 0, -getScreenWidth(), null);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void onDetailsResult(String fxml, Object object){

    }

    public void onDetailsResult(String fxml, Object object, int requestCode){

    }


    @Override
    public Window getPopupWindow() {
        return panelDetails.getScene().getWindow();
    }
}
