package com.dke.data.agrirouter.impl.onboard;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingRequest;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.onboard.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.AuthenticationUrlParameters;
import com.dke.data.agrirouter.api.service.parameters.OnboardingParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;

/** Internal service implementation. */
public class OnboardingServiceImpl extends AbstractOnboardingService
    implements OnboardingService, ResponseValidator {

  public OnboardingServiceImpl(Environment environment) {
    super(environment);
  }

  @Override
  public OnboardingResponse onboard(OnboardingParameters parameters) {
    this.getNativeLogger().info("BEGIN | Onboarding process. | '{}'.", parameters);
    parameters.validate();

    this.getNativeLogger().debug("Onboard device.");
    OnboardingResponse onboardingResponse =
        this.onboard(
            parameters.getRegistrationCode(), this.createOnboardingRequestBody(parameters));

    this.getNativeLogger().info("END | Onboarding process. | '{}'.", parameters);
    return onboardingResponse;
  }

  private OnboardingRequest createOnboardingRequestBody(OnboardingParameters parameters) {
    this.getNativeLogger().info("BEGIN | Create onboarding request. | '{}'.", parameters);
    OnboardingRequest onboardRequest =
        this.getOnboardRequest(
            parameters.getUuid(),
            parameters.getApplicationId(),
            parameters.getCertificationVersionId(),
            parameters.getGatewayId(),
            parameters.getCertificationType());
    this.getNativeLogger().info("END | Create onboarding request. | '{}'.", parameters);
    return onboardRequest;
  }

  private OnboardingResponse onboard(String registrationCode, OnboardingRequest onboardingRequest) {
    this.getNativeLogger()
        .info("BEGIN | Onboarding process. | '{}', '{}'.", registrationCode, onboardingRequest);
    Response response =
        RequestFactory.bearerTokenRequest(this.environment.getOnboardUrl(), registrationCode)
            .post(Entity.json(onboardingRequest));
    this.assertResponseStatusIsValid(response, HttpStatus.SC_CREATED);
    OnboardingResponse onboardingResponse = response.readEntity(OnboardingResponse.class);

    this.getNativeLogger()
        .info("END | Onboarding process. | '{}', '{}'.", registrationCode, onboardingRequest);
    return onboardingResponse;
  }

  @Override
  public String generateAuthenticationUrl(AuthenticationUrlParameters parameters) {
    this.getNativeLogger().info("BEGIN | Generating authentication URL. | '{}'.", parameters);
    String securedOnboardingAuthenticationUrl =
        this.environment.getSecuredOnboardingAuthenticationUrl(
            parameters.getApplicationId(),
            parameters.getResponseType(),
            parameters.getState(),
            parameters.getRedirectUri());
    this.getNativeLogger().info("END | Generating authentication URL. | '{}'.", parameters);
    return securedOnboardingAuthenticationUrl;
  }
}
