package sample.dao;

import sample.domain.Brands;

public interface BrandsDao {
  Brands getBrandsById(Integer brandId);
  Brands getBrands(Integer brandId);
}
