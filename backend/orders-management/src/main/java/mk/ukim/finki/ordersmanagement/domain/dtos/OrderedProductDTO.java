package mk.ukim.finki.ordersmanagement.domain.dtos;

import lombok.Data;
import mk.ukim.finki.ordersmanagement.domain.models.ids.OrderedProductId;
import mk.ukim.finki.productsmanagement.domain.dtos.ProductDTO;

import java.io.File;
import java.time.OffsetDateTime;

@Data
public class OrderedProductDTO {
    private final OrderedProductId id;
    private final OffsetDateTime dateCreated;
    private final OffsetDateTime dateModified;
    private final Integer quantity;
    private final Double price;
    private final Double totalPrice;
    private final String title;
    private final String description;
    private final byte[] image;
    private final ProductDTO productDTO;
}
