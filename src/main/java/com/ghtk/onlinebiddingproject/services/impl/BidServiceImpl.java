package com.ghtk.onlinebiddingproject.services.impl;

import com.ghtk.onlinebiddingproject.constants.AuctionStatusConstants;
import com.ghtk.onlinebiddingproject.exceptions.BadRequestException;
import com.ghtk.onlinebiddingproject.exceptions.NotFoundException;
import com.ghtk.onlinebiddingproject.models.entities.Auction;
import com.ghtk.onlinebiddingproject.models.entities.Bid;
import com.ghtk.onlinebiddingproject.models.entities.User;
import com.ghtk.onlinebiddingproject.models.requests.BidRequestDto;
import com.ghtk.onlinebiddingproject.repositories.AuctionRepository;
import com.ghtk.onlinebiddingproject.repositories.BidRepository;
import com.ghtk.onlinebiddingproject.repositories.UserRepository;
import com.ghtk.onlinebiddingproject.security.UserDetailsImpl;
import com.ghtk.onlinebiddingproject.services.BidService;
import com.ghtk.onlinebiddingproject.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class BidServiceImpl implements BidService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private AuctionUserServiceImpl auctionUserService;
    @Autowired
    private NotificationServiceImpl notificationService;

    @Override
    public List<Bid> getBidsByAuctionId(Integer auctionId) {
        return bidRepository.findByAuction_Id(auctionId, Sort.by(Sort.Direction.DESC, "price"));
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class}, isolation = Isolation.READ_UNCOMMITTED)
    public Bid saveBid(Integer auctionId, BidRequestDto bidDto, Bid bid) {
        UserDetailsImpl userDetails = CurrentUserUtils.getCurrentUserDetails();
        if (userDetails.isSuspended()) throw new AccessDeniedException("T??i kho???n c???a b???n ??ang b??? gi???i h???n!");

        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new NotFoundException("Kh??ng t??m th???y user n??y!"));
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new NotFoundException("Kh??ng t??m th???y auction v???i id n??y!"));
        Double currentHighestPrice = auction.getHighestPrice() != 0 ? auction.getHighestPrice() : auction.getPriceStart();
        if (auction.getUser().getId().equals(userDetails.getId()))
            throw new AccessDeniedException("B???n kh??ng th??? tr??? gi?? cho b??i ?????u gi?? c???a ch??nh m??nh!");
        if (!auction.getStatus().equals(AuctionStatusConstants.OPENING))
            throw new AccessDeniedException("Hi???n t???i kh??ng th??? tr??? gi?? cho b??i ?????u gi?? n??y!");
        if (bidDto.getPrice() < auction.getPriceStart())
            throw new BadRequestException("Gi?? m???i ph???i cao h??n gi?? kh???i ??i???m v?? gi?? hi???n t???i!");
        if (bidDto.getPrice() < auction.getHighestPrice() + auction.getPriceStep())
            throw new BadRequestException("Gi?? m???i ph???i cao h??n gi?? cao nh???t hi???n t???i + b?????c gi??!");
        if (currentHighestPrice >= 100000 && bidDto.getPrice() > (currentHighestPrice * 3))
            throw new BadRequestException("Gi?? m???i kh??ng ???????c h??n g???p 3 gi?? cao nh???t hi???n t???i");

        auction.setHighestPrice(bidDto.getPrice());
        auctionRepository.save(auction);
        auctionUserService.saveInterestedAuction(auctionId);

        bid.setUser(currentUser);
        bid.setAuction(auction);
        return bidRepository.save(bid);
    }
}
