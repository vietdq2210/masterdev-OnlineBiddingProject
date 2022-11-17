package com.ghtk.onlinebiddingproject.controllers;

import com.ghtk.onlinebiddingproject.models.dtos.ProfileDto;
import com.ghtk.onlinebiddingproject.models.entities.Profile;
import com.ghtk.onlinebiddingproject.models.responses.CommonResponse;
import com.ghtk.onlinebiddingproject.models.responses.ProfilePagingResponse;
import com.ghtk.onlinebiddingproject.models.responses.ProfilePagingResponseDto;
import com.ghtk.onlinebiddingproject.services.impl.ProfileServiceImpl;
import com.ghtk.onlinebiddingproject.utils.HttpHeadersUtils;
import com.ghtk.onlinebiddingproject.utils.converters.EntityToDtoConverter;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/admin/profiles")
public class AdminProfileController {
    @Autowired
    private ProfileServiceImpl profileService;
    @Autowired
    private EntityToDtoConverter entityToDtoConverter;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CommonResponse> adminGetAll(
            @And({
                    @Spec(path = "status", params = "status", spec = Equal.class),
            }) Specification<Profile> spec,
            Sort sort,
            @RequestHeader HttpHeaders headers) {
        ProfilePagingResponse pagingResponse = profileService.adminGetAll(spec, headers, Sort.by(Sort.Direction.DESC, "createdAt"));
        ProfilePagingResponseDto dtoResponse = entityToDtoConverter.convertToDto(pagingResponse);
        CommonResponse response = new CommonResponse(true, "Success", dtoResponse, null);
        return new ResponseEntity<>(response, HttpHeadersUtils.returnHttpHeaders(pagingResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CommonResponse> adminChangeUserStatus(@PathVariable("id") int id, @RequestBody @Valid ProfileDto profileDto) {
        ProfileDto dtoResponse = entityToDtoConverter.convertToDto(profileService.adminChangeUserStatus(id, profileDto));
        CommonResponse response = new CommonResponse(true, "Success", dtoResponse, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
