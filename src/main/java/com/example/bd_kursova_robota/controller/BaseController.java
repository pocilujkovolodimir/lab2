package com.example.bd_kursova_robota.controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class BaseController {

    public interface DialogListener{
        void onSave();
        void onCancel();
        void onTitleVisibilityChanged(boolean titleVisible);
    }

    private DialogListener dialogListener;

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public DialogListener getDialogListener() {
        return dialogListener;
    }

    public static final int DURATION_IN = 200;
    public static final int DURATION_OUT = 150;
    public static final int DURATION_VISIBLE = 1800;

    private Popup popup;
    private Timer timer;
    private FadeTransition fadeTransition;

    private double popupInsetLeft = 0;
    private double popupInsetTop = 0;
    private double popupInsetRight = 0;
    private double popupMarginTop = 0;

    public void setPopupMarginTop(double popupMarginTop) {
        this.popupMarginTop = popupMarginTop;
    }

    public void setPopupInsetRight(double popupInsetRight) {
        this.popupInsetRight = popupInsetRight;
    }

    public void setPopupInsetLeft(double popupInsetLeft) {
        this.popupInsetLeft = popupInsetLeft;
    }

    public void setPopupInsetTop(double popupInsetTop) {
        this.popupInsetTop = popupInsetTop;
    }

    public Popup createPopup(final String message) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setPadding(new Insets(8, 24, 8, 24));
        label.setBackground(new Background(new BackgroundFill(Color.valueOf("#2f2f2fde"), new CornerRadii(6), Insets.EMPTY)));
        label.setStyle( "-fx-effect: dropshadow(gaussian, #2f2f2ff0, " + 24 + ", 0, 0, 4);" );
        label.setTextFill(Color.valueOf("#ffffff"));
        label.setOnMouseEntered(e -> hidePopup(false));
        popup.getContent().add(label);
        return popup;
    }

    public void showPopupMessage(final String message, final Window stage){
        showPopupMessage(message, stage, null);
    }

    public void showPopupMessage(final String message, Node alignTo){
        showPopupMessage(message, getPopupWindow(), alignTo);
    }

    public void showPopupMessage(final String message){
        showPopupMessage(message, getPopupWindow());
    }

    private long getPopupDelay() {
        return DURATION_IN + DURATION_VISIBLE;
    }

    public void showPopupMessage(final String message, final Window stage, Node alignTo) {
        if (popup != null){
            hidePopup(true);
        }
        popup = createPopup(message);
        popup.setOnShown(e -> {
            if (alignTo == null) {
                popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
                popup.setY(stage.getY() + stage.getHeight() / 2 - popup.getHeight() / 2);
            }
            else{
                Point2D point2D = alignTo.localToScene(0, 0);
                double x = stage.getX() + point2D.getX() - popupInsetLeft;
                double stageEndX = stage.getX() + stage.getWidth();
                if (x + popup.getWidth() > stageEndX){
                    x = stage.getX() + point2D.getX() + alignTo.getBoundsInLocal().getWidth() - popup.getWidth() + popupInsetLeft;
                }
                popup.setX(x);
                popup.setY(stage.getY() + point2D.getY() + alignTo.getBoundsInLocal().getHeight() + popupMarginTop - popupInsetTop);
            }
        });
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    hidePopup(false);
                });
            }
        }, getPopupDelay());
        popup.show(stage);
        fadeTransition = getFadeTransition(popup, DURATION_IN, 0f, 1f, null);
    }



    public void hidePopup(boolean force){
        if (popup == null) return;
        fadeTransition.stop();
        if (force) {
            hidePopupImpl();
        }
        else{
           fadeTransition = getFadeTransition(popup, DURATION_OUT, 1f, 0f, event -> {
               hidePopupImpl();
           });
        }
    }

    public static FadeTransition getFadeTransition(Popup popup, int duration, double from, double to, EventHandler<ActionEvent> onFinished) {
        return getFadeTransition(popup.getScene().getRoot(), duration, from, to, onFinished);
    }

    public static FadeTransition getFadeTransition(Node node, int duration, double from, double to, EventHandler<ActionEvent> onFinished){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(duration));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.setOnFinished(onFinished);
        fadeTransition.play();
        return fadeTransition;
    }

    public static ParallelTransition getPageTransition(Node node, int duration, double fromAlpha, double toAlpha, double fromX, double toX, EventHandler<ActionEvent> onFinished){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(duration));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(fromAlpha);
        fadeTransition.setToValue(toAlpha);
        TranslateTransition translateTransition = new TranslateTransition();

        translateTransition.setNode(node);
        translateTransition.setFromX(fromX);
        translateTransition.setToX(toX);
        ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, translateTransition);
        parallelTransition.setOnFinished(onFinished);
        translateTransition.setDuration(Duration.millis(duration));
        parallelTransition.play();
        return parallelTransition;
    }


    private void hidePopupImpl() {
        popup.hide();
        popup = null;
        timer.cancel();
    }

    public Window getPopupWindow(){
        return null;
    }

}
