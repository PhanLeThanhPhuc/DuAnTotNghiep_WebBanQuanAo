package com.poly.elnr.controller;

import java.util.List;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;
import com.poly.elnr.service.ProductDetailService;

import com.poly.elnr.entity.Product;
import com.poly.elnr.repository.ProductRepository;
import com.poly.elnr.service.ProductService;




import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.elnr.repository.CategoryRepository;
import com.poly.elnr.repository.ProductRepository;
import com.poly.elnr.service.ProductService;
import com.poly.elnr.service.SizeService;


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
	CategoryRepository categoryRepository;
	

	@RequestMapping("productcategory")
	public String viewProductCategory(Model model,
											@RequestParam("idCategory") int idCategory,
											@RequestParam(name = "size", required = false) List<Integer> sizeId,
											@RequestParam(name = "color", required = false) List<Integer> colorId,
											@RequestParam(name = "sort", required = false) Optional<String> sort,
											@RequestParam(name = "p", required = false) Optional<Integer> p) {
		model.addAttribute("product", productService.findProductByCategoryFilter(idCategory,
				colorId,
				sizeId,
				sort,
				p));
		model.addAttribute("sizeId", sizeId != null ? sizeId : 0);
		model.addAttribute("colorId", colorId != null ? colorId : 0);
		model.addAttribute("sortValue", sort.orElse("price-asc"));
		model.addAttribute("idCategory", idCategory);
		return "user/product/productCategory";
	}

	@RequestMapping("productcategorydetail")
	public String viewProductCategoryDetail(Model model,
								   @RequestParam("idCategoryDetail") int idCategoryDetail,
	                               @RequestParam(name = "size", required = false) List<Integer> sizeId,
	                               @RequestParam(name = "color", required = false) List<Integer> colorId,
	                               @RequestParam(name = "sort", required = false) Optional<String> sort,
	                               @RequestParam(name = "p", required = false) Optional<Integer> p) {
		model.addAttribute("product", productService.findProductByCategoryDetailFilter(idCategoryDetail,
				colorId,
				sizeId,
				sort,
				p));
		model.addAttribute("sizeId", sizeId != null ? sizeId : 0);
		model.addAttribute("colorId", colorId != null ? colorId : 0);
		model.addAttribute("sortValue", sort.orElse("price-asc"));
		model.addAttribute("idCategoryDetail", idCategoryDetail);
		return "user/product/productCategoryDetail";
	}
	
	


	
	@RequestMapping("product-detail-{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		List<ProductDetails> size = detailService.findByProductID(id);
		model.addAttribute("sizes", size);
		Product item = productService.findById(id)	;
		model.addAttribute("item", item);
		return "user/product/detail";
	}
	

}
