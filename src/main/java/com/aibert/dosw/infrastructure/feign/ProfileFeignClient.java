package com.aibert.dosw.infrastructure.feign;

import com.aibert.dosw.application.dto.request.UserAuthUpdateDTO;
import com.aibert.dosw.application.dto.response.UserAuthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "profile-service", url = "${feign.profile-service.url}")
public interface ProfileFeignClient {

    @GetMapping("/internal/users/email/{email}")
    UserAuthDTO findByEmail(@PathVariable("email") String email);

    @GetMapping("/internal/users/{id}")
    UserAuthDTO findById(@PathVariable("id") UUID id);

    @PutMapping("/internal/users/{id}/auth")
    void updateAuthFields(@PathVariable("id") UUID id, @RequestBody UserAuthUpdateDTO dto);
}
