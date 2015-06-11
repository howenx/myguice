package sample.service;

import javax.inject.Inject;

import org.mybatis.guice.transactional.Isolation;
import org.mybatis.guice.transactional.Transactional;

import sample.domain.Brands;
import sample.mapper.BrandsMapper;

public class ItemServiceMapperImpl implements ItemService {

  @Inject
  private BrandsMapper brandsDao;


  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Brands getBrand(Integer brandId) {
    return this.brandsDao.getBrandsById(brandId);
  }
}
