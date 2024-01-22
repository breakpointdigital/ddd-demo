package pl.bpd.ddd.infrastructure.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.bpd.ddd.application.shared.CurrentUserInfo;
import pl.bpd.ddd.application.shared.UserRole;
import pl.bpd.ddd.infrastructure.repository.JpaMemberRepository;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final JpaMemberRepository jpaMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return jpaMemberRepository.findMemberByUsername(username)
                .map(member -> new CustomUserDetails(member.userId(), member.username(), member.password(), member.email(), Set.of(member.getRole())))
                .orElseThrow(() -> {
                    log.debug("User {} not found", username);
                    return new UsernameNotFoundException("User not found");
                });
    }

    @AllArgsConstructor
    private static class CustomUserDetails implements UserDetails, CredentialsContainer, CurrentUserInfo {
        private final String id;
        private final String username;
        private String password;
        private final String email;
        private final Set<String> roles;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return roles.stream().map(role -> new SimpleGrantedAuthority(UserRole.PREFIX + role)).collect(Collectors.toSet());
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public void eraseCredentials() {
            password = null;
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public String username() {
            return username;
        }

        @Override
        public String email() {
            return email;
        }

        @Override
        public Set<String> roles() {
            return roles;
        }
    }
}
