package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.ReadBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class JDBC_ReadBenchmark extends JdbcBenchmark implements ReadBenchmark {

    final int ROWS_IN_DB = 100000;
    int wantedUserId;
    Blackhole blackhole;

    @Setup
    public void populateDatabase(Blackhole blackhole) {
        this.blackhole = blackhole;
        super.setUp();
        JdbcUtils.performBatchUsersInsert(connection, 100000, BATCH_SIZE);
        JdbcUtils.performBatchPhonesInsert(connection, 100000, BATCH_SIZE);
    }

    @Setup(Level.Invocation)
    public void randomUserId() {
        this.wantedUserId = new Random().nextInt(ROWS_IN_DB);
    }

    @Benchmark
    @Override
    public void findEntityByID() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM USER_TABLE WHERE id = ?");
            statement.setInt(1, wantedUserId);
            rs = statement.executeQuery();
            blackhole.consume(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(statement);
        }
    }

    @Benchmark
    @Override
    public void findAllEntities() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM USER_TABLE");
            rs = statement.executeQuery();
            blackhole.consume(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(statement);
        }
    }

    @Benchmark
    @Override
    public void findByStringProperty() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM USER_TABLE WHERE firstName = ?");
            statement.setString(1, "FirstName" + wantedUserId);
            rs = statement.executeQuery();
            blackhole.consume(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(statement);
        }
    }

    @Benchmark
    @Override
    public void findByIndexedStringColumn() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM USER_TABLE WHERE lastName = ?");
            statement.setString(1, "LastName" + wantedUserId);
            rs = statement.executeQuery();
            blackhole.consume(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(statement);
        }
    }

    @Benchmark
    @Override
    public void findByCollectionElement() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT u.* FROM USER_TABLE u, PHONE p WHERE p.userId = u.id and p.phoneNumber = ?");
            statement.setString(1, "111111111");
            rs = statement.executeQuery();
            blackhole.consume(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(statement);
        }
    }
}
