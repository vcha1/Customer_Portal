package com.my1stle.customer.portal.service.serviceapi;

import java.util.*;

public interface ServiceScheduleRequestApi {

    Optional<ScheduleRequestDto> get(String token);
}
