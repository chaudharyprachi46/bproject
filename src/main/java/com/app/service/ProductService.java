package com.app.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.app.exception.ProductException;
import com.app.model.Product;
import com.app.request.CreateProductRequest;

public interface ProductService {
	
	public Product createProduct(CreateProductRequest req);
	
	public String deleteProduct(Long productId) throws ProductException;
	
	public Product updateProduct(Long productId, Product req) throws ProductException;
	
	public Product findProductById(Long id) throws ProductException;
	
	public List<Product> findProductByCategory(String category);
	
	//sort -- low to high     high to low
	//stock  -- instock n outOfStock
	//pageNumber n PageSize fro pagination 
	//page -- returns a paginated list of Product objects
	public Page<Product> getAllProducts(String category, List<String>colors, Integer minPrice, Integer maxPrice,
					Integer minDiscount, String sort, String stock, Integer pageNumber, Integer PageSize);

}
