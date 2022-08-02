package org.example.event.impl;

import lombok.AllArgsConstructor;
import org.example.dto.PasswordResetEventRequest;
import org.example.event.CustomAdminEvent;
import org.example.utils.InternalEventSender;
import org.example.utils.PropertiesUtils;
import org.keycloak.events.admin.AdminEvent;

@AllArgsConstructor
public class PasswordResetEvent implements CustomAdminEvent {

    private static final String ACTION_OPERATION_TYPE = "ACTION";
    private static final String USERS_RESOURCE_PATH = "users";
    private static final String RESET_PASSWORD_RESOURCE_PATH = "reset-password";
    private static final String URL = PropertiesUtils.readProperty("user-service.password-reset-event.uri");

    private final AdminEvent event;

    @Override
    public void process() {
        String[] resourcePath = event.getResourcePath().split("/");
        new InternalEventSender<PasswordResetEventRequest>()
                .send(new PasswordResetEventRequest(resourcePath[1]), URL);
    }

    @Override
    public boolean isValid() {
        return isPasswordResetEvent(event);
    }

    private boolean isPasswordResetEvent(AdminEvent event) {
        String[] resourcePath = event.getResourcePath().split("/");
        return ACTION_OPERATION_TYPE.equals(event.getOperationType().toString()) &&
                USERS_RESOURCE_PATH.equals(resourcePath[0]) &&
                RESET_PASSWORD_RESOURCE_PATH.equals(resourcePath[2]);
    }
}
