package com.poly.elnr.service.serviceImpl;

import org.springframework.stereotype.Service;

import com.poly.elnr.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService{

<<<<<<< Updated upstream
=======
	@Autowired
	ProductRepository dao;

	public List<Product> findAll() {
		return dao.findAll();
	}
	
	public Product findById(Integer id) {
		return dao.findById(id).get();
	}

	/*
	 * public List<Product> findByCategoryId(String cid) { return
	 * dao.findByCategoryId(cid); }
	 */
	public Product create(Product product) {
		return dao.save(product);
	}

	public Product update(Product product) {
		return dao.save(product);
	}

	public void delete(Integer id) {
		dao.deleteById(id);
	}

	@Override
	public List<Product> findByCategoryId(String cid) {
		// TODO Auto-generated method stub
		return null;
	}
>>>>>>> Stashed changes
}
