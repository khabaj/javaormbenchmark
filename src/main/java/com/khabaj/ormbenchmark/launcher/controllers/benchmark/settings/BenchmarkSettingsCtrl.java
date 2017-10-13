package com.khabaj.ormbenchmark.launcher.controllers.benchmark.settings;

import com.jfoenix.controls.JFXTreeView;
import com.khabaj.ormbenchmark.benchmarks.OrmBenchmark;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTreeCell;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class BenchmarkSettingsCtrl implements Initializable {

    @FXML
    JFXTreeView banchmarksTreeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

        TreeItem<String> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);

        for (Method method : benchmarkMethods) {
            List<TreeItem<String>> list = benchmarkClasses
                    .stream()
                    .map(clazz -> new TreeItem<>(clazz.getSimpleName()))
                    .collect(Collectors.toList());

            CheckBoxTreeItem<String> benchmarkItem = new CheckBoxTreeItem<>(method.getName());
            benchmarkItem.getChildren().addAll(list);
            benchmarkItem.setExpanded(true);

            rootItem.getChildren().add(benchmarkItem);
        }

        banchmarksTreeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        banchmarksTreeView.setRoot(rootItem);
        banchmarksTreeView.setShowRoot(false);
    }
}
