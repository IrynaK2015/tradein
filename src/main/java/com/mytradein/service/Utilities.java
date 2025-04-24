package com.mytradein.service;

import com.mytradein.model.AuthRoles;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utilities {

    public static User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public static boolean isCurrentUserSupervisor() {
        return new ArrayList<>(getCurrentUser().getAuthorities()).stream()
                .anyMatch(r -> r.getAuthority().equals(AuthRoles.ADMIN.toString()));
    }

    public static List<Integer> getPageNumbers(Page pageObject) {
        int totalPages = pageObject.getTotalPages();
        if (totalPages > 0) {
            return IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        return null;
    }

    public static String encodeSha256(String msg) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(msg);
    }
}
