package org.apereo.cas.webauthn.web.flow;

import org.apereo.cas.authentication.MultifactorAuthenticationProvider;
import org.apereo.cas.services.RegisteredServiceTestUtils;
import org.apereo.cas.util.spring.ApplicationContextProvider;
import org.apereo.cas.web.support.WebUtils;

import lombok.val;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.RequestContextHolder;
import org.springframework.webflow.test.MockRequestContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link WebAuthnStartRegistrationActionTests}.
 *
 * @author Misagh Moayyed
 * @since 6.3.0
 */
@Tag("WebflowMfaActions")
@SpringBootTest(classes = BaseWebAuthnWebflowTests.SharedTestConfiguration.class)
@DirtiesContext
public class WebAuthnStartRegistrationActionTests {
    @Autowired
    @Qualifier("webAuthnStartRegistrationAction")
    private Action webAuthnStartRegistrationAction;

    @Autowired
    @Qualifier("webAuthnMultifactorAuthenticationProvider")
    private MultifactorAuthenticationProvider webAuthnMultifactorAuthenticationProvider;

    @Autowired
    private ConfigurableApplicationContext applicationContext;
    
    @Test
    public void verifyOperation() throws Exception {
        ApplicationContextProvider.holdApplicationContext(applicationContext);
        ApplicationContextProvider.registerBeanIntoApplicationContext(applicationContext,
            this.webAuthnMultifactorAuthenticationProvider, "webAuthnMultifactorAuthenticationProvider");

        val context = new MockRequestContext();
        val request = new MockHttpServletRequest();
        val response = new MockHttpServletResponse();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, response));
        WebUtils.putMultifactorAuthenticationProviderIdIntoFlowScope(context, webAuthnMultifactorAuthenticationProvider);
        RequestContextHolder.setRequestContext(context);
        ExternalContextHolder.setExternalContext(context.getExternalContext());

        WebUtils.putAuthentication(RegisteredServiceTestUtils.getAuthentication(), context);
        assertNotNull(webAuthnStartRegistrationAction);
        assertNull(webAuthnStartRegistrationAction.execute(context));
        assertTrue(context.getFlowScope().contains(WebAuthnStartRegistrationAction.FLOW_SCOPE_WEB_AUTHN_APPLICATION_ID));
        assertTrue(context.getFlowScope().contains("displayName"));
    }

}
