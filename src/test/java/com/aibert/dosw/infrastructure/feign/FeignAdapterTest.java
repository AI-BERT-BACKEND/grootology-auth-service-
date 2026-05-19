package com.aibert.dosw.infrastructure.feign;

import com.aibert.dosw.application.dto.request.UserAuthUpdateDTO;
import com.aibert.dosw.application.dto.response.UserAuthDTO;
import com.aibert.dosw.domain.model.user.Role;
import com.aibert.dosw.domain.model.user.User;
import com.aibert.dosw.domain.model.user.UserStatus;
import com.aibert.dosw.infrastructure.feign.config.FeignAuthInterceptor;
import feign.FeignException;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeignAdapterTest {

    @Mock private ProfileFeignClient feignClient;
    @InjectMocks private ProfileFeignAdapter adapter;

    private final UUID userId = UUID.randomUUID();

    private UserAuthDTO buildDto() {
        UserAuthDTO dto = new UserAuthDTO();
        org.springframework.test.util.ReflectionTestUtils.setField(dto, "id", userId);
        org.springframework.test.util.ReflectionTestUtils.setField(dto, "email", "test@mail.escuelaing.edu.co");
        org.springframework.test.util.ReflectionTestUtils.setField(dto, "password", "hashed");
        org.springframework.test.util.ReflectionTestUtils.setField(dto, "verified", true);
        org.springframework.test.util.ReflectionTestUtils.setField(dto, "role", Role.ESTUDIANTE);
        org.springframework.test.util.ReflectionTestUtils.setField(dto, "status", UserStatus.ACTIVO);
        org.springframework.test.util.ReflectionTestUtils.setField(dto, "profileComplete", true);
        org.springframework.test.util.ReflectionTestUtils.setField(dto, "failedAttempts", 0);
        org.springframework.test.util.ReflectionTestUtils.setField(dto, "lockedUntil", null);
        return dto;
    }

    // --- ProfileFeignAdapter ---

    @Test
    void findByEmail_existe_retornaUsuario() {
        when(feignClient.findByEmail("test@mail.escuelaing.edu.co")).thenReturn(buildDto());
        Optional<User> result = adapter.findByEmail("test@mail.escuelaing.edu.co");
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
    }

    @Test
    void findByEmail_noExiste_retornaVacio() {
        when(feignClient.findByEmail(any())).thenThrow(FeignException.NotFound.class);
        assertTrue(adapter.findByEmail("noexiste@mail.escuelaing.edu.co").isEmpty());
    }

    @Test
    void findById_existe_retornaUsuario() {
        when(feignClient.findById(userId)).thenReturn(buildDto());
        Optional<User> result = adapter.findById(userId);
        assertTrue(result.isPresent());
        assertEquals("test@mail.escuelaing.edu.co", result.get().getEmail());
    }

    @Test
    void findById_noExiste_retornaVacio() {
        when(feignClient.findById(any())).thenThrow(FeignException.NotFound.class);
        assertTrue(adapter.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void save_llamaUpdateAuthFields() {
        User user = User.builder()
                .id(userId)
                .email("test@mail.escuelaing.edu.co")
                .password("hashed")
                .verified(true)
                .role(Role.ESTUDIANTE)
                .failedAttempts(1)
                .lockedUntil(null)
                .build();
        doNothing().when(feignClient).updateAuthFields(any(), any());
        adapter.save(user);
        verify(feignClient).updateAuthFields(eq(userId), any(UserAuthUpdateDTO.class));
    }

    // --- FeignAuthInterceptor ---

    @Test
    void feignAuthInterceptor_conToken_agregaHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token123");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        FeignAuthInterceptor interceptor = new FeignAuthInterceptor();
        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertTrue(template.headers().containsKey("Authorization"));
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void feignAuthInterceptor_sinToken_noAgregaHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        FeignAuthInterceptor interceptor = new FeignAuthInterceptor();
        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertFalse(template.headers().containsKey("Authorization"));
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void feignAuthInterceptor_sinRequestContext_noFalla() {
        RequestContextHolder.resetRequestAttributes();
        FeignAuthInterceptor interceptor = new FeignAuthInterceptor();
        RequestTemplate template = new RequestTemplate();
        assertDoesNotThrow(() -> interceptor.apply(template));
    }

    // --- UserAuthUpdateDTO ---

    @Test
    void userAuthUpdateDTO_constructor() {
        LocalDateTime locked = LocalDateTime.now().plusMinutes(15);
        UserAuthUpdateDTO dto = new UserAuthUpdateDTO(3, locked);
        assertEquals(3, dto.getFailedAttempts());
        assertEquals(locked, dto.getLockedUntil());
    }
}
