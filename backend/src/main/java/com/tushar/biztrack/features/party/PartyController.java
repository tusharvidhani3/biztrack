package com.tushar.biztrack.features.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parties")
public class PartyController {

    @Autowired
    private PartyService partyService;
    
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PartyDto>>> searchParties(@RequestParam String name, @RequestParam PartyType type, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size, PagedResourcesAssembler<PartyDto> assembler) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PartyDto> parties = partyService.getParties(name, type, pageable);
        return ResponseEntity.ok(assembler.toModel(parties));
    }

    @PostMapping
    public ResponseEntity<PartyDto> addParty(@RequestBody PartyDto partyDto) {
        PartyDto party = partyService.createParty(partyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(party);
    }

    @PutMapping
    public ResponseEntity<PartyDto> updateParty(@RequestBody PartyDto partyDto) {
        PartyDto party = partyService.updateParty(partyDto);
        return ResponseEntity.ok(party);
    }

    
}
