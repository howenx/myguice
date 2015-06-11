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

import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.bindProperties;
import static org.apache.ibatis.io.Resources.getResourceAsReader;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import sample.dao.UserDao;
import sample.dao.UserDaoImpl;
import sample.domain.User;
import sample.mapper.UserMapper;
import sample.service.CustomException;
import sample.service.FooService;
import sample.service.FooServiceMapperImpl;

import com.google.inject.Injector;

/**
 * Example of MyBatis-Guice basic integration usage.
 *
 * This is the recommended scenario.
 *
 * @version $Id$
 */
public class SampleBasicTest {

  private Injector injector;

  private FooService fooService;

  @Before
  public void setupMyBatisGuice() throws Exception {

    // bindings
    this.injector = createInjector(new MyBatisModule() {

      @Override
      protected void initialize() {
        install(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);

        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        addMapperClass(UserMapper.class);

        bindProperties(binder(), createTestProperties());
        bind(FooService.class).to(FooServiceMapperImpl.class);
        bind(UserDao.class).to(UserDaoImpl.class);
      }

    });

    // prepare the test db
    Environment environment =
        this.injector.getInstance(SqlSessionFactory.class).getConfiguration().getEnvironment();
    DataSource dataSource = environment.getDataSource();
    ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
    runner.setAutoCommit(true);
    runner.setStopOnError(true);
    runner.runScript(getResourceAsReader("sample/db/database-schema.sql"));
    runner.runScript(getResourceAsReader("sample/db/database-test-data.sql"));
    runner.closeConnection();

    this.fooService = this.injector.getInstance(FooService.class);
  }

  protected static Properties createTestProperties() {
    Properties myBatisProperties = new Properties();
    myBatisProperties.setProperty("mybatis.environment.id", "test");
    myBatisProperties.setProperty("JDBC.username", "sa");
    myBatisProperties.setProperty("JDBC.password", "");
    myBatisProperties.setProperty("JDBC.autoCommit", "false");
    return myBatisProperties;
  }

  @Test
  public void testFooService() {
    User user = this.fooService.doSomeBusinessStuff("u4");
    System.out.println("User is: " + user.toString());
    // assertNotNull(user);
    // assertEquals("Pocoyo", user.getName());
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

  @Test
  public void shouldNotFailOnObjectsMethodsCall() {
    this.fooService.toString();
  }

}
