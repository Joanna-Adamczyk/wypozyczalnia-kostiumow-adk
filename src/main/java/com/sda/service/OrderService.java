package com.sda.service;

import com.sda.model.Costume;
import com.sda.repository.CostumeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@Service
public class OrderService {

    private CostumeRepository costumeRepository;

    public OrderService(CostumeRepository costumeRepository) {
        this.costumeRepository = costumeRepository;
    }

    public Page<Costume> findAll(PageRequest page) {
        return costumeRepository.findAll(page);
    }

    public List<Costume> listAll() {
        List<Costume> costumes = new ArrayList<>();
        costumeRepository.findAll().forEach(costumes::add);
        return costumes;
    }

    public Costume getOne(Long id) {
        return costumeRepository.getOne(id);
    }

    public Costume add(Costume costume) {
        return costumeRepository.save(costume);
    }

    public void remove(Long id) {
        costumeRepository.delete(costumeRepository.findById(id).get());
    }

    public Optional<Costume> borrowCostume(Long id) {
        Optional<Costume> notBorrowedCostume = costumeRepository.findById(id).stream()
                .filter(costume -> costume.getBorrowedTill() == null).findAny();
        if (notBorrowedCostume.isPresent()) {
            Costume costumeToBorrow = notBorrowedCostume.get();
            costumeToBorrow.setBorrowedTill(LocalDate.now().plusDays(7));
            return Optional.of(costumeRepository.save(costumeToBorrow));
        }
        return notBorrowedCostume;
    }

    public Costume returnCostume(Long id) {
        Costume costumeToReturn = costumeRepository.findById(id).get();
        costumeToReturn.setBorrowedTill(null);
        return costumeRepository.save(costumeToReturn);
    }

    public Page<Costume> findByName (String name, Integer page) {
        return costumeRepository.findByNameLikeIgnoreCase("%"+name+"%", PageRequest.of(page, 6));
    }

}
