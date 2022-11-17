package com.ghtk.onlinebiddingproject;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ghtk.onlinebiddingproject.models.dtos.*;
import com.ghtk.onlinebiddingproject.models.entities.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlinebiddingprojectApplication {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Bid, BidDto> localDateTimeToStringBid = new PropertyMap<Bid, BidDto>() {
            protected void configure() {
                map().setCreatedAt(String.valueOf(source.getCreatedAt()));
            }
        };
        PropertyMap<Report, ReportDto> localDateTimeToStringReport = new PropertyMap<Report, ReportDto>() {
            protected void configure() {
                map().setCreatedAt(String.valueOf(source.getCreatedAt()));
            }
        };
        PropertyMap<Auction, AuctionDto> localDateTimeToStringAuction = new PropertyMap<Auction, AuctionDto>() {
            protected void configure() {
                map().setCreatedAt(String.valueOf(source.getCreatedAt()));
            }
        };
        PropertyMap<Notification, NotificationDto> localDateTimeToStringNotification = new PropertyMap<Notification, NotificationDto>() {
            protected void configure() {
                map().setUpdatedAt(String.valueOf(source.getUpdatedAt()));
            }
        };
        PropertyMap<Profile, ProfileDto> localDateTimeToStringProfile = new PropertyMap<Profile, ProfileDto>() {
            protected void configure() {
                map().setCreatedAt(String.valueOf(source.getCreatedAt()));
            }
        };
        modelMapper.addMappings(localDateTimeToStringBid);
        modelMapper.addMappings(localDateTimeToStringReport);
        modelMapper.addMappings(localDateTimeToStringAuction);
        modelMapper.addMappings(localDateTimeToStringNotification);
        modelMapper.addMappings(localDateTimeToStringProfile);
        return modelMapper;
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "metanoia",
                "api_key", "899938948262375",
                "api_secret", "28mQ7ymH9cjf1lW5o1lN09gEsc4",
                "secure", true));
    }

    public static void main(String[] args) {
        SpringApplication.run(OnlinebiddingprojectApplication.class, args);

    }
}
