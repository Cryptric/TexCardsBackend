package ch.cryptric.tex.cards.texcardsrestservice.service;

import ch.cryptric.tex.cards.texcardsrestservice.model.TexCardsUser;
import ch.cryptric.tex.cards.texcardsrestservice.repository.TexCardsUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    final TexCardsUserRepository texCardsUserRepository;

    public JwtUserDetailsService(TexCardsUserRepository texCardsUserRepository) {
        this.texCardsUserRepository = texCardsUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TexCardsUser user = texCardsUserRepository.findTexCardsUserByUserName(username);
        if (user == null) {
            throw new BadCredentialsException("no such user");
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER_ROLE")); // TODO remove?
        return new User(user.getUserName(), user.getPassword(), authorityList);
    }

    public UserDetails createUserDetails(String username, String password) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER_ROLE")); // TODO remove?
        return new User(username, password, authorityList);
    }

}
