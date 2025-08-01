package com.halildurmus.hotdeals.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.halildurmus.hotdeals.deal.DealRepository;
import com.halildurmus.hotdeals.user.dummy.DummyUsers;
import com.halildurmus.hotdeals.util.FakerUtil;
import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class UserServiceImplTest {

  @MockBean private DealRepository dealRepository;

  @MockBean private FakerUtil fakerUtil;

  @MockBean private UserRepository userRepository;

  private UserService userService;

  @Test
  public void createShouldSaveUser() {
    when(fakerUtil.generateNickname()).thenReturn("MrNobody123");
    when(userRepository.insert(any(User.class))).thenReturn(DummyUsers.user1);
    userService = new UserServiceImpl(dealRepository, userRepository, fakerUtil);
    var user = userService.create(DummyUsers.user1);

    assertNotNull(user);
    assertEquals(user.getNickname(), "MrNobody123");
  }

  @Test
  public void createCallsInsertTwiceIfNicknameIsAlreadyExists() {
    when(fakerUtil.generateNickname()).thenReturn("MrNobody123").thenReturn("MrNobody124");
    when(userRepository.insert(any(User.class)))
        .thenThrow(new DuplicateKeyException("E11000 duplicate key error index: nickname"))
        .thenReturn(DummyUsers.user1);
    userService = new UserServiceImpl(dealRepository, userRepository, fakerUtil);
    var user = userService.create(DummyUsers.user1);

    verify(userRepository, times(2)).insert(DummyUsers.user1);
    assertNotNull(user);
    assertEquals(user.getNickname(), "MrNobody124");
  }
}
