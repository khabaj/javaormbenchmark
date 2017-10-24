package com.khabaj.ormbenchmark.launcher.results;

public class Result {

    private String operation;
    private String persistenceProvider;
    private String database;
    private Double score;
    private Double scoreError;
    private String scoreUnit;

    public Result() {
    }

    public Result(String operation, String persistenceProvider, String database, Double score, Double scoreError, String scoreUnit) {
        this.operation = operation;
        this.persistenceProvider = persistenceProvider;
        this.database = database;
        this.score = score;
        this.scoreError = scoreError;
        this.scoreUnit = scoreUnit;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPersistenceProvider() {
        return persistenceProvider;
    }

    public void setPersistenceProvider(String persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getScoreUnit() {
        return scoreUnit;
    }

    public void setScoreUnit(String scoreUnit) {
        this.scoreUnit = scoreUnit;
    }

    public Double getScoreError() {
        return scoreError;
    }

    public void setScoreError(Double scoreError) {
        this.scoreError = scoreError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        if (operation != null ? !operation.equals(result.operation) : result.operation != null) return false;
        if (persistenceProvider != null ? !persistenceProvider.equals(result.persistenceProvider) : result.persistenceProvider != null)
            return false;
        if (database != null ? !database.equals(result.database) : result.database != null) return false;
        if (score != null ? !score.equals(result.score) : result.score != null) return false;
        if (scoreError != null ? !scoreError.equals(result.scoreError) : result.scoreError != null) return false;
        return scoreUnit != null ? scoreUnit.equals(result.scoreUnit) : result.scoreUnit == null;
    }

    @Override
    public int hashCode() {
        int result = operation != null ? operation.hashCode() : 0;
        result = 31 * result + (persistenceProvider != null ? persistenceProvider.hashCode() : 0);
        result = 31 * result + (database != null ? database.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (scoreError != null ? scoreError.hashCode() : 0);
        result = 31 * result + (scoreUnit != null ? scoreUnit.hashCode() : 0);
        return result;
    }
}
