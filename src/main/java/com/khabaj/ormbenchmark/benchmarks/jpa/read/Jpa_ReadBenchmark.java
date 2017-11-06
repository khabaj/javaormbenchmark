package com.khabaj.ormbenchmark.benchmarks.jpa.read;

import com.khabaj.ormbenchmark.benchmarks.ReadBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.Phone;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import com.khabaj.ormbenchmark.benchmarks.jpa.JpaBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;

import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class Jpa_ReadBenchmark extends JpaBenchmark implements ReadBenchmark {

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
        entityManager.clear();
    }

    @Benchmark
    @Override
    public void findEntityByID() {
        User user = entityManager.find(User.class, wantedUserId);
        blackhole.consume(user);
    }

    @Benchmark
    @Override
    public void findAllEntities() {
        List users = entityManager.createQuery("select u from User u").getResultList();
        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByStringProperty() {
        Query query = entityManager.createQuery("select u from User u where u.firstName = :firstName");
        query.setParameter("firstName", "FirstName" + wantedUserId);
        List users = query.getResultList();
        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByIndexedStringColumn() {
        Query query = entityManager.createQuery("select u from User u where u.lastName = :lastName");
        query.setParameter("lastName", "LastName" + wantedUserId);
        List users = query.getResultList();
        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByCollectionElement() {
        Query query = entityManager.createQuery("select u from User u, Phone p where u = p.user and p.phoneNumber = :phoneNumber");
        query.setParameter("phoneNumber", "111111111");
        List users = query.getResultList();
        blackhole.consume(users);
    }

    private void batchInsertUsersWithPhones(int rowsNumber) {

        entityManager.getTransaction().begin();
        for (int i = 1; i <= rowsNumber; i++) {

            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();

                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            }
            User user = new User("FirstName" + i, "LastName" + i);
            assignPhones(user, String.valueOf(i));
            entityManager.persist(user);
        }
        entityManager.getTransaction().commit();
    }

    private void assignPhones(User user, String beginPhoneString) {
        Set<Phone> phones = new HashSet<>();

        for (int i = 1; i <= 3; i++) {
            Phone phone = new Phone();
            phone.generatePhoneNumber(String.valueOf(beginPhoneString), String.valueOf(i));
            phone.setUser(user);
            phones.add(phone);
        }
        user.setPhones(phones);
    }
}
