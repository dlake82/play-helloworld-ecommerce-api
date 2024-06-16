//package com.saysimple.supports.service;
//
//import com.saysimple.supports.entity.Support;
//import com.saysimple.supports.enums.ContactEnumType;
//import com.saysimple.supports.enums.ProductEnumType;
//import com.saysimple.supports.repository.SupportRepository;
//import com.saysimple.supports.vo.RequestSupport;
//import com.saysimple.supports.vo.RequestUpdateSupport;
//import com.saysimple.supports.vo.ResponseSupport;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class SupportServiceTest {
//
//    @Mock
//    private SupportRepository repository;
//
//    @InjectMocks
//    private SupportServiceImpl service;
//
//    @Test
//    void testCreateSupport() {
//        // Given
//        RequestSupport request = new RequestSupport();
//        request.setSupportId("SUP001");
//        request.setProductId("PROD001");
//        request.setCategoryId("CAT001");
//        request.setUserId("USER001");
//        request.setTitle("Test Inquiry");
//        request.setContent("This is a test inquiry.");
//        request.setContactChoice("EMAIL");
//        request.setReturnChoice("No");
//        request.setProductImages("IMAGE1");
//
//        Support support = new Support();
//        support.setSupportId("SUP001");
//        support.setProductId("PROD001");
//        support.setCategoryId("CAT001");
//        support.setUserId("USER001");
//        support.setTitle("Test Inquiry");
//        support.setContent("This is a test inquiry.");
//        support.setContactChoice(ContactEnumType.OrderPoint);
//        support.setReturnChoice("No");
//        support.setProductImages(ProductEnumType.NoAny);
//        when(repository.save(any(Support.class))).thenReturn(support);
//
//        // When
//        ResponseSupport createdSupport = service.create(request);
//
//        // Then
//        assertThat(createdSupport.getSupportId()).isEqualTo("SUP001");
//        verify(repository, times(1)).save(any(Support.class));
//    }
//
//    @Test
//    void testGetSupport() {
//        // Given
//        Support support = new Support();
//        support.setSupportId("SUP001");
//        support.setProductId("PROD001");
//        support.setCategoryId("CAT001");
//        support.setUserId("USER001");
//        support.setTitle("Test Inquiry");
//        support.setContent("This is a test inquiry.");
//        support.setContactChoice(ContactEnumType.OrderPoint);
//        support.setReturnChoice("No");
//        support.setProductImages(ProductEnumType.NoAny);
//        when(repository.findBySupportId("SUP001")).thenReturn(Optional.of(support));
//
//        // When
//        ResponseSupport foundSupport = service.get("SUP001");
//
//        // Then
//        assertThat(foundSupport.getSupportId()).isEqualTo("SUP001");
//        verify(repository, times(1)).findBySupportId("SUP001");
//    }
//
//    @Test
//    void testUpdateSupport() {
//        // Given
//        RequestUpdateSupport request = new RequestUpdateSupport();
//        request.setSupportId("SUP001");
//        request.setProductId("PROD001");
//        request.setCategoryId("CAT001");
//        request.setUserId("USER001");
//        request.setTitle("Updated Inquiry");
//        request.setContent("Updated content.");
//        request.setContactChoice("EMAIL");
//        request.setReturnChoice("Yes");
//        request.setProductImages("IMAGE2");
//
//        Support support = new Support();
//        support.setSupportId("SUP001");
//        support.setProductId("PROD001");
//        support.setCategoryId("CAT001");
//        support.setUserId("USER001");
//        support.setTitle("Test Inquiry");
//        support.setContent("This is a test inquiry.");
//        support.setContactChoice(ContactEnumType.Delivery);
//        support.setReturnChoice("No");
//        support.setProductImages(ProductEnumType.Message);
//        when(repository.findBySupportId("SUP001")).thenReturn(Optional.of(support));
//        when(repository.save(any(Support.class))).thenReturn(support);
//
//        // When
//        ResponseSupport updatedSupport = service.update(request);
//
//        // Then
//        assertThat(updatedSupport.getTitle()).isEqualTo("Updated Inquiry");
//        assertThat(updatedSupport.getContent()).isEqualTo("Updated content.");
//        verify(repository, times(1)).findBySupportId("SUP001");
//        verify(repository, times(1)).save(any(Support.class));
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
//        support.setContactChoice(ContactEnumType.Delivery);
//        support.setReturnChoice("No");
//        support.setProductImages(ProductEnumType.Message);
//        when(repository.findBySupportId("SUP001")).thenReturn(Optional.of(support));
//
//        // When
//        service.delete("SUP001");
//
//        // Then
//        verify(repository, times(1)).findBySupportId("SUP001");
//        verify(repository, times(1)).delete(support);
//    }
//}
