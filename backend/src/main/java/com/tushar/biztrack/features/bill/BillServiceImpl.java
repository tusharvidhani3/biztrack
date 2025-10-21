package com.tushar.biztrack.features.bill;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tushar.biztrack.features.party.PartyMapper;
import com.tushar.biztrack.features.product.Product;
import com.tushar.biztrack.features.product.ProductMapper;
import com.tushar.biztrack.features.product.ProductService;
import com.tushar.biztrack.features.transaction.TransactionService;

import jakarta.transaction.Transactional;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private BillItemRepository billItemRepo;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BillItemMapper billItemMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private PartyMapper partyMapper;

    @Autowired
    private TransactionService transactionService;

    @Override
    public BillResponse getBill(Long billId) {
        Bill bill = billRepo.findById(billId).get();
        List<BillItemResponse> billItems = billItemRepo.findByBill_Id(billId).stream().map(billItem -> billItemMapper.toResponse(billItem)).collect(Collectors.toList());
        BillResponse billResponse = billMapper.toResponse(bill);
        billResponse.setItems(billItems);
        return billResponse;
    }

    @Override
    public Page<BillResponse> getBills(Pageable pageable, String partyName) { // add date range
        Page<Bill> bills = billRepo.findByParty_Name(pageable, partyName);
        return bills.map(bill -> billMapper.toResponse(bill));
    }

    @Override
    @Transactional
    public BillResponse createBill(BillRequest billRequest) {
        Bill bill = billRepo.save(billMapper.toEntity(billRequest));
        List<BillItem> billItems = billRequest.getItems().stream().map(billItemRequest -> {
            BillItem billItem = billItemMapper.toEntity(billItemRequest);
            Product product = productService.getProductById(billItemRequest.getProductId());
            billItem.setProduct(productMapper.toSnapshot(product));
            billItem.setBill(bill);
            return billItem;
        }).collect(Collectors.toList());
        billItems = billItemRepo.saveAll(billItems);
        BillResponse billResponse = billMapper.toResponse(bill);
        List<BillItemResponse> billItemResponses = billItems.stream().map(billItem -> billItemMapper.toResponse(billItem)).collect(Collectors.toList());
        billResponse.setItems(billItemResponses);
        return billResponse;
    }

    @Override
    public BillResponse updateBill(BillRequest billRequest) {
        Bill bill = billRepo.findById(billRequest.getId()).get();

        if(billRequest.getParty() != null)
        bill.setParty(partyMapper.toSnapshot(billRequest.getParty()));
        if(billRequest.getItems() != null) {
            List<BillItemRequest> billItemRequests = billRequest.getItems();
            for(BillItemRequest b : billItemRequests) {
                if(b.getId() == null)
                    createBillItem(b);
                else
                    updateBillItem(b);
            }
        }
        if(billRequest.getAdditionalCharges() != null)
            bill.setAdditionalCharges(billRequest.getAdditionalCharges());

        billRepo.save(bill);

        return getBill(billRequest.getId());
    }

    @Override
    public void deleteBill(Long billId) {
        billRepo.deleteById(billId);
        billItemRepo.deleteByBill_Id(billId);
    }

    @Transactional
    private void updateBillItem(BillItemRequest billItemRequest) {
        BillItem billItem = billItemRepo.findById(billItemRequest.getId()).get();
        if(billItemRequest.getPrice() != null)
            billItem.setPrice(billItemRequest.getPrice());
        if(billItemRequest.getProductId() != null)
            billItem.setProduct(productMapper.toSnapshot(productService.getProductById(billItemRequest.getProductId())));
        if(billItemRequest.getQuantity() != null)
            billItem.setQuantity(billItemRequest.getQuantity());
    }

    private BillItemResponse createBillItem(BillItemRequest billItemRequest) {
        BillItem billItem = billItemMapper.toEntity(billItemRequest);
        billItem.setProduct(productMapper.toSnapshot(productService.getProductById(billItemRequest.getProductId())));
        return billItemMapper.toResponse(billItemRepo.save(billItem));
    }

    @Override
    public long getBillTotal(Bill bill) {
        long total = transactionService.getSaleTransactionAmount(bill.getSaleTransaction().getId());
        total += bill.getAdditionalCharges();
        return total;
    }

    @Override
    public LocalDate getBillDate(Bill bill) {
        return bill.getSaleTransaction().getDispatchDate();
    }
    
}
