package org.example;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.CustomAdminEvent;
import org.example.event.impl.PasswordResetEvent;
import org.example.utils.EventUtils;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@NoArgsConstructor
public class CustomEventListenerProvider implements EventListenerProvider {

    @Override
    public void onEvent(Event event) {
        log.info("Caught event {}", EventUtils.toString(event));
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        log.info("Caught admin event {}", EventUtils.toString(adminEvent));

//        //создаём список всех возможных событий
        List<CustomAdminEvent> events = new LinkedList<>();
        events.add(new PasswordResetEvent(adminEvent));

//        //узнаём что за событие и выполняем необходимые действия
        events.stream().filter(CustomAdminEvent::isValid).forEach(CustomAdminEvent::process);
    }

    @Override
    public void close() {

    }
}