package sample.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import sample.domain.Brands;

public interface BrandsMapper {
  @Select("SELECT * FROM brands WHERE brand_id = #{brandId}")
  Brands getBrands(@Param("brandId") Integer brandId);
  Brands getBrandsById(Integer brandId);
}
