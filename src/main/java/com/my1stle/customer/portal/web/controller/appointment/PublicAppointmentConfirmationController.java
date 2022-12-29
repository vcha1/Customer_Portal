package com.my1stle.customer.portal.web.controller.appointment;

import com.my1stle.customer.portal.service.appointment.AppointmentService;
import com.my1stle.customer.portal.service.appointment.AppointmentStatus;
import com.my1stle.customer.portal.service.model.Appointment;
import com.my1stle.customer.portal.web.controller.PublicController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Controller
public class PublicAppointmentConfirmationController extends PublicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicAppointmentConfirmationController.class);

    private AppointmentService appointmentService;

    @Autowired
    public PublicAppointmentConfirmationController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping(value = "/appointment/{event_id}/confirm")
    public String confirmAppointment(
            @PathVariable("event_id") Long eventId,
            @RequestParam(name = "token", required = true) String token,
            Model model, RedirectAttributes redirectAttributes) {

        Appointment appointment = appointmentService.get(eventId);

        model.addAttribute("appointment", appointment);

        if (Objects.equals(appointment.getStatus(), AppointmentStatus.CONFIRMED)) {
            LOGGER.info("Appointment [Id : {}] has already been confirmed. ", eventId);
            formatAppointmentTimes(model, appointment);
            return "appointment/post-confirmation";
        } else if (Objects.equals(appointment.getStatus(), AppointmentStatus.REJECTED)) {
            redirectAttributes.addAttribute("token", token);
            return String.format("redirect:/public/appointment/%s/reject", appointment.getEventDetail().getId());
        }

        Appointment appointmentAfterConfirmation = this.appointmentService.confirm(appointment.getEventDetail().getId(), token);
        model.addAttribute("appointment", appointmentAfterConfirmation);
        formatAppointmentTimes(model, appointmentAfterConfirmation);

        return "appointment/post-confirmation";

    }


    @GetMapping(value = "/appointment/{event_id}/reject")
    public String rejectAppointment(
            @PathVariable("event_id") Long eventId,
            @RequestParam(name = "token", required = true) String token,
            Model model, RedirectAttributes redirectAttributes) {

        Appointment appointment = appointmentService.get(eventId);

        model.addAttribute("appointment", appointment);

        if (Objects.equals(appointment.getStatus(), AppointmentStatus.REJECTED)) {
            LOGGER.info("Appointment [Id : {}] has already been rejected.", eventId);
            return "appointment/post-rejection";
        } else if (Objects.equals(appointment.getStatus(), AppointmentStatus.CONFIRMED)) {
            redirectAttributes.addAttribute("token", token);
            return String.format("redirect:/public/appointment/%s/confirm", appointment.getEventDetail().getId());
        }

        Appointment appointmentAfterRejection = this.appointmentService.reject(appointment.getEventDetail().getId(), token);
        model.addAttribute("appointment", appointmentAfterRejection);

        return "appointment/post-rejection";

    }

    private static void formatAppointmentTimes(Model model, Appointment appointmentAfterConfirmation) {
        model.addAttribute("appointment_date", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(appointmentAfterConfirmation.getEventDetail().getStartDateTime()));
        model.addAttribute("time_frame_start", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(appointmentAfterConfirmation.getEventDetail().getStartDateTime().minusHours(1L)));
        model.addAttribute("time_frame_end", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(appointmentAfterConfirmation.getEventDetail().getStartDateTime().plusHours(1L)));
    }

}