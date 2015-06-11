package sample.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;

import sample.domain.Brands;

public class BrandsDaoImpl implements BrandsDao {
  

  @Inject
  private SqlSession sqlSession;
  
  @Override
  public Brands getBrandsById(Integer brandId) {
    System.out.println("品牌ID: "+brandId);
    return (Brands) this.sqlSession.selectOne("sample.mapper.BrandsMapper.getBrandsById", brandId);
  }
}
