package com.khabaj.ormbenchmark.benchmarks.spring.springdata;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.EntityManagerFactoryConfiguration;
import com.khabaj.ormbenchmark.benchmarks.configuration.SpringDataConfiguration;
import com.khabaj.ormbenchmark.benchmarks.entities.Phone;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import com.khabaj.ormbenchmark.benchmarks.spring.springdata.repositories.UserRepository;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SpringData_Benchmark extends BaseBenchmark {

    protected AnnotationConfigApplicationContext entityFactoryContext;
    protected ConfigurableApplicationContext applicationContext;
    protected UserRepository userRepository;

    public void setUp() {
        this.setUp(BATCH_SIZE);
    }

    public void setUp(int batchSize) {
        try {
            this.entityFactoryContext =
                    new AnnotationConfigApplicationContext(EntityManagerFactoryConfiguration.class);
            this.entityFactoryContext.getBean(EntityManagerFactory.class, batchSize);

            this.applicationContext = new AnnotationConfigApplicationContext();
            this.applicationContext.setParent(entityFactoryContext);
            ((AnnotationConfigApplicationContext)applicationContext).register(SpringDataConfiguration.class);
            applicationContext.refresh();
            this.userRepository = applicationContext.getBean(UserRepository.class);

        } catch (Exception e) {
            if (applicationContext != null) {
                applicationContext.close();
            }
            System.out.println(e.getMessage());
        }
    }

    @TearDown
    public void clear() {
        entityFactoryContext.close();
        applicationContext.close();
    }

    protected void batchInsertUsers(int rowsToInsert) {
        batchInsertUsers(rowsToInsert, BATCH_SIZE);
    }

    protected void batchInsertUsers(int rowsToInsert, int batchSize) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= rowsToInsert; i++) {
            if (i % batchSize == 0) {
                userRepository.save(users);
                users.clear();
            }
            users.add(new User("FirstName" + i, "LastName" + i));
        }
        userRepository.save(users);
    }

    protected void batchInsertUsersWithPhones(int rowsToInsert) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= rowsToInsert; i++) {
            if (i % BATCH_SIZE == 0) {
                userRepository.save(users);
                users.clear();
            }
            User user = new User("FirstName" + i, "LastName" + i);
            assignPhones(user, String.valueOf(i));
            users.add(user);
        }
        userRepository.save(users);
    }

    private void assignPhones(User user, String beginPhoneString) {
        Set<Phone> phones = new HashSet<>();

        for (int i=1; i <= 3; i++) {
            Phone phone = new Phone();
            phone.generatePhoneNumber(beginPhoneString, String.valueOf(i));
            phone.setUser(user);
            phones.add(phone);
        }
        user.setPhones(phones);
    }
}
