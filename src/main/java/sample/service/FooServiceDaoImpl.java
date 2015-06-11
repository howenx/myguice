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
package sample.service;



import javax.inject.Inject;

import org.mybatis.guice.transactional.Isolation;
import org.mybatis.guice.transactional.Transactional;

import sample.dao.UserDao;
import sample.domain.User;

/**
 * Impl of the FooService.
 *
 * FooService simply receives a userId and uses a mapper/dao to get a record from the database.
 *
 * @version $Id$
 */
@Transactional(rethrowExceptionsAs = CustomException.class)
public class FooServiceDaoImpl implements FooService {
  @Inject
  private UserDao userDao;


  @Transactional(isolation = Isolation.SERIALIZABLE)
  public User doSomeBusinessStuff(String userId) {
    return this.userDao.getUser(userId);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = IllegalArgumentException.class)
  public void brokenInsert(User user) {
    this.userDao.brokenInsert(user);
  }

  public void brokenInsert2(User user) {
    this.userDao.brokenInsert(user);
  }
}
