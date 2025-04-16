package de.bund.bva.isyfact.datetime.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import de.bund.bva.isyfact.datetime.core.UngewisseZeit;

public class UngewisseZeitEntitaetTest {

    private static final LocalTime ANFANG = LocalTime.of(12, 0);
    private static final LocalTime ENDE = LocalTime.of(18, 30);

    @Test
    void toUngewisseZeit() {
        UngewisseZeitEntitaet entitaet = new UngewisseZeitEntitaet();
        entitaet.setAnfang(ANFANG);
        entitaet.setEnde(ENDE);

        UngewisseZeit ungewisseZeit = entitaet.toUngewisseZeit();

        assertThat(ungewisseZeit.getAnfang()).isEqualTo(ANFANG);
        assertThat(ungewisseZeit.getEnde()).isEqualTo(ENDE);
    }

    @Test
    void testEqualsAndHashCodeWithEqualObjects() {
        UngewisseZeitEntitaet entity1 = new UngewisseZeitEntitaet();
        entity1.setAnfang(ANFANG);
        entity1.setEnde(ENDE);

        UngewisseZeitEntitaet entity2 = new UngewisseZeitEntitaet();
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
        LocalTime anfang1 = LocalTime.of(12, 0);
        LocalTime ende1 = LocalTime.of(15, 0);

        LocalTime anfang2 = LocalTime.of(13, 0);
        LocalTime ende2 = LocalTime.of(16, 0);

        UngewisseZeitEntitaet entity1 = new UngewisseZeitEntitaet();
        entity1.setAnfang(anfang1);
        entity1.setEnde(ende1);

        UngewisseZeitEntitaet entity2 = new UngewisseZeitEntitaet();
        entity2.setAnfang(anfang2);
        entity2.setEnde(ende2);

        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    void testHashCodeWithDifferentObjects() {
        LocalTime anfang2 = LocalTime.of(13, 0);
        LocalTime ende2 = LocalTime.of(16, 0);

        UngewisseZeitEntitaet entity1 = new UngewisseZeitEntitaet();
        entity1.setAnfang(ANFANG);
        entity1.setEnde(ENDE);

        UngewisseZeitEntitaet entity2 = new UngewisseZeitEntitaet();
        entity2.setAnfang(anfang2);
        entity2.setEnde(ende2);

        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }
}