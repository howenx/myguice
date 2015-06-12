package sample.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;

import sample.domain.Brands;

public class BrandsDaoImpl implements BrandsDao {
  

  @Inject
  private SqlSession sqlSession;
  
  @Override
  public Brands getBrandsById(Integer brandId) {
    System.out.println("品牌ID1: "+brandId);
    return (Brands) this.sqlSession.selectOne("BrandsDao.getBrandsById", brandId);
  }

  @Override
  public Brands getBrands(Integer brandId) {
    // TODO Auto-generated method stub
    return null;
  }
}
