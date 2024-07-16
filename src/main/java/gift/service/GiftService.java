package gift.service;


import gift.dto.PagingResponse;
import gift.model.category.Category;
import gift.model.gift.*;
import gift.repository.CategoryRepository;
import gift.repository.GiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class GiftService {

    private final GiftRepository giftRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public GiftService(GiftRepository giftRepository, CategoryRepository categoryRepository) {
        this.giftRepository = giftRepository;
        this.categoryRepository = categoryRepository;
    }


    public PagingResponse<GiftResponse> getAllGifts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<Gift> gifts = giftRepository.findAll(pageRequest);
        List<GiftResponse> giftResponses = gifts.stream()
                .map(GiftResponse::from)
                .collect(Collectors.toList());
        return new PagingResponse<>(page, giftResponses, size, gifts.getTotalElements(), gifts.getTotalPages());
    }

    public GiftResponse getGift(Long id) {
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Gift not found with id " + id));
        return GiftResponse.from(gift);
    }


    public void addGift(GiftRequest giftRequest) {
        Category category = categoryRepository.findById(giftRequest.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("해당 카테고리 id가 없습니다."));
        Gift gift = new Gift(giftRequest.getName(), giftRequest.getPrice(), giftRequest.getImageUrl(), category);
        giftRepository.save(gift);
    }

    @Transactional
    public void updateGift(GiftRequest giftRequest, Long id) {
        Category category = categoryRepository.findById(giftRequest.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("해당 카테고리 id가 없습니다."));
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 Gift가 없습니다. id : " + id));
        gift.modify(giftRequest.getName(), giftRequest.getPrice(), giftRequest.getImageUrl(), category);
        giftRepository.save(gift);
    }


    public void deleteGift(Long id) {
        giftRepository.deleteById(id);
    }
}




