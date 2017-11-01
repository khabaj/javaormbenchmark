package com.khabaj.ormbenchmark.benchmarks.hibernate;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.HibernateSpringConfiguration;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public abstract class HibernateBenchmark extends BaseBenchmark {

    protected ConfigurableApplicationContext applicationContext;
    protected Session session;

    @Setup
    public void setUp() {
        try {
            this.applicationContext = new AnnotationConfigApplicationContext(HibernateSpringConfiguration.class);
            this.session = applicationContext.getBean(SessionFactory.class).openSession();
        } catch (Exception e) {
            if (applicationContext != null) {
                applicationContext.close();
            }
            System.out.println(e.getMessage());
        }
    }

    @TearDown
    public void clear() {
        session.getTransaction().begin();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();
        session.clear();

        session.close();
        applicationContext.close();
    }

    protected void performBatchInsert(int rowsNumber) {

        session.getTransaction().begin();
        for (int i = 0; i < rowsNumber; i++) {

            if (i > 0 && i % BATCH_SIZE == 0) {
                session.flush();
                session.clear();

                session.getTransaction().commit();
                session.getTransaction().begin();
            }
            User user = new User("John" + i, "LastName" + i);
            session.persist(user);
        }
        session.getTransaction().commit();
    }

    protected List<User> getUsers() {
        List<User> users = session.createQuery("from User").list();
        return users;
    }
}
