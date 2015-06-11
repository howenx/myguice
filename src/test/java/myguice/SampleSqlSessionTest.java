/**
 * Copyright 2009-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package myguice;

import static com.google.inject.name.Names.bindProperties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.guice.MyBatisModule;
// import org.mybatis.guice.XMLMyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import sample.dao.BrandsDao;
import sample.dao.BrandsDaoImpl;
import sample.domain.Brands;
import sample.domain.User;
import sample.mapper.BrandsMapper;
import sample.service.CustomException;
import sample.service.FooService;
import sample.service.ItemService;
import sample.service.ItemServiceMapperImpl;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Example of MyBatis-Guice basic integration usage.
 *
 * This is the recommended scenario.
 *
 * @version $Id$
 */
public class SampleSqlSessionTest {

  private Injector injector;

  private FooService fooService;

  private ItemService itemService;
  
  @Before
  public void setupMyBatisGuice() throws Exception {

    // bindings
    List<Module> modules = this.createMyBatisModule();
    this.injector = Guice.createInjector(modules);

    // prepare the test db
    // Environment environment =
    // this.injector.getInstance(SqlSessionFactory.class).getConfiguration().getEnvironment();
    // DataSource dataSource = environment.getDataSource();
    // ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
    // runner.setAutoCommit(true);
    // runner.setStopOnError(true);
    // runner.runScript(getResourceAsReader("sample/db/database-schema.sql"));
    // runner.runScript(getResourceAsReader("sample/db/database-test-data.sql"));
    // runner.closeConnection();

    //this.fooService = this.injector.getInstance(FooService.class);
    this.itemService = this.injector.getInstance(ItemService.class);
  }

  protected List<Module> createMyBatisModule() {
    List<Module> modules = new ArrayList<Module>();
    modules.add(JdbcHelper.PostgreSQL);
    modules.add(new MyBatisModule() {

      @Override
      protected void initialize() {
        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
       // addMapperClass(UserMapper.class);
        addMapperClass(BrandsMapper.class);
      }

    });
    /*
     * modules.add(new XMLMyBatisModule() {
     * 
     * @Override protected void initialize() { setEnvironmentId("test");
     * setClassPathResource("org/mybatis/guice/sample/mybatis-config.xml"); }
     * 
     * });
     */
    modules.add(new Module() {
      public void configure(Binder binder) {
        bindProperties(binder, createTestProperties());
        //binder.bind(FooService.class).to(FooServiceDaoImpl.class);
        //binder.bind(UserDao.class).to(UserDaoImpl.class);
        binder.bind(BrandsDao.class).to(BrandsDaoImpl.class);
        binder.bind(ItemService.class).to(ItemServiceMapperImpl.class);
      }
    });

    return modules;
  }

  protected static Properties createTestProperties() {
    final Properties myBatisProperties = new Properties();
    myBatisProperties.setProperty("mybatis.environment.id", "test");
    myBatisProperties.setProperty("JDBC.host", "172.28.3.17");
    // configure the database port
    myBatisProperties.setProperty("JDBC.port", "5432");
    // configure the database schema
    myBatisProperties.setProperty("JDBC.schema", "postgres");
    myBatisProperties.setProperty("JDBC.username", "postgres");
    myBatisProperties.setProperty("JDBC.password", "postgres");
    // myBatisProperties.setProperty("JDBC.url", "jdbc:postgresql://172.28.3.17:5432/postgres");
    myBatisProperties.setProperty("JDBC.autoCommit", "false");
    return myBatisProperties;
  }

  @Test
  public void testFooService() {
    Brands user = this.itemService.getBrand(11022);
    System.out.println("User is: " + user.getBrandNmEn());
    assertNotNull(user);
    assertEquals("Pocoyo", user.getBrandNmEn());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTransactionalOnClassAndMethod() {
    User user = new User();
    user.setName("Christian Poitras");
    this.fooService.brokenInsert(user);
  }

  @Test(expected = CustomException.class)
  public void testTransactionalOnClass() {
    User user = new User();
    user.setName("Christian Poitras");
    this.fooService.brokenInsert2(user);
  }
}
