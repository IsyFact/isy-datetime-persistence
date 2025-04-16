package de.bund.bva.isyfact.datetime.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import de.bund.bva.isyfact.datetime.core.UngewissesDatum;

public class UngewissesDatumEntitaetTest {

    private static final LocalDate ANFANG = LocalDate.of(2017, 1, 1);
    private static final LocalDate ENDE = LocalDate.of(2017, 1, 10);
    @Test
    void toUngewissesDatum() {
        UngewissesDatumEntitaet entitaet = new UngewissesDatumEntitaet();

        entitaet.setAnfang(ANFANG);
        entitaet.setEnde(ENDE);

        UngewissesDatum ungewissesDatum = entitaet.toUngewissesDatum();

        assertThat(ungewissesDatum.getAnfang()).isEqualTo(ANFANG);
        assertThat(ungewissesDatum.getEnde()).isEqualTo(ENDE);
    }

    @Test
    void testEqualsAndHashCodeWithEqualObjects() {
        UngewissesDatumEntitaet entity1 = new UngewissesDatumEntitaet();
        entity1.setAnfang(ANFANG);
        entity1.setEnde(ENDE);

        UngewissesDatumEntitaet entity2 = new UngewissesDatumEntitaet();
        entity2.setAnfang(ANFANG);
        entity2.setEnde(ENDE);

        assertThat(entity1)
            .isNotNull()
            .isNotEqualTo("test")
            .isEqualTo(entity2);

        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    void testEqualsWithDifferentObjects() {
        LocalDate anfang2 = LocalDate.of(2017, 1, 5);
        LocalDate ende2 = LocalDate.of(2017, 1, 15);

        UngewissesDatumEntitaet entity1 = new UngewissesDatumEntitaet();
        entity1.setAnfang(ANFANG);
        entity1.setEnde(ENDE);

        UngewissesDatumEntitaet entity2 = new UngewissesDatumEntitaet();
        entity2.setAnfang(anfang2);
        entity2.setEnde(ende2);

        // Test der equals-Methode
        assertNotEquals(entity1, entity2);
    }

    @Test
    void testHashCodeWithDifferentObjects() {
        LocalDate anfang2 = LocalDate.of(2017, 1, 5);
        LocalDate ende2 = LocalDate.of(2017, 1, 15);

        UngewissesDatumEntitaet entity1 = new UngewissesDatumEntitaet();
        entity1.setAnfang(ANFANG);
        entity1.setEnde(ENDE);

        UngewissesDatumEntitaet entity2 = new UngewissesDatumEntitaet();
        entity2.setAnfang(anfang2);
        entity2.setEnde(ende2);

        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }
}