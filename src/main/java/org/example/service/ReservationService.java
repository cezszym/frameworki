//package org.example.service;
//
//import org.example.entity.Reservation;
//import org.example.other.ReservationStatus;
//import org.example.repository.ReservationRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//
//@Service
//public class ReservationService {
//    private ReservationRepository reservationRepository;
//
//    public ReservationService(ReservationRepository reservationRepository){
//        this.reservationRepository = reservationRepository;
//    }
//
//    public List<Reservation> findAll(){
//        return this.reservationRepository.findAll();
//    }
//
//    public Optional<Reservation> findReservationById(UUID reservationId){
//        return this.reservationRepository.findById(reservationId);
//    }
//
//    public List<Reservation> findByStatus(ReservationStatus reservationStatus){
//        return this.reservationRepository.findByStatus(reservationStatus);
//    }
//
//    public Reservation save(Reservation reservation){
//        // send email to user and house owner
//        return this.reservationRepository.save(reservation);
//    }
//
//    public void deleteById(UUID id){
//        this.reservationRepository.deleteById(id);
//        // send email to user and house owner
//    }
//
//    public void updateById(UUID id, Reservation reservation){
//        Optional<Reservation> toBeUpdated = this.reservationRepository.findById()
//
//
//        Reservation toBeUpdated = this.reservationRepository.findById(id);
//        reservation.setId(toBeUpdated.getId());
////        this.reservationRepository.deleteById(id);
//        this.reservationRepository.updateById(id, reservation);
//    }
//}
