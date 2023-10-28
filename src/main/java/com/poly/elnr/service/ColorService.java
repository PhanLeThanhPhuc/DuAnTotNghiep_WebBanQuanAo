package com.poly.elnr.service;

import java.util.List;



import com.poly.elnr.entity.Color;

public interface ColorService {

	public List<Color> findAllColor();

	public List<Color> findAllColorEight();

	/* public List<Color> findAll(); */

	public Color create(Color create);

	public void delete(Color id);

	public Color update(Color color);

}
