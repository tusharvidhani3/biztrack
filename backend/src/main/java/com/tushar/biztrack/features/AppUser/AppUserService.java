package com.tushar.biztrack.features.AppUser;

import com.tushar.biztrack.common.security.UserPrincipal;
import com.tushar.biztrack.features.auth.AuthRequest;

public interface AppUserService {
    void register(AuthRequest authRequest);
    UserPrincipal authenticate(AuthRequest authRequest);
}
