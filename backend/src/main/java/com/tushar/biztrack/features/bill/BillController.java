package com.tushar.biztrack.features.bill;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;
    
    @PostMapping
    public ResponseEntity<BillResponse> createBill(@RequestBody BillRequest billRequest) {
        BillResponse billResponse = billService.createBill(billRequest);
        return ResponseEntity.ok(billResponse);
    }

    @GetMapping("/{billId}")
    public ResponseEntity<BillResponse> getBill(@PathVariable Long billId) {
        BillResponse billResponse = billService.getBill(billId);
        return ResponseEntity.ok(billResponse);
    }

    @PutMapping
    public ResponseEntity<BillResponse> updateBill(@RequestBody BillRequest billRequest) {
        BillResponse billResponse = billService.updateBill(billRequest);
        return ResponseEntity.ok(billResponse);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<BillResponse>>> getBills(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam("party-name") String partyName, @RequestParam("from") LocalDate startDate, @RequestParam("to") LocalDate endDate, PagedResourcesAssembler<BillResponse> assembler) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BillResponse> bills = billService.getBills(pageable, partyName);
        return ResponseEntity.ok(assembler.toModel(bills));
    }
}
