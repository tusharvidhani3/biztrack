package com.tushar.biztrack.features.account;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tushar.biztrack.features.party.PartyType;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<AccountSummary>>> searchAccounts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size, @RequestParam("party-name") String partyName, @RequestParam("party-type") PartyType type, PagedResourcesAssembler<AccountSummary> assembler) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountSummary> accounts = accountService.getAccounts(pageable, partyName, type);
        return ResponseEntity.ok(assembler.toModel(accounts));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 1);
        AccountResponse accountDto = accountService.getAccount(accountId, pageable);
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("/payment-entries")
    public ResponseEntity<AccountEntryResponse> createPaymentAccountEntry(@RequestBody PaymentAccountEntryRequest paymentAccountEntryRequest) {
        AccountEntryResponse accountEntryResponse = accountService.createPaymentAccountEntry(paymentAccountEntryRequest);
        return ResponseEntity.ok(accountEntryResponse);
    }
    

}
