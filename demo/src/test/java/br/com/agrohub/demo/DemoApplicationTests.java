package br.com.agrohub.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import br.com.agrohub.demo.repository.UserRepository;

@SpringBootTest
class DemoApplicationTests {

	@MockitoBean
	UserRepository userRepository;

	@Test
	void contextLoads(ApplicationContext context) {
		assertThat(context).isNotNull();
	}

}
