package com.ddd.infrastructure.user.cache;

import com.ddd.domain.user.aggregate.UserAggregate;
import com.ddd.domain.user.cache.UserCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCacheServiceServiceImpl implements UserCacheService {

  public void saveCache(UserAggregate userAggregate){
    log.info("缓存 UserAggregate.userModel");
  }


}
