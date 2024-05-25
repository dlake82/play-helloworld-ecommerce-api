//package com.saysimple.supports.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.saysimple.supports.entity.Support;
//import com.saysimple.supports.enums.ContactEnumType;
//import com.saysimple.supports.enums.ProductEnumType;
//import com.saysimple.supports.repository.SupportRepository;
//import com.saysimple.supports.vo.RequestSupport;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Optional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class SupportControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private SupportRepository repository;
//
//    @BeforeEach
//    void setUp() {
//        repository.deleteAll();
//    }
//
//    @Test
//    void testCreateSupport() throws Exception {
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
//        // When
//        mockMvc.perform(post("/supports")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.supportId").value("SUP001"))
//                .andExpect(jsonPath("$.title").value("Test Inquiry"));
//    }
//
//    @Test
//    void testGetSupport() throws Exception {
//        // Given
//        Support support = new Support();
//        support.setSupportId("SUP001");
//        support.setProductId("PROD001");
//        support.setCategoryId("CAT001");
//        support.setUserId("USER001");
//        support.setTitle("Test Inquiry");
//        support.setContent("This is a test inquiry.");
//        support.setContactChoice(ContactEnumType.EMAIL);
//        support.setReturnChoice("No");
//        support.setProductImages(ProductEnumType.IMAGE1);
//        repository.save(support);
//
//        // When
//        mockMvc.perform(get("/supports/SUP001"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.supportId").value("SUP001"))
//                .andExpect(jsonPath("$.title").value("Test Inquiry"));
//    }
//
//    @Test
//    void testUpdateSupport() throws Exception {
//        // Given
//        Support support = new Support();
//        support.setSupportId("SUP001");
//        support.setProductId("PROD001");
//        support.setCategoryId("CAT001");
//        support.setUserId("USER001");
//        support.setTitle("Test Inquiry");
//        support.setContent("This is a test inquiry.");
//        support.setContactChoice(ContactEnumType.EMAIL);
//        support.setReturnChoice("No");
//        support.setProductImages(ProductEnumType.IMAGE1);
//        repository.save(support);
//
//        RequestSupport request = new RequestSupport();
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
//        // When
//        mockMvc.perform(put("/supports")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.supportId").value("SUP001"))
//                .andExpect(jsonPath("$.title").value("Updated Inquiry"));
//    }
//
//    @Test
//    void testDeleteSupport() throws Exception {
//        // Given
//        Support support = new Support();
//        support.setSupportId("SUP001");
//        support.setProductId("PROD001");
//        support.setCategoryId("CAT001");
//        support.setUserId("USER001");
//        support.setTitle("Test Inquiry");
//        support.setContent("This is a test inquiry.");
//        support.setContactChoice(ContactEnumType.EMAIL);
//        support.setReturnChoice("No");
//        support.setProductImages(ProductEnumType.IMAGE1);
//        repository.save(support);
//
//        // When
//        mockMvc.perform(delete("/supports/SUP001"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value(true));
//
//        // Then
//        Optional<Support> foundSupport = repository.findBySupportId("SUP001");
//        assertThat(foundSupport).isNotPresent();
//    }
//}
