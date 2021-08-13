package com.darkgran.farstar;

import com.darkgran.farstar.gui.TableStage;

public final class ScreenSettings {
    private boolean tableStageEnabled = true;
    private boolean netEnabled = false;
    private boolean tokenFramesEnabled = true;
    private boolean perfMeterEnabled = true;
    private boolean f1buttonEnabled = true;

    public boolean isTableStageEnabled() {
        return tableStageEnabled;
    }

    public void setTableStageEnabled(boolean tableStageEnabled, TableStage tableStage) {
        this.tableStageEnabled = tableStageEnabled;
        if (tableStage != null) { tableStage.enableButtons(tableStageEnabled); }
    }

    public boolean isNetEnabled() {
        return netEnabled;
    }

    public void setNetEnabled(boolean netEnabled) {
        this.netEnabled = netEnabled;
    }

    public boolean areTokenFramesEnabled() {
        return tokenFramesEnabled;
    }

    public void setTokenFramesEnabled(boolean tokenFramesEnabled) {
        this.tokenFramesEnabled = tokenFramesEnabled;
    }

    public boolean isPerfMeterEnabled() {
        return perfMeterEnabled;
    }

    public void setPerfMeterEnabled(boolean perfMeterEnabled) {
        this.perfMeterEnabled = perfMeterEnabled;
    }

    public boolean isF1buttonEnabled() {
        return f1buttonEnabled;
    }

    public void setF1buttonEnabled(boolean f1buttonEnabled) {
        this.f1buttonEnabled = f1buttonEnabled;
    }
}