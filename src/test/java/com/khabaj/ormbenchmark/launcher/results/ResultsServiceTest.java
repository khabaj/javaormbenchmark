package com.khabaj.ormbenchmark.launcher.results;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResultsServiceTest {

    @Test
    public void loadResults() throws Exception {
        String resultsDirectoryPath = "src/test/resources/2017-10-19 14-39-32";

        ResultsService resultsService = ResultsService.getInstance();
        List<Result> results = resultsService.loadResults(resultsDirectoryPath);

        Result result = new Result("insertOneRow", "EclipseLinkJpa", "MySQL",
                Double.parseDouble("0.010332468304035497"), Double.parseDouble("5.924050612913198E-4"), "ms/op");

        assertEquals(2, results.size());
        assertEquals(result, results.get(0));
    }
}