package it.unisannio.model;

import it.unisannio.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;


import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class UserTest {

    static User u1, u2;
    static Authority a1, a2;


    @BeforeEach
    public void setUp() throws Exception {
        u1 = new User();
        u1.setUsername("robric");
        u1.setFirstname("Roberto");
        u1.setLastname("Ricci");
        u1.setEmail("riccirob@gmail.com");
        u1.setPassword("password");
        u1.setActivated(true);
        Set<Authority> authorities = new HashSet<>();
        a1 = new Authority();
        a2 = new Authority();
        a1.setName(Role.ROLE_MANAGER);
        a2.setName(Role.ROLE_PASSENGER);
        authorities.add(a1);
        authorities.add(a2);
        u1.setAuthorities(authorities);

        u2 = new User();

    }

    @Test
    public void test() {

        assertTrue(u1.isActivated());
        assertEquals(u1.getFirstname(), "Roberto");
        assertEquals(u1.getLastname(), "Ricci");
        assertEquals(u1.getEmail(), "riccirob@gmail.com");
        assertEquals(u1.getAuthorities().size(), 2);
        assertEquals(u1.getUsername(), "robric");
        assertEquals(u1.getPassword(), "password");
        u1.setId(10L);
        assertTrue(u1.hashCode() >0);
        assertEquals(u1.getId(), 10L);
        assertTrue(u1.equals(u2) == false);
        assertTrue(u1.equals(u1));
        assertFalse(u1.equals(u2));
        assertTrue(u1.toString() != null);

    }


}