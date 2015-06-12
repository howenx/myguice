package myguice;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mybatis.guice.XMLMyBatisModule;

import sample.domain.Brands;
import sample.service.ItemService;
import sample.service.ItemServiceImpl;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;


public class SampleSqlSessionTest {

  private Injector injector;


  private ItemService itemService;

  @Before
  public void setupMyBatisGuice() throws Exception {

    List<Module> modules = this.createMyBatisModule();
    this.injector = Guice.createInjector(modules);

    this.itemService = this.injector.getInstance(ItemService.class);
  }

  protected List<Module> createMyBatisModule() {
    List<Module> modules = new ArrayList<Module>();

    modules.add(new XMLMyBatisModule() {

      @Override
      protected void initialize() {
        setEnvironmentId("development");
        setClassPathResource("sample/mybatis-config.xml");
      }

    });

    modules.add(new Module() {
      public void configure(Binder binder) {
        binder.bind(ItemService.class).to(ItemServiceImpl.class);
      }
    });

    return modules;
  }

  @Test
  public void testFooService() {
    Brands user = this.itemService.getBrand(11019);
    System.out.println("User is: " + user.getBrandNmEn());
  }
}
