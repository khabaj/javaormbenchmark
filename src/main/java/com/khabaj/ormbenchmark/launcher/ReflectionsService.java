package com.khabaj.ormbenchmark.launcher;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class ReflectionsService {

    private static ReflectionsService instance;
    private Reflections reflections;


    private ReflectionsService() {
    }

    public static ReflectionsService getInstance() {
        if (instance == null) {
            instance = new ReflectionsService();
        }
        return instance;
    }

    public void initialize() {
        final String PACKAGE_TO_SCAN = "com.khabaj.ormbenchmark.benchmarks";
        final String PACKAGE_TO_EXCLUDE_PATTERN = "com.khabaj.ormbenchmark.benchmarks.*.generated.*";

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(PACKAGE_TO_SCAN))
                .setScanners(new SubTypesScanner())
                .filterInputsBy(new FilterBuilder.Exclude(PACKAGE_TO_EXCLUDE_PATTERN));

        reflections = new Reflections(configurationBuilder);
    }

    public Reflections getReflections() {
        return reflections;
    }
}
