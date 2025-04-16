package de.bund.bva.isyfact.datetime.persistence.attributeconverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Period;
import java.util.Random;

import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PeriodAttributeConverterTest.TestConfig.class)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class
})
@Transactional
@DatabaseSetup("testPeriodSetup.xml")
public class PeriodAttributeConverterTest {

    @Autowired
    protected TestEntityManager entityManager;
    final PeriodAttributeConverter converter = new PeriodAttributeConverter();
    final Random rng = new Random();

    @AfterEach
    public void commit() {
        entityManager.flush();
    }

    @Test
    void testConvertToDatabaseColumn() {
        int years = rng.nextInt(100) + 1;
        int months = rng.nextInt(11) + 1;
        int days = rng.nextInt(30) + 1;
        Period period = Period.of(years, months, days);
        String columnString = converter.convertToDatabaseColumn(period);
        String expectedColumnString = String.format("P%dY%dM%dD", years, months, days);
        assertThat(columnString).isEqualTo(expectedColumnString);
    }

    @Test
    void testConvertToEntityAttribute() {
        int years = rng.nextInt(100) + 1;
        int months = rng.nextInt(11) + 1;
        int days = rng.nextInt(30) + 1;
        Period period = converter.convertToEntityAttribute(String.format("P%dY%dM%dD", years, months, days));
        Period expextedPeriod = Period.of(years, months, days);
        assertThat(period).isEqualTo(expextedPeriod);
    }

    @Test
    @ExpectedDatabase(value = "testPeriodExpectedWriteNull.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void convertToDatabaseColumnNull() {
        TestPeriodEntity testEntity = new TestPeriodEntity();
        testEntity.setId(2);
        testEntity.setPeriod(null);

        entityManager.persist(testEntity);
    }

    @Test
    void convertToEntityAttributeNull() {
        TestPeriodEntity testEntity = entityManager.find(TestPeriodEntity.class, 1L);

        assertThat(testEntity.getPeriod()).isNull();
    }

    @Test
    void convertToDatabaseColumn2() {
        TestPeriodEntity testEntity = new TestPeriodEntity();
        testEntity.setId(2);
        testEntity.setPeriod(Period.of(3, 4, 5));

        entityManager.persist(testEntity);
    }

    @Test
    @ExpectedDatabase(value = "testPeriodExpectedWritePeriod.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void convertToDatabaseColumn() {
        TestPeriodEntity testEntity = new TestPeriodEntity();
        testEntity.setId(2);
        testEntity.setPeriod(Period.of(3, 4, 5));

        entityManager.persist(testEntity);
    }

    @Test
    void convertToEntityAttribute() {
        TestPeriodEntity testEntity = entityManager.find(TestPeriodEntity.class, 0L);

        assertEquals(Period.of(10, 20, 30), testEntity.getPeriod());
    }

    @EnableTransactionManagement
    @AutoConfigureTestEntityManager
    @AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
    static class TestConfig {
        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setPackagesToScan("de.bund.bva.isyfact.datetime.persistence.attributeconverter");
            em.setDataSource(dataSource);
            em.setJpaDialect(new HibernateJpaDialect());

            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            vendorAdapter.setGenerateDdl(true);
            vendorAdapter.setDatabase(Database.H2);
            vendorAdapter.setShowSql(false);
            em.setJpaVendorAdapter(vendorAdapter);

            return em;
        }

        @Bean
        public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
            JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(emf);
            return transactionManager;
        }
    }
}
