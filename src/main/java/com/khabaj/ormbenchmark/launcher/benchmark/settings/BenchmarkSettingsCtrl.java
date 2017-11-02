package com.khabaj.ormbenchmark.launcher.benchmark.settings;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import com.khabaj.ormbenchmark.benchmarks.PersistenceBenchmark;
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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BenchmarkSettingsCtrl implements Initializable {

    private static BenchmarkSettingsCtrl instance;

    @FXML
    JFXTreeView banchmarksTreeView;

    @FXML
    JFXTextField benchmarkNameField;

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

        instance = this;
        prepareAvailableBenchmarks();
        prepareBenchmarkSettingsForm();

    }

    private void prepareAvailableBenchmarks() {

        final String PACKAGE_TO_SCAN = "com.khabaj.ormbenchmark.benchmarks";
        final String PACKAGE_TO_EXCLUDE_PATTERN = "com.khabaj.ormbenchmark.benchmarks.*.generated.*";

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(PACKAGE_TO_SCAN))
                .setScanners(new SubTypesScanner())
                .filterInputsBy(new FilterBuilder.Exclude(PACKAGE_TO_EXCLUDE_PATTERN));


        Reflections reflections = new Reflections(configurationBuilder);

        Set<Class<? extends PersistenceBenchmark>> subtypes = reflections.getSubTypesOf(PersistenceBenchmark.class);

        Set<Class<? extends PersistenceBenchmark>> benchmarks = subtypes.stream()
                .filter(el -> Modifier.isInterface(el.getModifiers()))
                .collect(Collectors.toSet());

        buildBenchmarksTree(reflections, benchmarks);
    }

    private void buildBenchmarksTree(Reflections reflections, Set<Class<? extends PersistenceBenchmark>> benchmarks) {

        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>();
        rootItem.setExpanded(true);
        rootItem.setIndependent(true);

        benchmarks.forEach((Class clazz) -> {

            CheckBoxTreeItem<String> interfaceItem = new CheckBoxTreeItem<>(clazz.getSimpleName());

            Set<Class> benchmarkClasses =
                    ((Set<Class>) reflections.getSubTypesOf(clazz)).stream()
                            .filter((Class el) -> !Modifier.isAbstract(el.getModifiers()))
                            .collect(Collectors.toSet());

            if (benchmarkClasses != null && !benchmarkClasses.isEmpty()) {
                rootItem.getChildren().add(interfaceItem);
            }

            Arrays.stream(clazz.getDeclaredMethods())
                    .map(Method::getName)
                    .sorted(Comparator.reverseOrder())
                    .forEach(method -> {
                        List<CheckBoxTreeItem<String>> list = benchmarkClasses
                                .stream()
                                .map((Class el) -> new CheckBoxTreeItem<>(el.getSimpleName()))
                                .collect(Collectors.toList());

                        CheckBoxTreeItem<String> benchmarkItem = new CheckBoxTreeItem<>(method);
                        benchmarkItem.getChildren().addAll(list);
                        benchmarkItem.setIndependent(false);
                        interfaceItem.getChildren().add(benchmarkItem);
                    });
        });
        banchmarksTreeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
        banchmarksTreeView.setRoot(rootItem);
        banchmarksTreeView.setShowRoot(false);
    }

    private void prepareBenchmarkSettingsForm() {

        ChangeListener<String> acceptOnlyDigitsListener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                ((StringProperty) observable).set(oldValue);
            else if (!StringUtils.isEmpty(newValue) && Integer.parseInt(newValue) > 100000) {
                ((StringProperty) observable).set(oldValue);
            }
        };

        forksField.textProperty().addListener(acceptOnlyDigitsListener);
        warmupIterationsField.textProperty().addListener(acceptOnlyDigitsListener);
        measurementIteriationsField.textProperty().addListener(acceptOnlyDigitsListener);
        benchmarkModeField.getItems().addAll(Mode.values());
        timeUnitField.getItems().addAll(TimeUnit.values());

        benchmarkSettings = new BenchmarkSettings();

        Bindings.bindBidirectional(benchmarkNameField.textProperty(), benchmarkSettings.benchmarkNameProperty());
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
