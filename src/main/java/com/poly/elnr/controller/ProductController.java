package com.poly.elnr.controller;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	@Autowired
	DiscountService discountService;
	
	@RequestMapping("productcategory")
	public String viewProductCategory(Model model, @RequestParam("idCategory") int idCategory,
			@RequestParam(name = "size", required = false) List<Integer> sizeId,
			@RequestParam(name = "color", required = false) List<Integer> colorId,
			@RequestParam(name = "sort", required = false) Optional<String> sort,
			@RequestParam(name = "p", required = false) Optional<Integer> p) {
		Page<Product> products = productService.findProductByCategoryFilter(idCategory, colorId, sizeId, sort, p);
		List<Product> productList = products.getContent();
		List<Product> updatedProductList = discountCheckService.getAllDiscountProducts(productList);
		model.addAttribute("product", new PageImpl<>(updatedProductList, products.getPageable(), products.getTotalElements()));
		model.addAttribute("sizeId", sizeId != null ? sizeId : 0);
		model.addAttribute("colorId", colorId != null ? colorId : 0);
		model.addAttribute("sortValue", sort.orElse("price-asc"));
		model.addAttribute("idCategory", idCategory);
		return "user/product/productCategory";
	}

	@RequestMapping("productcategorydetail")
	public String viewProductCategoryDetail(Model model, @RequestParam("idCategoryDetail") int idCategoryDetail,
			@RequestParam(name = "size", required = false) List<Integer> sizeId,
			@RequestParam(name = "color", required = false) List<Integer> colorId,
			@RequestParam(name = "sort", required = false) Optional<String> sort,
			@RequestParam(name = "p", required = false) Optional<Integer> p) {

		Page<Product> products = productService.findProductByCategoryDetailFilter(idCategoryDetail, colorId, sizeId,
				sort, p);
		List<Product> productList = products.getContent();
		List<Product> updatedProductList = discountCheckService.getAllDiscountProducts(productList);
		model.addAttribute("product",
				new PageImpl<>(updatedProductList, products.getPageable(), products.getTotalElements()));
		model.addAttribute("sizeId", sizeId != null ? sizeId : 0);
		model.addAttribute("colorId", colorId != null ? colorId : 0);
		model.addAttribute("sortValue", sort.orElse("price-asc"));
		model.addAttribute("idCategoryDetail", idCategoryDetail);
		return "user/product/productCategoryDetail";
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
		} else {
			model.addAttribute("rating", "0");
			model.addAttribute("ratingTotal", 0);
			model.addAttribute("rating5", 0);
			model.addAttribute("rating4", 0);
			model.addAttribute("rating3", 0);
			model.addAttribute("rating2", 0);
			model.addAttribute("rating1", 0);
		}
		

		model.addAttribute("images", imageService.findByProductID(id));
		model.addAttribute("reviews", reviewService.findByProductID(id, p));
		model.addAttribute("sizes", detailService.findByProductID(id));
		model.addAttribute("soluong", detailService.findByProductID(id).stream().mapToInt(n -> n.getQuantity()).sum());
		model.addAttribute("item",discountCheckService.saveDiscountProduct(productService.findById(id)));
		return "user/product/detail";
	}

}
