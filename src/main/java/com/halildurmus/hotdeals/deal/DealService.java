package com.halildurmus.hotdeals.deal;

import com.github.fge.jsonpatch.JsonPatch;
import com.halildurmus.hotdeals.deal.dto.DealPatchDTO;
import com.halildurmus.hotdeals.security.role.IsSuper;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DealService {

  @IsSuper
  Page<Deal> findAll(Pageable pageable);

  @IsSuper
  Page<Deal> getPendingDeals(Pageable pageable);

  int countDealsByPostedBy(ObjectId postedBy);

  int countDealsByStore(ObjectId storeId);

  Optional<Deal> findById(String id);

  Page<Deal> getDealsByCategory(String category, Pageable pageable);

  Page<Deal> getDealsByStoreId(ObjectId storeId, Pageable pageable);

  Page<Deal> getLatestActiveDeals(Pageable pageable);

  Page<Deal> getMostLikedActiveDeals(Pageable pageable);

  Deal create(Deal deal);

  Deal patch(String id, DealPatchDTO dealPatchDTO);

  Deal update(Deal deal);

  void delete(String id);

  Deal vote(String id, DealVoteType voteType);

  Page<Deal> getDealsByStatus(DealStatus status, Pageable pageable);

}
