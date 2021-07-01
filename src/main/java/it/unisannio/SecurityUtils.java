package it.unisannio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class SecurityUtils {

   private static final Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);

   private SecurityUtils() { }

   /**
    * Get the login of the current user.
    *
    * @return the login of the current user.
    */
   public static Optional<String> getCurrentUsername() {
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null) {
         LOG.debug("no authentication in security context found");
         return Optional.empty();
      }

      String username = null;
      if (authentication.getPrincipal() instanceof UserDetails) {
         UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
         username = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
         username = (String) authentication.getPrincipal();
      }

      LOG.debug("found username '{}' in security context", username);

      return Optional.ofNullable(username);
   }

   public static String resolveToken(String bearerToken) {
      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }
      return null;
   }
}
