package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.entity.Flat;
import org.example.entity.FlatDetail;
import org.example.entity.User;
import org.example.model.FlatDTO;
import org.example.model.FlatDetailDTO;
import org.example.repository.FlatDetailRepository;
import org.example.repository.FlatRepository;
import org.example.request.wrappers.FlatWrapper;
import org.example.security.Identity;
import org.example.service.BrowserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping("/api/flats")
@RestController
public class FlatController {
    private final FlatRepository flatRepository;
    private final FlatDetailRepository flatDetailRepository;
    private final BrowserService browserService;
    private final Identity identity;

    public FlatController(final FlatRepository flatRepository, FlatDetailRepository flatDetailRepository, BrowserService browserService, Identity identity){
        this.flatRepository = flatRepository;
        this.flatDetailRepository = flatDetailRepository;
        this.browserService = browserService;
        this.identity = identity;
    }

    @Operation(summary = "Get all flats")
    @GetMapping("/")
    public ResponseEntity<CollectionModel<?>> getAll(){

        Link link = linkTo(FlatController.class).withSelfRel();

        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            ArrayList<Flat> flats = new ArrayList<>(this.flatRepository.getAllByUser(user));
            if (flats.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            flats.forEach(flat -> flat.add(linkTo(FlatController.class).slash(flat.getId()).withSelfRel()));
            flats.forEach(flat -> flat.add(linkTo(FlatController.class).slash("details").slash(flat.getId()).withRel("flatDetails")));
            return ResponseEntity.ok(CollectionModel.of(flats,link));
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get flat by id")
    @GetMapping("/{flatId}")
    public ResponseEntity<?> getById(
            @Parameter(
                    description = "unique id of flat",
                    example = "b8d02d81-6329-ef96-8a4d-55b376d8b25a")
            @PathVariable("flatId") UUID id){

        Link link = linkTo(FlatController.class).slash(id).withSelfRel();

        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Flat flat = this.flatRepository.getByUserAndId(user, id);
            if (flat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            // FlatDetail link
            Link flat_link = linkTo(FlatController.class).slash("details").slash(flat.getId()).withRel("flatDetails");

            flat.add(link);
            flat.add(flat_link);

            return new ResponseEntity<>(flat, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get the number of flats")
    @GetMapping("/count")
    public ResponseEntity<?> count(){

        Link link = linkTo(FlatController.class).slash("count").withSelfRel();

        try{
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(this.flatRepository.getAllByUser(user).size(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get details about the flats by id")
    @GetMapping("/details/{flatId}")
    public ResponseEntity<EntityModel<?>> getDetails(
            @Parameter(
                    description = "unique id of flat",
                    example = "b8d02d81-6329-ef96-8a4d-55b376d8b25a")
            @PathVariable("flatId") UUID id){
        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Flat flat = this.flatRepository.getByUserAndId(user, id);
            if(flat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            FlatDetail flatDetail = this.flatDetailRepository.getById(flat.getFlatDetail().getId());

            Link link = linkTo(FlatController.class).slash("details").slash(flatDetail.getId()).withSelfRel();
            return ResponseEntity.ok(EntityModel.of(flatDetail,link));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create flat")
    @PostMapping("/")
    public ResponseEntity<Flat> save(@RequestBody FlatWrapper flatWrapper){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        // FlatWrapper is used to receive two objects (by default @RequestBody is mapped to only one object)
        // Note that JSON object notation in this case requires creation of dictionary where key is name of the object class
        // and value is body of this object (Postman -> (Collection) Flats -> (Request) CREATE flat)
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

            // index flat
            browserService.createFlat(flat);

            Link link = linkTo(FlatController.class).slash(flat.getId()).withSelfRel();
            // FlatDetail link
            Link flat_link = linkTo(FlatController.class).slash("details").slash(flat.getId()).withRel("flatDetails");

            flat.add(link);
            flat.add(flat_link);

            return new ResponseEntity<>(flat, HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update flat by id")
    @PutMapping("/{flatId}")
    public ResponseEntity<Flat> update(
            @Parameter(
                    description = "unique id of flat",
                    example = "b8d02d81-6329-ef96-8a4d-55b376d8b25a")
            @PathVariable("flatId") UUID id, @RequestBody FlatWrapper flatWrapper){
        Link link = linkTo(FlatController.class).slash(id).withSelfRel();

        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        // Description available in save method (above)
        FlatDTO flatDTO = flatWrapper.getFlatDTO();
        FlatDetailDTO flatDetailDTO = flatWrapper.getFlatDetailDTO();

        Flat flat = this.flatRepository.getByUserAndId(user, id);
        if(flat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            FlatDetail flatDetail = new FlatDetail();
            flatDetail.setId(flatDetailDTO.getId());
            flatDetail.setKitchen(flatDetailDTO.getKitchen());
            flatDetail.setBathroom(flatDetailDTO.getBathroom());
            flatDetail.setTowels(flatDetailDTO.getTowels());
            flatDetail.setWifi(flatDetailDTO.getWifi());

            this.flatDetailRepository.save(flatDetail);

            flat.setUser(user);
            flat.setFlatDetail(flatDetail);
            flat.setAdress(flatDTO.getAdress());
            flat.setPostCode(flatDTO.getPostCode());
            flat.setCity(flatDTO.getCity());
            flat.setCountry(flatDTO.getCountry());
            flat.setMetrage(flatDTO.getMetrage());
            flat.setNumOfRooms(flatDTO.getNumOfRooms());

            this.flatRepository.save(flat);

            // update indexed flat
            browserService.updateFlat(flat);

            // FlatDetail link
            Link flat_link = linkTo(FlatController.class).slash("details").slash(flat.getId()).withRel("flatDetails");

            flat.add(link);
            flat.add(flat_link);

            return new ResponseEntity<>(flat, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete flat by id")
    @DeleteMapping("/{flatId}")
    public ResponseEntity<?> delete(
            @Parameter(
                    description = "unique id of flat",
                    example = "b8d02d81-6329-ef96-8a4d-55b376d8b25a")
            @PathVariable("flatId") UUID flatId){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        // Check if user has permissions to delete this flat
        Flat flat = this.flatRepository.findByUserAndId(user, flatId);
        if (flat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            // Get flatdetails object id to be deleted
            FlatDetail flatDetail = flat.getFlatDetail();

            // Delete flat object - FIRST!
            this.flatRepository.delete(flat);
            // Delete flatdetails object
            this.flatDetailRepository.delete(flatDetail);

            // delete indexed flat
            browserService.deleteFlat(flat);

            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
