package com.vidbox.file

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.ServletUnitTestingSupport
import com.vidbox.VidboxApplication
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.MockBeans
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [FileContentController::class])
@ContextConfiguration(classes = [VidboxApplication::class])
@MockBeans(MockBean(FileService::class))
class FileContentControllerTests: ServletUnitTestingSupport() {

    @Test
    @Order(1)
    @WithMockKeycloakAuth(
        authorities = ["ROLE_user"],
        claims = OpenIdClaims(
            sub = "1",
            nickName = "test",
            preferredUsername = "test_user"
        )
    )
    fun `assert that files are retrieved`() {
        mockMvc().get("/files")
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}
