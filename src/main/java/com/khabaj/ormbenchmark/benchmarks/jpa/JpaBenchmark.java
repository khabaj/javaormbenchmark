package com.khabaj.ormbenchmark.benchmarks.jpa;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.OrmBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.*;

import javax.persistence.EntityManager;

public abstract class JpaBenchmark extends BaseBenchmark implements OrmBenchmark{

    EntityManager entityManager;

    @Setup()
    public abstract void setUp() ;

    @TearDown
    public void closeEntityManager() {
        entityManager.close();
    }

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from User").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    @Benchmark
    //@Override
    public void insertOneRow() {
        User user = new User("John", "Stevens");
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

//    @Benchmark
//    @Override
//    public void insert100Rows() {
//        entityManager.getTransaction().begin();
//        for (int i = 0; i<100; i++) {
//            entityManager.persist(new User("John" + i, "LastName" + i));
//        }
//        entityManager.getTransaction().commit();
//    }
//
//    @Benchmark
//    //@Override
//    public void insert10000Rows() {
//        entityManager.getTransaction().begin();
//        for (int i = 0; i<10000; i++) {
//
//            if ( i > 0 && i % 5000 == 0 ) {
//                entityManager.flush();
//                entityManager.clear();
//
//                entityManager.getTransaction().commit();
//                entityManager.getTransaction().begin();
//            }
//            entityManager.persist(new User("John" + i, "LastName" + i));
//        }
//        entityManager.getTransaction().commit();
//    }

}
