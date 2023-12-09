package com.poly.elnr.controller;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.elnr.entity.Review;
import com.poly.elnr.service.DiscountCheckService;
import com.poly.elnr.service.DiscountService;
import com.poly.elnr.service.ImageService;
import com.poly.elnr.service.ProductDetailService;

import com.poly.elnr.entity.Product;
import com.poly.elnr.repository.ProductRepository;
import com.poly.elnr.repository.ReviewRepository;
import com.poly.elnr.service.ProductService;
import com.poly.elnr.service.ReviewService;
import com.poly.elnr.service.SizeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("user")
public class ProductController {

	@Autowired
	ProductDetailService detailService;
	@Autowired
	ProductService productService;
	@Autowired
	SizeService sizeService;
	@Autowired
	ReviewService reviewService;
	@Autowired
	ImageService imageService;

	@Autowired
	DiscountCheckService discountCheckService;
	
	
	
	@RequestMapping("productcategory")
	public String viewProductCategory(Model model, @RequestParam("idCategory") int idCategory,
			@RequestParam(name = "size", required = false) List<Integer> sizeId,
			@RequestParam(name = "color", required = false) List<Integer> colorId,
			@RequestParam(name = "sort", required = false) Optional<String> sort,
			@RequestParam(name = "p", required = false) Optional<Integer> p,
			@RequestParam(name = "min", required = false) String min,
			@RequestParam(name = "max", required = false) String max) {
		if(min == null) min="0";
		if(max == null) max="1000000";
		Page<Product> products = productService.findProductByCategoryFilter(idCategory, colorId, sizeId,
				sort, p,min,max);
		List<Product> productList = products.getContent();
		List<Product> updatedProductList = discountCheckService.getAllDiscountProducts(productList);
		model.addAttribute("product", new PageImpl<>(updatedProductList, products.getPageable(), products.getTotalElements()));
		model.addAttribute("sizeId", sizeId != null ? sizeId : 0);
		model.addAttribute("colorId", colorId != null ? colorId : 0);
		model.addAttribute("sortValue", sort.orElse("price-asc"));
		model.addAttribute("idCategory", idCategory);
		model.addAttribute("name", "idCategory");
		model.addAttribute("form", "productcategory");
		model.addAttribute("min", min);
		model.addAttribute("max", max);
		return "user/product/productCategory";
	}

	@RequestMapping("productcategorydetail")
	public String viewProductCategoryDetail(Model model, @RequestParam("idCategoryDetail") int idCategoryDetail,
			@RequestParam(name = "size", required = false) List<Integer> sizeId,
			@RequestParam(name = "color", required = false) List<Integer> colorId,
			@RequestParam(name = "sort", required = false) Optional<String> sort,
			@RequestParam(name = "p", required = false) Optional<Integer> p,
			@RequestParam(name = "min", required = false) String min,
			@RequestParam(name = "max", required = false) String max) {
		
		if(min == null) min="0";
		if(max == null) max="1000000";
		Page<Product> products = productService.findProductByCategoryDetailFilter(idCategoryDetail, colorId, sizeId,
				sort, p,min,max);
		List<Product> productList = products.getContent();
		List<Product> updatedProductList = discountCheckService.getAllDiscountProducts(productList);
		model.addAttribute("product",
				new PageImpl<>(updatedProductList, products.getPageable(), products.getTotalElements()));
		model.addAttribute("sizeId", sizeId != null ? sizeId : 0);
		model.addAttribute("colorId", colorId != null ? colorId : 0);
		model.addAttribute("sortValue", sort.orElse("price-asc"));
		model.addAttribute("idCategory", idCategoryDetail);
		model.addAttribute("name", "idCategoryDetail");
		model.addAttribute("form", "productcategorydetail");
		model.addAttribute("min", min);
		model.addAttribute("max", max);
		return "user/product/productCategory";
	}
	@RequestMapping("productsale")
	public String viewProductSale(Model model, 
			@RequestParam(name = "size", required = false) List<Integer> sizeId,
			@RequestParam(name = "color", required = false) List<Integer> colorId,
			@RequestParam(name = "sort", required = false) Optional<String> sort,
			@RequestParam(name = "p", required = false) Optional<Integer> p,
			@RequestParam(name = "min", required = false) String min,
			@RequestParam(name = "max", required = false) String max) {
		
		if(min == null) min="0";
		if(max == null) max="1000000";
		model.addAttribute("product",discountCheckService.getDiscountProducts2( productService.findSale( colorId, sizeId),sort,p,Double.valueOf(min),Double.valueOf(max)));
		model.addAttribute("sizeId", sizeId != null ? sizeId : 0);
		model.addAttribute("colorId", colorId != null ? colorId : 0);
		model.addAttribute("sortValue", sort.orElse("price-asc"));
		model.addAttribute("min", min);
		model.addAttribute("max", max);
		return "user/product/productSale";
	}
	
	
	@RequestMapping("productSearch")
	public String viewProductSearch(Model model, 
			@RequestParam(name = "size", required = false) List<Integer> sizeId,
			@RequestParam(name = "color", required = false) List<Integer> colorId,
			@RequestParam(name = "sort", required = false) Optional<String> sort,
			@RequestParam(name = "p", required = false) Optional<Integer> p,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "min", required = false) String min,
			@RequestParam(name = "max", required = false) String max){
		
		if(min == null) min="0";
		if(max == null) max="1000000";
		Page<Product> products = productService.findProductSearch( colorId, sizeId,
				sort, p, search,min,max);
		List<Product> productList = products.getContent();
		List<Product> updatedProductList = discountCheckService.getAllDiscountProducts(productList);
		model.addAttribute("product",new PageImpl<>(updatedProductList, products.getPageable(), products.getTotalElements()));
		model.addAttribute("sizeId", sizeId != null ? sizeId : 0);
		model.addAttribute("colorId", colorId != null ? colorId : 0);
		model.addAttribute("sortValue", sort.orElse("price-asc"));
		model.addAttribute("search", search);
		model.addAttribute("min", min);
		model.addAttribute("max", max);
		return "user/product/productSearch";
	}

	@RequestMapping("product-detail")
	public String detail(Model model, @RequestParam("product-id") Integer id,
			@RequestParam(name = "p", required = false) Optional<Integer> p) {
		List<Review> rating = reviewService.findByProductID2(id);
		if (!rating.isEmpty()) {
			model.addAttribute("rating", rating.stream().mapToDouble(n -> n.getRating()).average().getAsDouble());
			model.addAttribute("ratingTotal", rating.stream().count());
			model.addAttribute("rating5", rating.stream().filter(n -> n.getRating() == 5).count());
			model.addAttribute("rating4", rating.stream().filter(n -> n.getRating() == 4).count());
			model.addAttribute("rating3", rating.stream().filter(n -> n.getRating() == 3).count());
			model.addAttribute("rating2", rating.stream().filter(n -> n.getRating() == 2).count());
			model.addAttribute("rating1", rating.stream().filter(n -> n.getRating() == 1).count());
		} 
		model.addAttribute("images", imageService.findByProductID(id));
		model.addAttribute("reviews", reviewService.findByProductID(id, p));
		model.addAttribute("sizes", detailService.findByProductID(id));
		model.addAttribute("soluong", detailService.findByProductID(id).stream().mapToInt(n -> n.getQuantity()).sum());
		model.addAttribute("item",discountCheckService.DiscountProduct(productService.findById(id)));
		model.addAttribute("isDiscount",discountCheckService.isDiscountProduct(productService.findById(id)));
		
		return "user/product/detail";
	}

}
