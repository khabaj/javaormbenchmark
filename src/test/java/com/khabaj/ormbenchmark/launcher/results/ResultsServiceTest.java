package com.khabaj.ormbenchmark.launcher.results;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResultsServiceTest {

    @Test
    public void loadResults() throws Exception {
        String resultsDirectoryPath = "src/test/resources/2017-10-25 08-50-10";

        ResultsService resultsService = ResultsService.getInstance();
        List<Result> results = resultsService.loadResults(resultsDirectoryPath);

        Result result = new Result("insert100Rows", "EclipseLinkJpa", "MySQL",
                Double.parseDouble("13.2352"), Double.parseDouble("0.604"), "ms/op");

        assertEquals(6, results.size());
        assertEquals(result, results.get(0));
    }
}