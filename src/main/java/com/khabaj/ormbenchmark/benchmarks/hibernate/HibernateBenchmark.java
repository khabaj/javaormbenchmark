package com.khabaj.ormbenchmark.benchmarks.hibernate;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.HibernateSpringConfiguration;
import com.khabaj.ormbenchmark.benchmarks.entities.Phone;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    protected void batchInsertUsersWithPhones(int rowsNumber) {

        session.getTransaction().begin();
        for (int i = 1; i <= rowsNumber; i++) {

            if (i > 0 && i % BATCH_SIZE == 0) {
                session.flush();
                session.clear();

                session.getTransaction().commit();
                session.getTransaction().begin();
            }
            User user = new User("FirstName" + i, "LastName" + i);
            assignPhones(user, String.valueOf(i));
            session.persist(user);
        }
        session.getTransaction().commit();
    }

    private void assignPhones(User user, String beginPhoneString) {
        Set<Phone> phones = new HashSet<>();

        for (int i=1; i <= 3; i++) {
            Phone phone = new Phone();
            phone.generatePhoneNumber(String.valueOf(beginPhoneString), String.valueOf(i));
            phone.setUser(user);
            phones.add(phone);
        }
        user.setPhones(phones);
    }

    protected List<User> getUsers() {
        List<User> users = session.createQuery("from User").list();
        return users;
    }
}
