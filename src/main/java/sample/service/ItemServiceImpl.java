package sample.service;

import javax.inject.Inject;

import org.mybatis.guice.transactional.Isolation;
import org.mybatis.guice.transactional.Transactional;

import sample.dao.BrandsDao;
import sample.domain.Brands;

public class ItemServiceImpl implements ItemService {

  @Inject
  private BrandsDao brandsDao;


  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Brands getBrand(Integer brandId) {
    return this.brandsDao.getBrandsById(brandId);
  }
}
