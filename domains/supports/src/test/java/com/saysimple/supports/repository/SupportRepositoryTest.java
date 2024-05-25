//package com.saysimple.supports.repository;
//
//import com.saysimple.supports.entity.Support;
//import com.saysimple.supports.enums.ContactEnumType;
//import com.saysimple.supports.enums.ProductEnumType;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//public class SupportRepositoryTest {
//
//    @Autowired
//    private SupportRepository repository;
//
//    @Test
//    void testCreateAndFindSupport() {
//        // Given
//        Support support = new Support();
//        support.setSupportId("SUP001");
//        support.setProductId("PROD001");
//        support.setCategoryId("CAT001");
//        support.setUserId("USER001");
//        support.setTitle("Test Inquiry");
//        support.setContent("This is a test inquiry.");
//        support.setContactChoice(ContactEnumType.Exchange);
//        support.setReturnChoice("No");
//        support.setProductImages(ProductEnumType.Recording);
//
//        // When
//        Support savedSupport = repository.save(support);
//
//        // Then
//        Optional<Support> foundSupport = repository.findBySupportId(savedSupport.getSupportId());
//        assertThat(foundSupport).isPresent();
//        assertThat(foundSupport.get().getSupportId()).isEqualTo("SUP001");
//        assertThat(foundSupport.get().getTitle()).isEqualTo("Test Inquiry");
//    }
//
//    @Test
//    void testUpdateSupport() {
//        // Given
//        Support support = new Support();
//        support.setSupportId("SUP001");
//        support.setProductId("PROD001");
//        support.setCategoryId("CAT001");
//        support.setUserId("USER001");
//        support.setTitle("Test Inquiry");
//        support.setContent("This is a test inquiry.");
//        support.setContactChoice(ContactEnumType.Cancel);
//        support.setReturnChoice("No");
//        support.setProductImages(ProductEnumType.NoAny);
//        Support savedSupport = repository.save(support);
//
//        // When
//        savedSupport.setContent("Updated content.");
//        Support updatedSupport = repository.save(savedSupport);
//
//        // Then
//        Optional<Support> foundSupport = repository.findBySupportId(updatedSupport.getSupportId());
//        assertThat(foundSupport).isPresent();
//        assertThat(foundSupport.get().getContent()).isEqualTo("Updated content.");
//    }
//
//    @Test
//    void testDeleteSupport() {
//        // Given
//        Support support = new Support();
//        support.setSupportId("SUP001");
//        support.setProductId("PROD001");
//        support.setCategoryId("CAT001");
//        support.setUserId("USER001");
//        support.setTitle("Test Inquiry");
//        support.setContent("This is a test inquiry.");
//        support.setContactChoice(ContactEnumType.Cancel);
//        support.setReturnChoice("No");
//        support.setProductImages(ProductEnumType.NoAny);
//        Support savedSupport = repository.save(support);
//
//        // When
//        repository.deleteById(savedSupport.getId());
//
//        // Then
//        Optional<Support> foundSupport = repository.findBySupportId(savedSupport.getSupportId());
//        assertThat(foundSupport).isNotPresent();
//    }
//}
