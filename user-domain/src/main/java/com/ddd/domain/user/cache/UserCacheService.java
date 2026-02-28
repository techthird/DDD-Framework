package com.ddd.domain.user.cache;

import com.ddd.domain.user.aggregate.UserAggregate;
import com.ddd.domain.user.entity.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public interface UserCacheService {

  void saveCache(UserAggregate userAggregate);

}
