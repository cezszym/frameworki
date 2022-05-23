package org.example.controller;

import org.example.entity.Flat;
import org.example.entity.FlatDetail;
import org.example.entity.User;
import org.example.model.FlatDTO;
import org.example.model.FlatDetailDTO;
import org.example.repository.FlatDetailRepository;
import org.example.repository.FlatRepository;
import org.example.repository.UserRepository;
import org.example.request.wrappers.FlatWrapper;
import org.example.security.Identity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RequestMapping("/api/flats")
@RestController
public class FlatController {
    private final FlatRepository flatRepository;
    private final FlatDetailRepository flatDetailRepository;
    private final UserRepository userRepository;
    private final Identity identity;

    public FlatController(final FlatRepository flatRepository, FlatDetailRepository flatDetailRepository, final UserRepository userRepository, Identity identity){
        this.flatRepository = flatRepository;
        this.flatDetailRepository = flatDetailRepository;
        this.userRepository = userRepository;
        this.identity = identity;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllFlats(){
        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            ArrayList<Flat> flats = new ArrayList<>(this.flatRepository.getAllByUser(user));
            if (flats.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(flats, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{flatId}")
    public ResponseEntity<?> getFlatById(@PathVariable("flatId") UUID id){
        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Flat flat = this.flatRepository.getByUserAndId(user, id);
            if (flat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(flat, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> count(){
        try{
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(this.flatRepository.count(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/details/{flatId}")
    public ResponseEntity<?> getDetails(@PathVariable("flatId") UUID id){
        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Flat flat = this.flatRepository.getByUserAndId(user, id);
            if(flat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            FlatDetail flatDetail = this.flatDetailRepository.getById(flat.getFlatDetail().getId());
            return new ResponseEntity<>(flatDetail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Flat> save(@RequestBody FlatWrapper flatWrapper){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        FlatDTO flatDTO = flatWrapper.getFlatDTO();
        FlatDetailDTO flatDetailDTO = flatWrapper.getFlatDetailDTO();

        try{
            FlatDetail flatDetail = this.flatDetailRepository.save(FlatDetail.builder().
                    id(flatDetailDTO.getId()).
                    kitchen(flatDetailDTO.getKitchen()).
                    bathroom(flatDetailDTO.getBathroom()).
                    towels(flatDetailDTO.getTowels()).
                    wifi(flatDetailDTO.getWifi()).build());

            Flat flat = this.flatRepository.save(Flat.builder().
                    user(user).flatDetail(flatDetail).
                    adress(flatDTO.getAdress()).
                    postCode(flatDTO.getPostCode()).
                    city(flatDTO.getCity()).
                    country(flatDTO.getCountry()).
                    metrage(flatDTO.getMetrage()).
                    numOfRooms(flatDTO.getNumOfRooms()).build());

            return new ResponseEntity<>(flat, HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{flatId}")
    public ResponseEntity<Flat> update(@PathVariable("flatId") UUID id, @RequestBody FlatDTO flatDTO, @RequestBody FlatDetailDTO flatDetailDTO){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try{
            // Delete existing flat details and flat
            deleteById(id);

            // Add new ones
            FlatDetail flatDetail = this.flatDetailRepository.save(FlatDetail.builder().
                    id(flatDetailDTO.getId()).
                    kitchen(flatDetailDTO.getKitchen()).
                    bathroom(flatDetailDTO.getBathroom()).
                    towels(flatDetailDTO.getTowels()).
                    wifi(flatDetailDTO.getWifi()).build());

            Flat flat = this.flatRepository.save(Flat.builder().
                    user(user).flatDetail(flatDetail).
                    adress(flatDTO.getAdress()).
                    postCode(flatDTO.getPostCode()).
                    city(flatDTO.getCity()).
                    country(flatDTO.getCountry()).
                    metrage(flatDTO.getMetrage()).
                    numOfRooms(flatDTO.getNumOfRooms()).build());

            return new ResponseEntity<>(flat, HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{flatId}")
    public ResponseEntity<?> deleteById(@PathVariable("flatId") UUID flatId){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        // Check if user has permissions to delete this flat
        Flat flat = this.flatRepository.findByUserAndId(user, flatId);
        if (flat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            // Get flatdetails object id to be deleted
            UUID flatDetailId = flat.getFlatDetail().getId();

            // Delete flatdetails object
            this.flatDetailRepository.deleteById(flatDetailId);
            // Delete flat object
            this.flatRepository.deleteById(flat.getId());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
