package com.my1stle.customer.portal.service.installation.detail;

import com.my1stle.customer.portal.service.installation.detail.model.Timeline;
import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;

@Service
public class InstallationTimelineService {
    private static final String TIMELINE_CONFIGURATION_LOCATION = "customer/installation/status_timeline.json";

    public Timeline getTimeline() {

        Gson gson = new Gson();

        InputStream stream = InstallationTimelineService.class.getClassLoader().getResourceAsStream(TIMELINE_CONFIGURATION_LOCATION);

        if (null == stream)
            throw new RuntimeException(String.format("%s not found!", TIMELINE_CONFIGURATION_LOCATION));

        InputStreamReader reader = new InputStreamReader(stream);

        return gson.fromJson(reader, Timeline.class);
    }


}
