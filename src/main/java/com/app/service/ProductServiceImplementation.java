package com.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb.PageRequestDto;
import org.springframework.stereotype.Service;

import com.app.exception.ProductException;
import com.app.model.Category;
import com.app.model.Product;
import com.app.service.UserService;
import com.app.repository.CategoryRepository;
import com.app.repository.ProductRepository;
import com.app.request.CreateProductRequest;

@Service
public class ProductServiceImplementation implements ProductService {

	private ProductRepository productRepository;
	private UserService userService;
	private CategoryRepository categoryRepository;

	

	public ProductServiceImplementation(ProductRepository productRepository, UserService userService,
			CategoryRepository categoryRepository) {
		super();
		this.productRepository = productRepository;
		this.userService = userService;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Product createProduct(CreateProductRequest req) {
		Category topLevel = categoryRepository.findByName(req.getTopLevelCategory());

		if (topLevel == null) {
			Category topLevelCategory = new Category();
			topLevelCategory.setName(req.getTopLevelCategory());
			topLevelCategory.setLevel(1);

			// assign category to toplevel
			topLevel = categoryRepository.save(topLevelCategory);
		}

		// finding 2nd level category ny name and its parent
		Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLevelCategory(), topLevel.getName());

		if (secondLevel == null) {
			Category secondLevelCategory = new Category();
			secondLevelCategory.setName(req.getSecondLevelCategory());
			secondLevelCategory.setParentCategory(topLevel);
			secondLevelCategory.setLevel(2);

			// assign category to toplevel
			topLevel = categoryRepository.save(secondLevelCategory);
		}

		// finding 3nd level category ny name and its parent
		Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLevelCategory(),
				secondLevel.getName());

		if (thirdLevel == null) {
			Category thirdLevelCategory = new Category();
			thirdLevelCategory.setName(req.getThirdLevelCategory());
			thirdLevelCategory.setParentCategory(secondLevel);
			thirdLevelCategory.setLevel(3);

			// assign category to toplevel
			thirdLevel = categoryRepository.save(thirdLevelCategory);
		}

		Product product = new Product();
		product.setTitle(req.getTitle());
		product.setColor(req.getColor());
		product.setDescription(req.getDescription());
		product.setDiscountedPrice(req.getDiscountedPercent());
		product.setDiscountedPercent(req.getDiscountedPercent());
		product.setImageUrl(req.getImageUrl());
		product.setBrand(req.getBrand());
		product.setPrice(req.getPrice());
		product.setQuantity(req.getQuantity());
		product.setCategory(thirdLevel);
		product.setCreatedAt(LocalDateTime.now());

		Product savedProduct = productRepository.save(product);

		return savedProduct;
	}

	@Override
	public String deleteProduct(Long productId) throws ProductException {

		Product deleteProduct = findProductById(productId);
		// clear sizes if taken

		productRepository.delete(deleteProduct);

		return "Product deleted Successfully";
	}

	// to update quantity of product
	@Override
	public Product updateProduct(Long productId, Product req) throws ProductException {
		Product product = findProductById(productId);

		if (req.getQuantity() != 0) {
			product.setQuantity(req.getQuantity());
		}
		return productRepository.save(product);
	}

	// used in delete,update product
	@Override
	public Product findProductById(Long id) throws ProductException {
		Optional<Product> opt = productRepository.findById(id);

		// if product is present return it
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new ProductException("Product not found with Id - " + id);
	}

	@Override
	public List<Product> findProductByCategory(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	// pageable n pageRequest inherited from o.s.data.domain.Page
	@Override // mostimp
	public Page<Product> getAllProducts(String category, List<String> colors, Integer minPrice, Integer maxPrice,
			Integer minDiscount, String sort, String stock, Integer pageNumber, Integer PageSize) {

		Pageable pageable = PageRequest.of(pageNumber, PageSize);// will return the the page no and size of page

		// get all the products
		List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

		// anyMatch = any matching color from the given list of colors
		// equalsIgnoreCase = ignore upper or lower case
		/*
		 * if send products has color empty then dont filter, or else if passes
		 * product's color matches any of the color from the list n return list of
		 * products of all the matched product.
		 */
		if (!colors.isEmpty()) {
			products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
					.collect(Collectors.toList());
		}

		// if stock !=null and Stock==in_stock then get products whose quantity > 0
		// and if Stock==out_of_stock then get products whose quantity < 1
		if (stock != null) {
			if (stock.equals("in_stock")) {
				products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
			} else if (stock.equals("out_of_stock")) {
				products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
			}
		}
		/*
		 * startIndex -- the product from which we need to start (itne products skip
		 * krne hai) (Pageable pageable= PageRequest.of(pageNumber, PageSize) <--this
		 * will give the offset)
		 */

		/*
		 * endindex -- if there are 10 products in 1page so the start index of 2ndpage
		 * will be 11 and endIndex will be 11+10=21(startIndex+products), but now if on
		 * the last page there are only 5 products left, so taking min of products.
		 */
		int startIndex = (int) pageable.getOffset();
		int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

		List<Product> pageContent = products.subList(startIndex, endIndex);

		Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());

		return filteredProducts;
	}

}
