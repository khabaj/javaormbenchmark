package com.khabaj.ormbenchmark.launcher.benchmark.settings;

import javafx.beans.property.*;
import org.openjdk.jmh.annotations.Mode;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BenchmarkSettings {

    private StringProperty benchmarkName;
    private IntegerProperty forks;
    private IntegerProperty warmupIterations;
    private IntegerProperty measurementIteriations;
    private ObjectProperty<Mode> benchmarkMode;
    private ObjectProperty<TimeUnit> timeUnit;
    private ObjectProperty<List<String>> benchmarksToRun;

    public BenchmarkSettings() {
        this.benchmarkName = new SimpleStringProperty();
        this.forks = new SimpleIntegerProperty();
        this.warmupIterations = new SimpleIntegerProperty();
        this.measurementIteriations = new SimpleIntegerProperty();
        this.benchmarkMode = new SimpleObjectProperty<>();
        this.timeUnit = new SimpleObjectProperty<>();
        this.benchmarksToRun = new SimpleObjectProperty<>();
    }

    public String getBenchmarkName() {
        return benchmarkName.get();
    }

    public StringProperty benchmarkNameProperty() {
        return benchmarkName;
    }

    public void setBenchmarkName(String benchmarkName) {
        this.benchmarkName.set(benchmarkName);
    }

    public int getForks() {
        return forks.get();
    }

    public IntegerProperty forksProperty() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks.set(forks);
    }

    public int getWarmupIterations() {
        return warmupIterations.get();
    }

    public IntegerProperty warmupIterationsProperty() {
        return warmupIterations;
    }

    public void setWarmupIterations(int warmupIterations) {
        this.warmupIterations.set(warmupIterations);
    }

    public int getMeasurementIteriations() {
        return measurementIteriations.get();
    }

    public IntegerProperty measurementIteriationsProperty() {
        return measurementIteriations;
    }

    public void setMeasurementIteriations(int measurementIteriations) {
        this.measurementIteriations.set(measurementIteriations);
    }

    public Mode getBenchmarkMode() {
        return benchmarkMode.get();
    }

    public ObjectProperty<Mode> benchmarkModeProperty() {
        return benchmarkMode;
    }

    public void setBenchmarkMode(Mode benchmarkMode) {
        this.benchmarkMode.set(benchmarkMode);
    }

    public TimeUnit getTimeUnit() {
        return timeUnit.get();
    }

    public ObjectProperty<TimeUnit> timeUnitProperty() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit.set(timeUnit);
    }

    public List<String> getBenchmarksToRun() {
        return benchmarksToRun.get();
    }

    public ObjectProperty<List<String>> benchmarksToRunProperty() {
        return benchmarksToRun;
    }

    public void setBenchmarksToRun(List<String> benchmarksToRun) {
        this.benchmarksToRun.set(benchmarksToRun);
    }
}
