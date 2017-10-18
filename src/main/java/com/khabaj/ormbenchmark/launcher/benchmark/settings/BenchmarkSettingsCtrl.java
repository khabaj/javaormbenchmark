package com.khabaj.ormbenchmark.launcher.benchmark.settings;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import com.khabaj.ormbenchmark.benchmarks.OrmBenchmark;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.lang.StringUtils;
import org.openjdk.jmh.annotations.Mode;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BenchmarkSettingsCtrl implements Initializable {

    private static BenchmarkSettingsCtrl instance;

    @FXML
    JFXTreeView banchmarksTreeView;

    @FXML
    JFXTextField forksField;

    @FXML
    JFXTextField warmupIterationsField;

    @FXML
    JFXTextField measurementIteriationsField;

    @FXML
    JFXComboBox<Mode> benchmarkModeField;

    @FXML
    JFXComboBox<TimeUnit> timeUnitField;

    private BenchmarkSettings benchmarkSettings;

    public static BenchmarkSettingsCtrl getController() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        prepareAvailableBenchmarks();
        prepareBenchmarkSettingsForm();
        instance = this;
    }

    private void prepareAvailableBenchmarks() {

        final String PACKAGE_TO_SCAN = "com.khabaj.ormbenchmark.benchmarks";
        final String PACKAGE_TO_EXCLUDE_PATTERN = "com.khabaj.ormbenchmark.benchmarks.*.generated.*";

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(PACKAGE_TO_SCAN))
                .filterInputsBy(new FilterBuilder.Exclude(PACKAGE_TO_EXCLUDE_PATTERN))
                .setScanners(new SubTypesScanner());

        Reflections reflections = new Reflections(configurationBuilder);

        Method[] benchmarkMethods = OrmBenchmark.class.getDeclaredMethods();
        Set<Class<? extends OrmBenchmark>> benchmarkClasses = reflections.getSubTypesOf(OrmBenchmark.class);

        benchmarkClasses = benchmarkClasses.stream()
                .filter(el -> !Modifier.isAbstract(el.getModifiers()))
                .filter(el -> !Modifier.isInterface(el.getModifiers()))
                .collect(Collectors.toSet());

        buildBenchmarksTree(benchmarkMethods, benchmarkClasses);
    }

    private void buildBenchmarksTree(Method[] benchmarkMethods, Set<Class<? extends OrmBenchmark>> benchmarkClasses) {

        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>();
        rootItem.setExpanded(true);
        rootItem.setIndependent(true);

        for (Method method : benchmarkMethods) {
            List<CheckBoxTreeItem<String>> list = benchmarkClasses
                    .stream()
                    .map(clazz -> new CheckBoxTreeItem<>(clazz.getSimpleName()))
                    .collect(Collectors.toList());

            CheckBoxTreeItem<String> benchmarkItem = new CheckBoxTreeItem<>(method.getName());
            benchmarkItem.getChildren().addAll(list);
            benchmarkItem.setIndependent(false);
            rootItem.getChildren().add(benchmarkItem);
        }

        banchmarksTreeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
        banchmarksTreeView.setRoot(rootItem);
        banchmarksTreeView.setShowRoot(false);
    }

    private void prepareBenchmarkSettingsForm() {

        ChangeListener<String> acceptOnlyDigitsListener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                ((StringProperty) observable).set(oldValue);
            else if(!StringUtils.isEmpty(newValue) && Integer.parseInt(newValue) > 100000) {
                ((StringProperty) observable).set(oldValue);
            }
        };

        forksField.textProperty().addListener(acceptOnlyDigitsListener);
        warmupIterationsField.textProperty().addListener(acceptOnlyDigitsListener);
        measurementIteriationsField.textProperty().addListener(acceptOnlyDigitsListener);
        benchmarkModeField.getItems().addAll(Mode.values());
        timeUnitField.getItems().addAll(TimeUnit.values());

        benchmarkSettings = new BenchmarkSettings();

        Bindings.bindBidirectional(forksField.textProperty(), benchmarkSettings.forksProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(warmupIterationsField.textProperty(), benchmarkSettings.warmupIterationsProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(measurementIteriationsField.textProperty(), benchmarkSettings.measurementIteriationsProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(benchmarkModeField.valueProperty(), benchmarkSettings.benchmarkModeProperty());
        Bindings.bindBidirectional(timeUnitField.valueProperty(), benchmarkSettings.timeUnitProperty());

        setBenchmarkSettingsDefaultValues();
    }

    private void setBenchmarkSettingsDefaultValues() {
        benchmarkSettings.setForks(1);
        benchmarkSettings.setWarmupIterations(5);
        benchmarkSettings.setMeasurementIteriations(5);
        benchmarkSettings.setBenchmarkMode(Mode.AverageTime);
        benchmarkSettings.setTimeUnit(TimeUnit.MILLISECONDS);
    }

    private List<String> findCheckedItems(CheckBoxTreeItem<String> item) {
        List<String> benchmarksToRun = new ArrayList<>();

        if (item.isSelected() && item.isLeaf()) {
            benchmarksToRun.add(item.getValue() + "." + item.getParent().getValue());
        }
        for (TreeItem<?> child : item.getChildren()) {
            benchmarksToRun.addAll(findCheckedItems((CheckBoxTreeItem<String>) child));
        }
        return benchmarksToRun;
    }

    public BenchmarkSettings getBenchmarkSettings() {
        benchmarkSettings.setBenchmarksToRun(getBenchmarksToRun());
        return benchmarkSettings;
    }

    private List<String> getBenchmarksToRun() {
        return findCheckedItems((CheckBoxTreeItem<String>) banchmarksTreeView.getRoot());
    }
}
