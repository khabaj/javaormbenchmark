package com.khabaj.ormbenchmark.benchmarks.hibernate;

import com.khabaj.ormbenchmark.benchmarks.ReadBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.hibernate.query.Query;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.Random;

public class Hibernate_ReadBenchmark extends HibernateBenchmark implements ReadBenchmark {

    final int ROWS_IN_DB = 100000;
    int wantedUserId;
    Blackhole blackhole;

    @Setup
    public void prepare(Blackhole blackhole) {
        this.blackhole = blackhole;
        batchInsertUsersWithPhones(ROWS_IN_DB);
    }

    @Setup(Level.Invocation)
    public void randomUserId() {
        this.wantedUserId = new Random().nextInt(ROWS_IN_DB);
    }

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        session.clear();
    }

    @Benchmark
    @Override
    public void findEntityByID() {
        User user = session.get(User.class, wantedUserId);
        blackhole.consume(user);
    }

    @Benchmark
    @Override
    public void findAllEntities() {
        List users = session.createQuery("select u from User u").getResultList();
        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByStringProperty() {
        Query query = session.createQuery("select u from User u where u.firstName = :firstName");
        query.setParameter("firstName", "FirstName" + wantedUserId);
        List users = query.list();
        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByIndexedStringColumn() {
        Query query = session.createQuery("select u from User u where u.lastName = :lastName");
        query.setParameter("lastName", "LastName" + wantedUserId);
        List users = query.list();
        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByCollectionElement() {
        Query query = session.createQuery("select u from User u, Phone p where u = p.user and p.phoneNumber = :phoneNumber");
        query.setParameter("phoneNumber", "111111111");
        List users = query.list();
        blackhole.consume(users);
    }
}
