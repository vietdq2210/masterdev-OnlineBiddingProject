package com.ghtk.onlinebiddingproject.models.requests;

import com.ghtk.onlinebiddingproject.models.dtos.ItemImageDto;
import com.ghtk.onlinebiddingproject.models.entities.Auction;
import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ItemRequestDto {

    private Integer id;

    @Size(max = 200, message = "Tên sản phẩm không được quá 200 ký tự!")
    private String name;

    @Size(max = 1000, message = "Mô tả sản phẩm không được quá 200 ký tự!")
    private String description;

    @Positive(message = "Số lượng sản phẩm không được nhỏ hơn hoặc bằng 0!")
    private Integer amount;

    private Auction auction;

    private List<ItemImageDto> itemImages;
}
