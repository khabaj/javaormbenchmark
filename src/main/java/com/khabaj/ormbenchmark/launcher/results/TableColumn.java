package com.khabaj.ormbenchmark.launcher.results;

enum TableColumn {
    DATABASE("Database"),
    BENCHMARK("Benchmark"),
    PERSISTENCE_PROVIDER("Persistence Provider"),
    SCORE("Score"),
    SCORE_ERROR("Score Error"),
    UNIT("UNIT"),;

    private String columnName;

    TableColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
