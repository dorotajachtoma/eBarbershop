package pl.barbershop.controller;


import org.hibernate.Hibernate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.barbershop.model.*;
import pl.barbershop.repository.*;
import pl.barbershop.service.DateServiceImpl;
import pl.barbershop.service.SlotServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/user/login/panel/barbershop/reservation")
public class ReservationController {


    private final ServiceRepository serviceRepository;
    private final BarbershopRepository barbershopRepository;
    private final DateRepository dateRepository;
    private final SlotRepository slotRepository;
    private final DateServiceImpl dateService;
    private final SlotServiceImpl slotService;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;


    public ReservationController(ServiceRepository serviceRepository, BarbershopRepository barbershopRepository, DateRepository dateRepository, SlotRepository slotRepository, DateServiceImpl dateService, SlotServiceImpl slotService, UserRepository userRepository, ReservationRepository reservationRepository) {
        this.serviceRepository = serviceRepository;
        this.barbershopRepository = barbershopRepository;
        this.dateRepository = dateRepository;
        this.slotRepository = slotRepository;
        this.dateService = dateService;
        this.slotService = slotService;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/{barbershop_id}")
    public String chooseService(Model model, @PathVariable("barbershop_id") Long barbershopId) {
        model.addAttribute("barbershopId", barbershopId);
        model.addAttribute("barbershop", barbershopRepository.getOne(barbershopId));
        model.addAttribute("service", serviceRepository.findServicesByBarbershopsId(barbershopId));
        return "barbershop-details";
    }

    @GetMapping("/{barbershop_id}/{service_id}")
    public String chooseDateGet(Model model, @PathVariable("barbershop_id") Long barbershopId, @PathVariable("service_id") Long serviceId) {
        return "calendar";
    }

    @PostMapping("/{barbershop_id}/{service_id}")
    public String chooseSlot(Model model, @PathVariable("barbershop_id") Long barbershopId, @PathVariable("service_id") Long serviceId, @RequestParam("date") String date) {
        String parsed = date.replace("%2F", "/");
        Optional<Barbershop> optionalBarbershop = barbershopRepository.findById(barbershopId);
        Optional<Service> optionalService = serviceRepository.findById(serviceId);
        String open = optionalBarbershop.get().getOpen();
        String close = optionalBarbershop.get().getClose();
        String duration = optionalService.get().getTime();
        List<Slot> slots = new ArrayList<>();
        if (slotRepository.findAll().isEmpty()) {
            for (Slot slot : slotService.setSlot(open, close, duration)) {
                slotRepository.save(slot);
                slots.add(slot);
            }
        }
        model.addAttribute("barbershopId", barbershopId);
        model.addAttribute("serviceId", serviceId);
        model.addAttribute("dateId", dateRepository.checkDate(parsed).getId());
        model.addAttribute("slots", slots);
        return "slots-list";

    }

    @GetMapping("/{barbershop_id}/{service_id}/{dateId}/{slotId}")
    public String chooseSlot(Model model,@PathVariable("barbershop_id") Long barbershopId, @PathVariable("service_id") Long serviceId, @PathVariable("dateId") Long dateId, @PathVariable("slotId") Long slotId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            User user = userRepository.findByEmail(username);
            Reservation reservation = new Reservation();
            Optional<Barbershop> optionalBarbershop = barbershopRepository.findById(barbershopId);
            Optional<Service> optionalService = serviceRepository.findById(serviceId);
            Optional<Date> optionalDate = dateRepository.findById(dateId);
            Optional<Slot> optionalSlot = slotRepository.findById(slotId);
            reservation.setSlot(optionalSlot.get());
            reservation.setBarbershop(optionalBarbershop.get());
            reservation.setService(optionalService.get());
            reservation.setDate(optionalDate.get());
            reservation.setUser(user);
            reservationRepository.save(reservation);
            List<Reservation> reservations = new ArrayList<>();
            reservations.add(reservation);
            optionalBarbershop.get().setReservation(reservations);
            model.addAttribute("reservation",reservation);
            return "reservation-list";
        }
        return "redirect:/login";

    }
}