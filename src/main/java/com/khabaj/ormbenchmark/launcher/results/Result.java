package com.khabaj.ormbenchmark.launcher.results;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Result extends RecursiveTreeObject<Result> {

    private StringProperty operation;
    private StringProperty persistenceProvider;
    private StringProperty database;
    private DoubleProperty score;
    private DoubleProperty scoreError;
    private StringProperty scoreUnit;

    public Result() {
        this("","","",0d,0d,"");
    }

    public Result(String operation, String persistenceProvider, String database, Double score, Double scoreError, String scoreUnit) {
        this.operation = new SimpleStringProperty(operation);
        this.persistenceProvider = new SimpleStringProperty(persistenceProvider);
        this.database = new SimpleStringProperty(database);
        this.score = new SimpleDoubleProperty(score);
        this.scoreError = new SimpleDoubleProperty(scoreError);
        this.scoreUnit = new SimpleStringProperty(scoreUnit);
    }

    public String getOperation() {
        return operation.get();
    }

    public StringProperty operationProperty() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation.set(operation);
    }

    public String getPersistenceProvider() {
        return persistenceProvider.get();
    }

    public StringProperty persistenceProviderProperty() {
        return persistenceProvider;
    }

    public void setPersistenceProvider(String persistenceProvider) {
        this.persistenceProvider.set(persistenceProvider);
    }

    public String getDatabase() {
        return database.get();
    }

    public StringProperty databaseProperty() {
        return database;
    }

    public void setDatabase(String database) {
        this.database.set(database);
    }

    public Double getScore() {
        return score.get();
    }

    public DoubleProperty scoreProperty() {
        return score;
    }

    public void setScore(Double score) {
        this.score.set(score);
    }

    public Double getScoreError() {
        return scoreError.get();
    }

    public DoubleProperty scoreErrorProperty() {
        return scoreError;
    }

    public void setScoreError(double scoreError) {
        this.scoreError.set(scoreError);
    }

    public String getScoreUnit() {
        return scoreUnit.get();
    }

    public StringProperty scoreUnitProperty() {
        return scoreUnit;
    }

    public void setScoreUnit(String scoreUnit) {
        this.scoreUnit.set(scoreUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        if (getOperation() != null ? !getOperation().equals(result.getOperation()) : result.getOperation() != null)
            return false;
        if (getPersistenceProvider() != null ? !getPersistenceProvider().equals(result.getPersistenceProvider()) : result.getPersistenceProvider() != null)
            return false;
        return getDatabase() != null ? getDatabase().equals(result.getDatabase()) : result.getDatabase() == null;
    }

    @Override
    public int hashCode() {
        int result = getOperation() != null ? getOperation().hashCode() : 0;
        result = 31 * result + (getPersistenceProvider() != null ? getPersistenceProvider().hashCode() : 0);
        result = 31 * result + (getDatabase() != null ? getDatabase().hashCode() : 0);
        return result;
    }
}
