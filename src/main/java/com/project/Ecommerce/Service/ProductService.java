package com.project.Ecommerce.Service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.project.Ecommerce.Dto.ProductDTO;
import com.project.Ecommerce.Dto.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    ProductResponse searchBykeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

}
